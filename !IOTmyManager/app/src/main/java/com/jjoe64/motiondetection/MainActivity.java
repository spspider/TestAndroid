package com.jjoe64.motiondetection;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.widget.TextView;

import com.example.sp_1.iotmymanager.R;
import com.jjoe64.motiondetection.motiondetection.MotionDetector;
import com.jjoe64.motiondetection.motiondetection.MotionDetectorCallback;

public class MainActivity extends AppCompatActivity {
    private TextView txtStatus;
    private MotionDetector motionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera);

        txtStatus = (TextView) findViewById(R.id.txtStatus);

        motionDetector = new MotionDetector(this, (SurfaceView) findViewById(R.id.surfaceView));
        motionDetector.setMotionDetectorCallback(new MotionDetectorCallback() {
            @Override
            public void onMotionDetected() {

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(80);
                txtStatus.setText("Motion detected");

            }

            @Override
            public void onTooDark() {
                txtStatus.setText("Too dark here");
            }

            @Override
            public void ReadyToListen() {

            }
        });

        ////// Config Options
        motionDetector.setCheckInterval(2000);
        motionDetector.setLeniency(20);
        motionDetector.setMinLuma(1000);
    }


    @Override
    protected void onResume() {
        super.onResume();
        motionDetector.onResume();

        if (motionDetector.checkCameraHardware()) {
            txtStatus.setText("Camera found");
        } else {
            txtStatus.setText("No camera available");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        motionDetector.onPause();
    }

}
