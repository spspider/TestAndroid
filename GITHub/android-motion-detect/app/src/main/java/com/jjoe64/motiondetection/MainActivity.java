package com.jjoe64.motiondetection;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;

import com.jjoe64.motiondetection.motiondetection.MotionDetector;
import com.jjoe64.motiondetection.motiondetection.MotionDetectorCallback;
import com.jjoe64.motiondetection.TTS.TTSService;

public class MainActivity extends AppCompatActivity {
    private TextView txtStatus;
    private MotionDetector motionDetector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = new Intent(this, TTSService.class);
        intent.putExtra("locale", "ru");
        intent.putExtra("message", "Синтез речи работает");
        startService(intent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStatus = (TextView) findViewById(R.id.txtStatus);

        motionDetector = new MotionDetector(this, (SurfaceView) findViewById(R.id.surfaceView));
        motionDetector.setMotionDetectorCallback(new MotionDetectorCallback() {
            @Override
            public void onMotionDetected() {

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(80);
                txtStatus.setText("Motion detected");
                //Intent intent = new Intent(this, Text_Speech_Activity.class);
                send_detected();

            }
            ;

            @Override
            public void onTooDark() {
                txtStatus.setText("Too dark here");
            }

            @Override
            public void ReadyToListen() {

            }
        });

        ////// Config Options
        motionDetector.setCheckInterval(5000);
        motionDetector.setLeniency(20);
        motionDetector.setMinLuma(1000);
    }

    private void send_detected() {
        Intent intent = new Intent(this, TTSService.class);
        intent.putExtra("locale", "ru");
        intent.putExtra("message", "Движение обнаружено");
        startService(intent);
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
    protected void onDestroy() {
        super.onDestroy();

    }

}
