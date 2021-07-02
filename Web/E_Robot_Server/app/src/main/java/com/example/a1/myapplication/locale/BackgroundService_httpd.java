package com.example.a1.myapplication.locale;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.a1.myapplication.MyServer;

import java.io.IOException;


/**
 * Created by Башня1 on 06.01.2019.
 */

public class BackgroundService_httpd extends Service {
    public static final String BUNDLE_HTTPS = "com.E-robot.https.Service";
    //private static Set<String> HTTPS;
    public static final String TAG = "locale-HTTP-plugin";
    public static final Intent REQUEST_REQUERY = new Intent(com.twofortyfouram.locale.api.Intent.ACTION_REQUEST_QUERY);
    //private BroadcastReceiver mReceiver;
    private static boolean isRunning = false;
    int isStartEdit=0;
    public static boolean isRunning() {
        return isRunning;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isStartEdit = intent.getIntExtra(EditActivity.START_FROM_EDIT, 0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            if(!isRunning)
            new MyServer(getApplicationContext(),isStartEdit);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't start server:\n" + e);
            Log.i(TAG, "не удалось запустить сервер" + e);
        }
        isRunning = true;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(mReceiver);
        isRunning = false;
    }
}
