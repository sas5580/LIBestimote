package com.estimote.examples.demos.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;
import com.estimote.examples.demos.R;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Visualizes distance from beacon to the device.
 *
 * @author wiktor@estimote.com (Wiktor Gworek)
 */
public class FindBookActivity extends ActionBarActivity {

    private static final String TAG = FindBookActivity.class.getSimpleName();

    // Y positions are relative to height of bg_distance image.
    private static final double RELATIVE_START_POS = 320.0 / 1110.0;
    private static final double RELATIVE_STOP_POS = 885.0 / 1110.0;
    public static final String EXTRAS_TARGET_ACTIVITY = "extrasTargetActivity";
    public static final String EXTRAS_BEACON = "extrasBeacon";
    private BeaconManager beaconManager;
    private Beacon beacon;
    private Region region;

    private View dotView;
    private int startY = -1;
    private int segmentLength = -1;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_view);
        dotView = findViewById(R.id.dot);
        beacon = getIntent().getParcelableExtra(ProximityActivity.EXTRAS_BEACON);
        region = new Region("regionid", beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
        if (beacon == null) {
            Toast.makeText(this, "Beacon not found in intent extras", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        name = (TextView) findViewById(R.id.bookName);
        name.setText(getIntent().getStringExtra("Name"));
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> rangedBeacons) {
                // Note that results are not delivered on UI thread.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Just in case if there are multiple beacons with the same uuid, major, minor.
                        Beacon foundBeacon = null;
                        for (Beacon rangedBeacon : rangedBeacons) {
                            if (rangedBeacon.getMacAddress().equals(beacon.getMacAddress())) {
                                foundBeacon = rangedBeacon;
                            }
                        }
                        if (foundBeacon != null) {
                            updateDistanceView(foundBeacon);
                        }
                    }
                });
            }
        });

        final View view = findViewById(R.id.sonar);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                startY = (int) (RELATIVE_START_POS * view.getMeasuredHeight());
                int stopY = (int) (RELATIVE_STOP_POS * view.getMeasuredHeight());
                segmentLength = stopY - startY;

                dotView.setVisibility(View.VISIBLE);
                dotView.setTranslationY(computeDotPosY(beacon));
            }
        });
    }

    private void updateDistanceView(Beacon foundBeacon) {
        if (segmentLength == -1) {
            return;
        }

        dotView.animate().translationY(computeDotPosY(foundBeacon)).start();
    }

    private int computeDotPosY(Beacon beacon) {
        // Let's put dot at the end of the scale when it's further than 6m.
        double distance = Math.min(Utils.computeAccuracy(beacon), 2.0);
        ////////////////////////////////////
        TextView range = (TextView) findViewById(R.id.outOfRange);
        if (Utils.computeAccuracy(beacon)>=1.0){
            range.setText(String.format("Out of Range: %.2fm ", Utils.computeAccuracy(beacon)));
        }
        else{
            range.setText(String.format("Within Range: %.2fm", Utils.computeAccuracy(beacon)));
        }
        ////////////////////////////////////
        return startY + (int) (segmentLength * (distance / 6.0));
    }

    @Override
    protected void onStart() {
        super.onStart();

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onStop() {
        beaconManager.disconnect();

        super.onStop();
    }
}
