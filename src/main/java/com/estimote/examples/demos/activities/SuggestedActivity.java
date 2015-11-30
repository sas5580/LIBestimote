package com.estimote.examples.demos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.estimote.examples.demos.R;
import com.estimote.examples.demos.adapters.BeaconListAdapter;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.internal.Util;

import java.util.Collections;
import java.util.List;

/**
 * Displays list of found beacons sorted by RSSI.
 * Starts new activity with selected beacon if activity was provided.
 *
 * @author wiktor.gworek@estimote.com (Wiktor Gworek)
 */
public class SuggestedActivity extends ActionBarActivity {

    private static final String TAG = SuggestedActivity.class.getSimpleName();

    public static final String EXTRAS_TARGET_ACTIVITY = "extrasTargetActivity";
    public static final String EXTRAS_BEACON = "extrasBeacon";

    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);

    private BeaconManager beaconManager;
    private BeaconListAdapter adapter;
    private String beac1="C9:CB:96:80:77:15";
    private String beac2="C6:C2:91:0B:73:B8";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        // Configure device list.
        adapter = new BeaconListAdapter(this);

        // Configure BeaconManager.
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                // Note that results are not delivered on UI thread.
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        // Note that beacons reported here are already sorted by estimated
                        // distance between device and beacon.
                        adapter.replaceWith(beacons);

                        if (adapter.getCount()>0) {
                            Beacon beac = adapter.getItem(0);
                            if (beac.getMacAddress().toStandardString().equals(beac1)) {
                                Intent intent = new Intent(SuggestedActivity.this, DisActivity.class);
                                startActivity(intent);
                            } else if(beac.getMacAddress().toStandardString().equals(beac2)) {
                                Intent intent = new Intent(SuggestedActivity.this, DiscoverActivity.class);
                                startActivity(intent);
                            }
                        }

                    }
                });
            }
        });
    }

    @Override protected void onDestroy() {
        beaconManager.disconnect();
        super.onDestroy();
    }

    @Override protected void onResume() {
        super.onResume();

        if (SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            startScanning();
        }
    }

    @Override protected void onStop() {
        beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);

        super.onStop();
    }

    private void startScanning() {
        adapter.replaceWith(Collections.<Beacon>emptyList());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
            }
        });
    }

    private AdapterView.OnItemClickListener createOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getIntent().getStringExtra(EXTRAS_TARGET_ACTIVITY) != null) {
                    try {
                        Class<?> clazz = Class.forName(getIntent().getStringExtra(EXTRAS_TARGET_ACTIVITY));
                        Intent intent = new Intent(SuggestedActivity.this, clazz);
                        intent.putExtra(EXTRAS_BEACON, adapter.getItem(position));
                        startActivity(intent);

                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, "Finding class by name failed", e);
                    }
                }
            }
        };
    }
}
