package com.estimote.examples.demos.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.estimote.examples.demos.R;

import java.io.File;

public class ProxCheck extends Activity {
    Button button,returnb;
    ImageView imageView;
    static final int CAM_REQUREST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prox_check);
        button = (Button) findViewById(R.id.button);
        returnb = (Button) findViewById(R.id.rbutton);
        imageView = (ImageView) findViewById(R.id.image_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent, CAM_REQUREST);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        button.setText("Successful Checkout Press \nTo Sign Out Another Item");
                        button.setTextSize(20);
                    }
                }, 2000);

            }
        });
        returnb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProxCheck.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private File getFile() {
        File folder = new File("sdcard/camera_app");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File image_file = new File(folder, "cam_image.jpg");
        return image_file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = "sdcard/camera_app/cam_image.jpg";
        imageView.setImageDrawable(Drawable.createFromPath(path));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
