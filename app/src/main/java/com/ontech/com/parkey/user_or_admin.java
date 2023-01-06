package com.ontech.com.parkey;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.furkanakdemir.surroundcardview.SurroundCardView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class user_or_admin extends AppCompatActivity {

    SurfaceView cameraView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    TextView textView;
    int requestCode = 100;
/*

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = cm.getActiveNetworkInfo();
        return activenetwork != null && activenetwork.isConnectedOrConnecting();
    }

    public int flag;

    {
        flag = 0;
    }

    public String output = "";


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //  super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        return;
                }
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_or_admin);

        //startCameraSource();


     /*   Button ClickButton = (Button) findViewById(R.id.ClickButton);

        ClickButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        flag = 1;
                    }
                }
        );

        Button GoButton = (Button) findViewById(R.id.GoButton);

        GoButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if (!checkConnection()) {
                            Toast.makeText(getApplicationContext(), "Please Check your Internet Connection...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        switch_to_user_info(v);
                    }
                }
        );


        cameraView = (SurfaceView) findViewById(R.id.surface_view);
        textView = (TextView) findViewById(R.id.text_view);


        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available");
        } else {

            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)//to select the back cam of device
                    .setRequestedPreviewSize(1600, 1200) // width,ht of camera pixel frames
                    .setRequestedFps(2.0f) //sets the requested frame rate in fps
                    .setAutoFocusEnabled(true)// to be set to false

                    .build();

            // cameraSource.takePicture();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(user_or_admin.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestCode);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                *//**
         * Release resources for cameraSource
         *//*
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                *//**
         * Detect all the text from camera using TextBlock and the values into a stringBuilder
         * which will then be set to the textView.
         *//*
                @Override
                //to be written in onpress btn
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {
                        if (flag == 0) {
                            textView.post(new Runnable() {
                                @Override
                                public void run() {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (int i = 0; i < items.size(); ++i) {
                                        TextBlock item = items.valueAt(i);
                                        stringBuilder.append(item.getValue());
                                        stringBuilder.append("\n");
                                    }
                                    output = stringBuilder.toString();

                                    textView.setText(output);
                                }
                            });
                        }
                    }

                }
            });

        }
    }

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        textView = findViewById(R.id.text_view);
        cameraView = findViewById(R.id.surface_view);
        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();
        }
    }

            *//**
         * Add call back to SurfaceView and check if camera permission is granted.
         * If permission is granted we can start our cameraSource and pass it to surfaceView
         *//*




    public void switch_to_user_info(View v){
        Intent intent = new Intent(this, MainActivity.class);
        String message = output;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        recreate();
    }
    private void setStatusBarTransparent () {
        Window window = user_or_admin.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.TRANSPARENT);
    }*/
    }
}