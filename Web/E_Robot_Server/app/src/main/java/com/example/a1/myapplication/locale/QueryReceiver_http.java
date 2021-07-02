package com.example.a1.myapplication.locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.a1.myapplication.MyServer;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Башня1 on 06.01.2019.
 */

public class QueryReceiver_http extends BroadcastReceiver {
    String TAG = BackgroundService_httpd.TAG;

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (!com.twofortyfouram.locale.api.Intent.ACTION_QUERY_CONDITION.equals(intent.getAction())) {
            Log.w(TAG, "Received unexpected Intent action"); //$NON-NLS-1$//$NON-NLS-2$
            return;
        }
        final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.api.Intent.EXTRA_BUNDLE);
        if (bundle == null) {
            Log.e(TAG, "Received null BUNDLE"); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }
        if (!bundle.containsKey(BackgroundService_httpd.BUNDLE_HTTPS)) {
            Log.e(TAG, "Missing SSID param in Bundle");
            return;
        }
        if (!BackgroundService_httpd.isRunning()) {
            final Intent serviceIntent = new Intent(context, BackgroundService_httpd.class);
            serviceIntent.putExtras(intent);
            Log.e(TAG, "перезапуск сервиса");
            context.startService(serviceIntent);
        }
        // get all the ssids and see if we have a match
        final Set<String> HTTPs = MyServer.getHTTPS();
        String soughtHTTPS = bundle.getString(BackgroundService_httpd.BUNDLE_HTTPS);
        //soughtHTTPS = "/username=ok";





        if (HTTPs == null || !HTTPs.contains(soughtHTTPS)) {
            Log.i(TAG, "RESULT_CONDITION_UNSATISFIED");
            setResultCode(com.twofortyfouram.locale.api.Intent.RESULT_CONDITION_UNSATISFIED);
        } else {
            Log.i(TAG, "RESULT_CONDITION_SATISFIED");
            setResultCode(com.twofortyfouram.locale.api.Intent.RESULT_CONDITION_SATISFIED);
            //if (EditActivity. == 1) {

            //}
        }
        if (EditActivity.EditActivityOpen==0)
        MyServer.clearHTTPS();
        Log.i(TAG,"HTTPS, cleared");
        //
    }


}
