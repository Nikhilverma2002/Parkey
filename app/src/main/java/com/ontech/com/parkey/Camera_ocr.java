package com.ontech.com.parkey;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.Objects;


public class Camera_ocr extends Fragment {

    SurfaceView cameraView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    TextView textView;
    int requestCode = 100;

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) requireActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                    if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
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

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera_ocr, container, false);

        //startCameraSource();


        TextView ClickButton =  view.findViewById(R.id.ClickButton);

        ClickButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if (flag == 0) {
                            flag = 1;
                            Toast.makeText(requireActivity().getApplicationContext(), "Scanner is paused again click the button to resume", Toast.LENGTH_LONG).show();
                        }
                        else
                            flag = 0;
                    }
                }
        );

        TextView GoButton =  view.findViewById(R.id.GoButton);

        GoButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if (!checkConnection()) {
                            //Toast.makeText(requireActivity().getApplicationContext(), "Please Check your Internet Connection...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        switch_to_user_info(v);
                    }
                }
        );


        cameraView = (SurfaceView) view.findViewById(R.id.surface_view);
        textView = (TextView) view.findViewById(R.id.text_view);


        TextRecognizer textRecognizer = new TextRecognizer.Builder(requireActivity().getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available");
        } else {

            cameraSource = new CameraSource.Builder(requireActivity().getApplicationContext(), textRecognizer)
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

                        if (ActivityCompat.checkSelfPermission(requireActivity().getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(requireActivity(),
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
        return view;
    }

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(requireActivity().getApplicationContext()).build();

        textView = view.findViewById(R.id.text_view);
        cameraView =  view.findViewById(R.id.surface_view);
        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            cameraSource = new CameraSource.Builder(requireActivity().getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();
        }
    }

    public void switch_to_user_info(View v){
        /*Intent intent = new Intent(this, MainActivity.class);
        String message = output;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        recreate();*/

        Toast.makeText(requireActivity().getApplicationContext(), "Hey yo done.", Toast.LENGTH_SHORT);
    }
}