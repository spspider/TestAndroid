package com.example.sp_1.iotmymanager.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.example.sp_1.iotmymanager.NotInPackage.MainActivity;
import com.example.sp_1.iotmymanager.util.DBHelper;
import com.example.sp_1.iotmymanager.util.MqttAndroidClient;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.util.Locale;

/**
 * Created by sp_1 on 25.11.2016.
 */

public class MQTT_Service_depricated extends Service
{
    public static final String DEBUG_TAG = MainActivity.TAG; // Debug TAG

    private static final String MQTT_THREAD_NAME = "MqttService[" + DEBUG_TAG + "]"; // Handler Thread ID
    public static String RECIEVE_NEW_CONNECTION_OPTION = "newConnective.OptionReceiever";

    private String MQTT_BROKER=""; // Broker URL or IP Address
    private long MQTT_PORT=0; // Broker Port
    private String username="";
    private String password="" ;

    //private MqttServiceBinder mqttServiceBinder;

    public static final int MQTT_QOS_0 = 0; // QOS Level 0 ( Delivery ---Once no confirmation )
    public static final int MQTT_QOS_1 = 1; // QOS Level 1 ( Delevery at least Once with confirmation )
    public static final int MQTT_QOS_2 = 2; // QOS Level 2 ( Delivery only once with confirmation with handshake )

    private static final int MQTT_KEEP_ALIVE = 240000; // KeepAlive Interval in MS
    private static final String MQTT_KEEP_ALIVE_TOPIC_FORAMT = "/users/%s/keepalive"; // Topic format for KeepAlives
    private static final byte[]     MQTT_KEEP_ALIVE_MESSAGE = { 0 }; // Keep Alive message to send
    private static final int MQTT_KEEP_ALIVE_QOS = MQTT_QOS_0; // Default Keepalive QOS

    private static final boolean MQTT_CLEAN_SESSION = true; // Start a clean session?

    private static final String MQTT_URL_FORMAT = "tcp://%s:%d"; // URL Format normally don't change

    public static final String ACTION_START  = DEBUG_TAG + ".START"; // Action to start
    public static final String ACTION_STOP   = DEBUG_TAG + ".STOP"; // Action to stop
    public static final String ACTION_SUBSCRIBE = DEBUG_TAG+".SUBSCRIBE";
    public static final String ACTION_PUBLISH = DEBUG_TAG+".PUBLISH";
    public static final String ACTION_KEEPALIVE= DEBUG_TAG + ".KEEPALIVE"; // Action to keep alive used by alarm manager
    public static final String ACTION_RECONNECT= DEBUG_TAG + ".RECONNECT"; // Action to reconnect


    private static final String DEVICE_ID_FORMAT = "andr_%s"; // Device ID Format, add any prefix you'd like
    // Note: There is a 23 character limit you will get
    // An NPE if you go over that limit
    private boolean mStarted = false;   // Is the Client started?
    private String mDeviceId;       // Device ID, Secure.ANDROID_ID
    private Handler mConnHandler;     // Seperate Handler thread for networking

    private MqttDefaultFilePersistence mDataStore; // Defaults to FileStore
    private MemoryPersistence mMemStore; // On Fail reverts to MemoryStore
    private MqttConnectOptions mOpts; // Connection Options

    private MqttTopic mKeepAliveTopic; // Instance Variable for Keepalive topic
    private MqttTopic mpublish_topic; // Instance Variable for Keepalive topic

    private MqttClient mClient; // Mqtt Client

    private AlarmManager mAlarmManager; // Alarm manager to perform repeating tasks
    private ConnectivityManager mConnectivityManager; // To check for connectivity changes
    private static MQTT_Service_depricated instance = null;

