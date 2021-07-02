package com.example.sp_1.p0801_handler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    final String LOG_TAG = "myLogs";

    Handler h;
    Handler a;
    TextView tvInfo;
    Button btnStart;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        //setCursorVisible(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        btnStart = (Button) findViewById(R.id.btnStart);
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                // обновляем TextView
                tvInfo.setText("Закачано файлов: " + msg.what);
                if (msg.what == 10) btnStart.setEnabled(true);
            };
        };
        a = new Handler(){
            public void handleMessage(android.os.Message msg) {
                Toast.makeText(getApplicationContext(),"работает!", Toast.LENGTH_SHORT);                                
                Log.d(LOG_TAG, "handler a recieved"+msg.what);
            }
        };
    }

    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                btnStart.setEnabled(false);
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        for (int i = 1; i <= 10; i++) {
                            // долгий процесс
                            downloadFile();
                            h.sendEmptyMessage(i);
                            ///h.sen
                            // пишем лог
                            Log.d(LOG_TAG, "i = " + i);
                        }
                    }
                });
                t.start();
                break;
            case R.id.btnTest:
                Thread f = new Thread(new Runnable() {
                    @Override
                    public void run() {
                       int msg =100;
                        a.sendEmptyMessage(msg);
                    }
                });
                Log.d(LOG_TAG, "test");
                f.start();
                break;
            default:
                break;
        }
    }
    void downloadFile() {
        // пауза - 1 секунда
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
