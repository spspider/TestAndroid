package com.example.sp_1.iotmymanager.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.sp_1.iotmymanager.Activitys.MainDevicesPager;
import com.example.sp_1.iotmymanager.NotInPackage.MainActivity;
import com.example.sp_1.iotmymanager.Service.MQTT_Service_depricated;

import java.util.HashMap;


/**
 * Created by sp_1 on 14.02.2017.
 */

public class MqttAndroidClient {
    public static String IOTMANAGERdeviceCONFIG;
    //private Activity activity;
    boolean bound = false;
    Intent intent;
    private static MqttAndroidClient instance = null;
    public MQTT_Service_depricated mqttService;
    private MyServiceConnection serviceConnection = new MyServiceConnection();
    public String host,port,username,password;
    public static final String SUBSCRIBEIOT = "SUBSCRIBEIOT";

    //ServiceConnection sConn;
    //private static final String SERVICE_NAME = "org.eclipse.paho.android.service.MqttService";



    public void loadAllPreferences(Activity activity) {


        //MqttAndroidClient GC = new MqttAndroidClient();
        HashMap<String,String> Connectionmap=loadPreference(activity);
        host = Connectionmap.get(DBHelper.COLUMN_HOST);
        port = Connectionmap.get(DBHelper.COLUMN_port);
        username = Connectionmap.get(DBHelper.COLUMN_USER_NAME);
        password = Connectionmap.get(DBHelper.COLUMN_PASSWORD);

        Bundle bundle = new Bundle();
        bundle.putString(DBHelper.COLUMN_HOST,host);
        bundle.putString(DBHelper.COLUMN_port,port);
        bundle.putString(DBHelper.COLUMN_USER_NAME,username);
        bundle.putString(DBHelper.COLUMN_PASSWORD,password);
        startMyService(activity,bundle);
        sendSubscribe(activity);
        Log.d(MainActivity.TAG, String.valueOf(Connectionmap));
    }

    private void sendSubscribe(Activity activity) {
        Intent intentSend= new Intent(MQTT_Service_depricated.RECIEVE_NEW_CONNECTION_OPTION);
        Bundle bundle = new Bundle();
        bundle.putString(SUBSCRIBEIOT,"/IoTmanager");

        intentSend.putExtras(bundle);
        activity.sendBroadcast(intentSend);
    }

    private final class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mqttService = ((MQTT_Service_depricated.MyBinder) binder).getService();
            //bindedService = true;
            // now that we have the service available, we can actually
            // connect...
            mqttService.test();
            Log.d(MainActivity.TAG, "Service bounded");
            //doConnect();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(MainActivity.TAG, "Service unbounded");
            mqttService = null;
            bound = false;
        }
    }

    public HashMap<String,String> loadPreference(Activity activity) {
        SharedPreferences sPref = activity.getSharedPreferences("mysettings",Context.MODE_PRIVATE);
        HashMap map= new HashMap();
        //sPref = getPreferences(MODE_PRIVATE);
        host = sPref.getString(DBHelper.COLUMN_HOST, "");
        port = sPref.getString(DBHelper.COLUMN_port, "0");
        username = sPref.getString(DBHelper.COLUMN_USER_NAME, "");
        password = sPref.getString(DBHelper.COLUMN_PASSWORD, "");
        map.put(DBHelper.COLUMN_HOST,host);
        map.put(DBHelper.COLUMN_port,port);
        map.put(DBHelper.COLUMN_USER_NAME,username);
        map.put(DBHelper.COLUMN_PASSWORD,password);

        //sendNewPrefernces(activity,host,port,username,password);
        //setupConnection(activity, host, port, username, password);
        return map;
    }
    public void savePreferece(Activity activity, HashMap<String, String> map) {
        SharedPreferences sPref = activity.getSharedPreferences("mysettings",Context.MODE_PRIVATE);
        //sPref = getPreferences(MODE_PRIVATE);
        host=map.get(DBHelper.COLUMN_HOST);
        port=map.get(DBHelper.COLUMN_port);
        username=map.get(DBHelper.COLUMN_USER_NAME);
        password=map.get(DBHelper.COLUMN_PASSWORD);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(DBHelper.COLUMN_HOST,host);
        ed.putString(DBHelper.COLUMN_port,port);
        ed.putString(DBHelper.COLUMN_USER_NAME,username);
        ed.putString(DBHelper.COLUMN_PASSWORD,password);
        Bundle bundle = new Bundle();
        bundle.putString(DBHelper.COLUMN_HOST,host);
        bundle.putString(DBHelper.COLUMN_port,port);
        bundle.putString(DBHelper.COLUMN_USER_NAME,username);
        bundle.putString(DBHelper.COLUMN_PASSWORD,password);
        startMyService(activity,bundle);
        sendNewPrefernces(activity,host,port,username,password);
        ed.commit();
        //mqttService.setupConnection(activity, host, port, username, password);
        //sendNewPrefernces(host,port,username,password);
    }
    public void startMyService(Activity activity,Bundle bundle) {
        Intent intentStop;
        intentStop = new Intent(activity, MQTT_Service_depricated.class).setAction(MainActivity.TAG+".STOP");
        activity.stopService(intentStop);
        Log.d(MainActivity.TAG, String.valueOf(activity));
        Intent intentStart;
        intentStart = new Intent(activity, MQTT_Service_depricated.class).setAction(MainActivity.TAG+".START");
        intentStart.putExtras(bundle);
        // стартуем сервис
        activity.startService(intentStart);

    }
    private void sendNewPrefernces(Activity activity,String columnHost, String column_port, String columnUserName, String columnPassword) {
        Intent intentSend= new Intent(MQTT_Service_depricated.RECIEVE_NEW_CONNECTION_OPTION);
        Bundle bundle = new Bundle();
        bundle.putString(DBHelper.COLUMN_HOST,columnHost);
        bundle.putString(DBHelper.COLUMN_port,column_port);
        bundle.putString(DBHelper.COLUMN_USER_NAME,columnUserName);
        bundle.putString(DBHelper.COLUMN_PASSWORD,columnPassword);
        intentSend.putExtras(bundle);
        activity.sendBroadcast(intentSend);
        //startMyService();
    }
    public synchronized static MqttAndroidClient getInstance()
    {
        if (instance == null) {
            instance = new MqttAndroidClient();
        }

        return instance;
    }
    public void startBind(Activity activity, String connection) {

        if (mqttService==null) {
            intent = new Intent(activity, MQTT_Service_depricated.class);
            activity.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            //intent.setAction(MQTT_Service_depricated.ACTION_START);
            activity.startService(intent);

            Log.d(MainActivity.TAG, "startBind" +connection);
        }
        //mqttService.
        //mqttService.setupConnection(context, host, port, login, Password);
    }


}