    MyBinder binder = new MyBinder();
    private Context context;


    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }



    public void test() {
        Log.d(MainActivity.TAG,"----------------test----------------------");
    }



    public class MyBinder extends Binder {
        public MQTT_Service_depricated getService() {
            //Log.d(MainActivity.TAG,"bind returned");
            return MQTT_Service_depricated.this;
        }
    }


    //private NetworkConnectionIntentReceiver networkConnectionMonitor;
    /**
     * Start MQTT Client
     * //@param Context context to start the service with
     * @return void
     */
    /*
    private class NetworkConnectionIntentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(DEBUG_TAG, "Internal network status receive.");
            // we protect against the phone switching off
            // by requesting a wake lock - we request the minimum possible wake
            // lock - just enough to keep the CPU running until we've finished
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            PowerManager.WakeLock wl = pm
                    .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MQTT");
            wl.acquire();
            Log.i(DEBUG_TAG,"Reconnect for Network recovery.");
            if (mClient.isConnected()) {
                Log.i(DEBUG_TAG,"Online,reconnect.");
                // we have an internet connection - have another try at
                // connecting
                //--------//reconnect();
            } else {
                //---------//notifyClientsOffline();
            }

            wl.release();
        }
    }
        */

    public static void actionStart(Context ctx) {
        Intent i = new Intent(ctx,MQTT_Service_depricated.class);
        i.setAction(ACTION_START);
        ctx.startService(i);
    }
    /**
     * Stop MQTT Client
     * //@param Context context to start the service with
     * @return void
     */
    public static void actionStop(Context ctx) {
        Intent i = new Intent(ctx,MQTT_Service_depricated.class);
        i.setAction(ACTION_STOP);
        ctx.startService(i);
    }
    /**
     * Send a KeepAlive Message
     * //@param Context context to start the service with
     * @return void
     */
    public static void actionKeepalive(Context ctx) {
        Intent i = new Intent(ctx,MQTT_Service_depricated.class);
        i.setAction(ACTION_KEEPALIVE);
        ctx.startService(i);
    }

    /**
     * Initalizes the DeviceId and most instance variables
     * Including the Connection Handler, Datastore, Alarm Manager
     * and ConnectivityManager.
     */
    public synchronized static MQTT_Service_depricated getInstance()
    {
        if (instance == null) {
            instance = new MQTT_Service_depricated();
        }

        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //retriveConnection();
        Log.d(MainActivity.TAG, "MyService onCreate");
        mDeviceId = String.format(DEVICE_ID_FORMAT,
                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        HandlerThread thread = new HandlerThread(MQTT_THREAD_NAME);
        thread.start();

        mConnHandler = new Handler(thread.getLooper());//если поток не включен, он его включет

        mDataStore = new MqttDefaultFilePersistence(getCacheDir().getAbsolutePath());



        // Do not set keep alive interval on mOpts we keep track of it with alarm's

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        registerReceiver(SetNewConnection,new IntentFilter(MQTT_Service_depricated.RECIEVE_NEW_CONNECTION_OPTION));
    }
public void setOnCreate(){
    mOpts = new MqttConnectOptions();
    mOpts.setCleanSession(MQTT_CLEAN_SESSION);
    //String password = "5506487";
    //String userName = "spspider";
    mOpts.setPassword(password.toCharArray());
    mOpts.setUserName(username);
}
    private final BroadcastReceiver SetNewConnection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle getBundle = intent.getExtras();

            if (getBundle.get(DBHelper.COLUMN_HOST)!=null) {
                //setupConnection(context,)
                setupConnection(
                        context,
                        getBundle.getString(DBHelper.COLUMN_HOST),
                        getBundle.getString(DBHelper.COLUMN_port),
                        getBundle.getString(DBHelper.COLUMN_USER_NAME),
                        getBundle.getString(DBHelper.COLUMN_PASSWORD));
            }else if(getBundle.get(MqttAndroidClient.SUBSCRIBEIOT)!=null){
                subscribe((String) getBundle.get(MqttAndroidClient.SUBSCRIBEIOT));
                //Log.d(MainActivity.TAG,"Subscribe");
            }else if (getBundle.get(MqttAndroidClient.IOTMANAGERdeviceCONFIG)!=null){
                subscribe((String) getBundle.get(MqttAndroidClient.IOTMANAGERdeviceCONFIG));
            }


        }
    };

    public void setupConnection(Context context, String host, String port, String login, String password) {

        this.context = context;
        this.username = login;
        this.password = password;
        this.MQTT_BROKER = host;
        this.MQTT_PORT = Long.parseLong((port== null ? "0" : port));
        Log.d(MainActivity.TAG,"Receved new Connection"+" "+login+" "+password);
    }
    /**
     * Service onStartCommand
     * Handles the action passed via the Intent
     *
     * @return START_REDELIVER_INTENT
     */


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        if (bundle!=null) {

            if (bundle.get(DBHelper.COLUMN_HOST)!=null) {
                //Log.d(MainActivity.TAG, String.valueOf(bundle)+bundle.get(DBHelper.COLUMN_port));
                this.MQTT_BROKER = (String) bundle.get(DBHelper.COLUMN_HOST);
                if (intent.getStringExtra(DBHelper.COLUMN_port) != null) {
                    this.MQTT_PORT = Integer.parseInt(String.valueOf(bundle.get(DBHelper.COLUMN_port)));
                }
                this.username = intent.getStringExtra(DBHelper.COLUMN_USER_NAME);
                this.password = intent.getStringExtra(DBHelper.COLUMN_PASSWORD);
                if (password != null)
                    if (username != null) {
                        setOnCreate();
                    }
                ;
            }
            else{
                Log.d(MainActivity.TAG,"This bundle doesnot have host");
            }
        }
        else{
            Log.d(MainActivity.TAG,"bundle is null");
        }
        Log.i(DEBUG_TAG,"Received action of " + action);

        if(action == null) {
            Log.i(DEBUG_TAG,"Starting service with no action Probably from a crash");
        } else {

            if(action.equals(ACTION_START)) {
                Log.i(DEBUG_TAG,"Received ACTION_START");
                start();
            } else if(action.equals(ACTION_STOP)) {
                stop();
            }else if(action.equals(ACTION_SUBSCRIBE)) {
                Log.i(DEBUG_TAG,"Received ACTION_SUBSCRIBE");
                    subscribe("/IoTmanager/dev01-kitchen/config");

            } else if(action.equals(ACTION_KEEPALIVE)) {
                keepAlive();
            } else if(action.equals(ACTION_RECONNECT)) {
                if(isNetworkAvailable()) {
                    reconnectIfNecessary();
                }
            }
            else if(action.equals(ACTION_PUBLISH)) {
                Log.i(DEBUG_TAG,"Received ACTION_PUBLSIH");



                    publish_token("/IoTmanager");
                try {
                    publish("/IoTmanager","HELLO");
                } catch (MqttConnectivityException e) {
                    e.printStackTrace();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                //publishTemperature("textToPublish");



            }
            else{
                Log.i(DEBUG_TAG,"Action not found");
            }
        }

        return START_REDELIVER_INTENT;
    }

    /**
     * Attempts connect to the Mqtt Broker
     * and listen for Connectivity changes
     * via ConnectivityManager.CONNECTVITIY_ACTION BroadcastReceiver
     */
    public synchronized void start() {



        if(mStarted) {
            Log.i(DEBUG_TAG,"Attempt to start while already started");
            return;
        }

        if(hasScheduledKeepAlives()) {
            stopKeepAlives();
        }

        connect("/IoTmanager");

        registerReceiver(mConnectivityReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }


    /**
     * Attempts to stop the Mqtt client
     * as well as halting all keep alive messages queued
     * in the alarm manager
     */
    private synchronized void stop() {
        if(!mStarted) {
            Log.i(DEBUG_TAG,"Attemtpign to stop connection that isn't running");
            return;
        }

        if(mClient != null) {
            mConnHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mClient.disconnect();
                    } catch(MqttException ex) {
                        ex.printStackTrace();
                    }
                    mClient = null;
                    mStarted = false;

                    stopKeepAlives();
                }
            });
        }
        else {
            Log.i(DEBUG_TAG,"mClient is null");
        }

        unregisterReceiver(mConnectivityReceiver);
    }
    /**
     * Connects to the broker with the appropriate datastore
     */
    private synchronized void subscribe(final String subscribe_text){
        if(mClient != null) {
            //Looper looper = mConnHandler.getLooper();
            //looper.getThread();
            mConnHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mClient.subscribe(subscribe_text, 0);

                    } catch (MqttException e) {
                        e.printStackTrace();
                        Log.i(DEBUG_TAG,e.getMessage().toString());
                    }
                }

            });
            Log.i(DEBUG_TAG,"подписано"+subscribe_text);
            sendBroadcastV("подписано");
        }
        else{
            Log.i(DEBUG_TAG,"нет подключения");
        }
    }


    private synchronized void  publish(final String topic, String message)
            throws MqttConnectivityException, MqttPersistenceException, MqttException {
        final byte[] byte_message = message.getBytes();
//        mClient.publish("hello",byte_message,0,false);
          // return publish_topic.publish(message2);



        if(mClient != null) {

            mConnHandler.post(new Runnable() {

                //Intent intent = new Intent(MainActivity_ext.BROADCAST_ACTION);
                @Override
                public void run() {
                    try {
                        mClient.publish(topic,byte_message,0,false);
                        //Log.i(DEBUG_TAG,"опубликовано",+topic+ Arrays.toString(byte_message));

                    } catch (MqttException e) {
                        e.printStackTrace();
                        Log.i(DEBUG_TAG,e.getMessage().toString());
                    }
                }

            });
            //sendBroadcastV("опубликовано");
            Log.i(DEBUG_TAG,"опубликовано"+topic+":"+byte_message);
            ///////////////////
            Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
            sendBroadcast(i);
            //////////////////

        }
        else{
            Log.i(DEBUG_TAG,"нет подключения");
        }

    }
    private void sendBroadcastV(String s) {
        Intent intent = new Intent();
        intent.setAction("RECIVEFILTER");
        intent.putExtra("backString", s);
        sendBroadcast(intent);
        //context.sendBroadcast(new Intent (context, MainActivity.class));
    }
    private synchronized void connect(final String subscribe_text_first_connect) {
        String url = String.format(Locale.US, MQTT_URL_FORMAT, MQTT_BROKER, MQTT_PORT);
        Log.i(DEBUG_TAG,"Connecting with URL: " + url);
        try {
            if(mDataStore != null) {
                Log.i(DEBUG_TAG,"Connecting with DataStore");
                mClient = new MqttClient(url,mDeviceId,mDataStore);
            } else {
                Log.i(DEBUG_TAG,"Connecting with MemStore");
                mClient = new MqttClient(url,mDeviceId,mMemStore);
            }
        } catch(MqttException e) {
            e.printStackTrace();
            Log.d(DEBUG_TAG,"Problem mDataStore"+e);
        }
        //mConnHandler.
        mConnHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mClient.connect(mOpts);

                    mClient.subscribe(DBHelper.FirstTimeSubscribe, 0);

                    mClient.setCallback(new MqttEventCallback(getApplicationContext()));
                    //mClient.setCallback(new MQTT_Service_depricated());
                    mStarted = true; // Service is now connected

                    Log.i(DEBUG_TAG,"Successfully connected and subscribed starting keep alives");
                    //sendBroadcastV("Successfully connected and subscribed starting keep alives");
                    //subscribe("");
                    publish_HELLO();
                    //Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "Connected!");
//                    MainActivity.mContext.sendBroadcast(i);
                    //sendBroadcast(i);

                    startKeepAlives();
                } catch(MqttException e) {
                    e.printStackTrace();
                    Log.d(DEBUG_TAG,"Problem MqttException"+e);
                } catch (MqttConnectivityException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void publish_HELLO() throws MqttException, MqttConnectivityException {
        publish("/IoTmanager","HELLO");
    }

    /**
     * Schedules keep alives via a PendingIntent
     * in the Alarm Manager
     */
    private void startKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, MQTT_Service_depricated.class);
        i.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + MQTT_KEEP_ALIVE,
                MQTT_KEEP_ALIVE, pi);
    }
    /**
     * Cancels the Pending Intent
     * in the alarm manager
     */
    private void stopKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, MQTT_Service_depricated.class);
        i.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        mAlarmManager.cancel(pi);
    }
    /**
     * Publishes a KeepALive to the topic
     * in the broker
     */
    private synchronized void keepAlive() {
        if(isConnected()) {
            try {
                sendKeepAlive();
                return;
            } catch(MqttConnectivityException ex) {
                ex.printStackTrace();
                reconnectIfNecessary();
            } catch(MqttPersistenceException ex) {
                ex.printStackTrace();
                stop();
            } catch(MqttException ex) {
                ex.printStackTrace();
                stop();
            }
        }
    }
    /**
     * Checkes the current connectivity
     * and reconnects if it is required.
     */
    public synchronized void reconnectIfNecessary() {
        if(mStarted && mClient == null) {
            connect(MqttAndroidClient.SUBSCRIBEIOT);
        }
    }
    /**
     * Query's the NetworkInfo via ConnectivityManager
     * to return the current connected state
     * @return boolean true if we are connected false otherwise
     */
    boolean isNetworkAvailable() {
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();

        return (info == null) ? false : info.isConnected();
    }
    /**
     * Verifies the client State with our local connected state
     * @return true if its a match we are connected false if we aren't connected
     */
    private boolean isConnected() {
        if(mStarted && mClient != null && !mClient.isConnected()) {
            Log.i(DEBUG_TAG,"Mismatch between what we think is connected and what is connected");
        }

        if(mClient != null) {
            return (mStarted && mClient.isConnected()) ? true : false;
        }

        return false;
    }
    /**
     * Receiver that listens for connectivity chanes
     * via ConnectivityManager
     */
    private final BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(DEBUG_TAG,"Connectivity Changed...");
        }
    };

    /**
     * Sends a Keep Alive message to the specified topic
     * //@see MQTT_KEEP_ALIVE_MESSAGE
     * //@see MQTT_KEEP_ALIVE_TOPIC_FORMAT
     * @return MqttDeliveryToken specified token you can choose to wait for completion
     */
    private synchronized MqttDeliveryToken sendKeepAlive()
            throws MqttConnectivityException, MqttPersistenceException, MqttException {
        if(!isConnected())
            throw new MqttConnectivityException();

        if(mKeepAliveTopic == null) {
            mKeepAliveTopic = mClient.getTopic(
                    String.format(Locale.US, MQTT_KEEP_ALIVE_TOPIC_FORAMT,mDeviceId));
        }

        Log.i(DEBUG_TAG,"Sending Keepalive to " + MQTT_BROKER);

        MqttMessage message = new MqttMessage(MQTT_KEEP_ALIVE_MESSAGE);
        message.setQos(MQTT_KEEP_ALIVE_QOS);

        return mKeepAliveTopic.publish(message);
    }

    private synchronized void publish_token(String message) {
        if(isConnected()) {
            try {
                publish_token_mqtt(message);
                return;
            } catch(MqttConnectivityException ex) {
                ex.printStackTrace();
                reconnectIfNecessary();
            } catch(MqttPersistenceException ex) {
                ex.printStackTrace();
                stop();
            } catch(MqttException ex) {
                ex.printStackTrace();
                stop();
            }
        }
    }
    private synchronized MqttDeliveryToken publish_token_mqtt(String message)
            throws MqttConnectivityException, MqttPersistenceException, MqttException {
        if(!isConnected())
            throw new MqttConnectivityException();


        if(mpublish_topic == null) {
            mpublish_topic = mClient.getTopic(
                    String.format(Locale.US, "topic",mDeviceId));
        }


        MqttMessage message2 = new MqttMessage(message.getBytes());
        message2.setQos(MQTT_KEEP_ALIVE_QOS);

        return mpublish_topic.publish(message2);

    }
    /**
     * Query's the AlarmManager to check if there is
     * a keep alive currently scheduled
     * @return true if there is currently one scheduled false otherwise
     */
    private synchronized boolean hasScheduledKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, MQTT_Service_depricated.class);
        i.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_NO_CREATE);

        return (pi != null) ? true : false;
    }



    /**
     * Connectivity Lost from broker
     */
/*
    @Override
    public void connectionLost(Throwable arg0) {
        stopKeepAlives();

        mClient = null;

        if(isNetworkAvailable()) {
            reconnectIfNecessary();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.i(DEBUG_TAG,"  Topic:\t" + topic +
                "  Message:\t" + new String(message.getPayload()) +
                "  QoS:\t" + message.getQos());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

*/

    /**
     * MqttConnectivityException Exception class
     */
    private class MqttConnectivityException extends Exception {
        private static final long serialVersionUID = -7385866796799469420L;
    }
}
