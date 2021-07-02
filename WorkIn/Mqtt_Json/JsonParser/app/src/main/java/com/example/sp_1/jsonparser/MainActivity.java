package com.example.sp_1.jsonparser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.id.message;


public class MainActivity extends AppCompatActivity{
    public static String JsonString;
    private String TAG = MainActivity.class.getSimpleName();
    public ListView lv;
    public static Context mContext;
    public static MainActivity activity;
    private static MainActivity instance = null;
    private static final int Connect = 0;
    private static final int Subscribe = 1;
    private static final int Get_Con_exec = 2;
    private static final int insertMessage = 3;
    private static final int publish = 4;
    private GlobalClass app;
    BroadcastReceiver br;
    private static TextView tv;
    public static ScrollView scrollView1;


    public synchronized static MainActivity getInstance()
    {
        if (instance == null) {
            instance = new MainActivity();
        }

        return instance;
    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JsonString = getString(R.string.json_str);

        mContext=getApplicationContext();
        //activity = this;
        //final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        app = (GlobalClass) getApplication().getApplicationContext();
        app.setActivity(this);
        app.setContext(this);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String task = intent.getStringExtra("backString");
                myLog(task);

                Log.d("my_log","recieved");
            }

        };

        IntentFilter intFilt = new IntentFilter("RECIVEFILTER");
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);
        /////////////////////////////////////////////////

        //asyncTask.delegate = this;
        //lv = (ListView) findViewById(R.id.list);

        LinearLayout ll = (LinearLayout) findViewById(R.id.myLiLa);
        tv = new TextView(this);
        tv.setText("");
        ll.addView(tv);
        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        myLog("works");
        //CreateListItem li = new CreateListItem();
        //li.createList();
        //createList();
        //CreateListItem LI = new CreateListItem(this);
        //LI.createList();
    }



    public Context getContext() {

        return  mContext;//на запрос getContext() отвечаем контекстом
    }

    static void myLog(String str) {
        tv.append(str + "\n");
        scrollView1.post(new Runnable() {
            @Override
            public void run() {
                scrollView1.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add(Menu.NONE, Connect, Menu.NONE, "Connect");
        menu.add(Menu.NONE, Subscribe, Menu.NONE, "Subscribe");
        menu.add(Menu.NONE, publish, Menu.NONE, "Publish");
        menu.add(Menu.NONE, Get_Con_exec, Menu.NONE, "GetContacts");
        menu.add(Menu.NONE, insertMessage, Menu.NONE, "insertMessage");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle item selection
        switch (item.getItemId()) {
            case Subscribe:
                intent = new Intent(this, MQTT_Service_depricated.class);
                intent.setAction(MQTT_Service_depricated.ACTION_SUBSCRIBE);
                startService(intent);
                return true;
            case Get_Con_exec:

                JsonParser asyncTask = new JsonParser(new JsonParser.AsyncResponse() {
                    @Override
                    public void processFinish(Void contactList) {
                        Log.d("my_log","Response From Asynchronous task:");
                        //addAdapter(contactList);
                    }
                });
                asyncTask.execute();

                //asyncTask.execute();
                return true;
            case insertMessage:
                //JsonParcer_MQTT_subscribe parcer = new JsonParcer_MQTT_subscribe(getApplicationContext());

                    //JsonParcer_MQTT_subscribe.insertMessage("","");
                    JsonParcer_MQTT_subscribe parcer = new JsonParcer_MQTT_subscribe();
                    //String Allvalues = parcer.insertMessage("","");
                    String message = "{\"id\":\"2\",\"page\":\"Kitchen\",\"descr\":\"Dimmer\",\"widget\":\"range\",\"topic\":\"/IoTmanager/dev01-kitchen/dim-light\",\"style\":\"range-calm\",\"badge\":\"badge-assertive\",\"leftIcon\":\"ion-ios-rainy-outline\",\"rightIcon\":\"ion-ios-rainy\"}";
                    String message2 = "{\"id\":\"1\",\"page\":\"Kitchen\",\"descr\":\"Light-1\",\"widget\":\"toggle\",\"topic\":\"/IoTmanager/dev01-kitchen/light1\",\"color\":\"orange\"}";
                    String message3 ="{\"id\":\"0\",\"page\":\"Kitchen\",\"descr\":\"Light-0\",\"widget\":\"toggle\",\"topic\":\"/IoTmanager/dev01-kitchen/light0\",\"color\":\"blue\"}";
                    String message4 =  "{\"id\":\"3\",\"page\":\"Kitchen\",\"descr\":\"ADC\",\"widget\":\"small-badge\",\"topic\":\"/IoTmanager/dev01-kitchen/ADC\",\"badge\":\"badge-balanced\"}";
                    String message5 = "{\"id\":\"4\",\"page\":\"Outdoor\",\"descr\":\"Garden light\",\"widget\":\"toggle\",\"topic\":\"/IoTmanager/dev01-kitchen/light4\",\"color\":\"red\"}";
                    //message = getResources().getString(R.string.json_str);
                    //String message3  = Resources.getSystem().getString(R.string.json_str);
                    //myLog(message3);
                    parcer.insertMessage(message,"");
                    parcer.insertMessage(message2,"");
                    parcer.insertMessage(message3,"");
                    parcer.insertMessage(message4,"");
                    parcer.insertMessage(message5,"");
                return true;
            case Connect:
                intent = new Intent(this, MQTT_Service_depricated.class);
                intent.setAction(MQTT_Service_depricated.ACTION_START);
                startService(intent);
                return true;
            case publish:
                intent = new Intent(this, MQTT_Service_depricated.class);
                intent.setAction(MQTT_Service_depricated.ACTION_PUBLISH);
                startService(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

/*
    @Override
    public void processFinish(ArrayList<HashMap<String, String>> contactList) {
        Log.d("my_log","Response From Asynchronous task:");
        addAdapter(contactList);
    }
    */

