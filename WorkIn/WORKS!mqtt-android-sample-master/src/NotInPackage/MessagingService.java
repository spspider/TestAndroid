package NotInPackage;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.Connection;

/**
 * Created by sp_1 on 20.11.2016.
 */

public class MessagingService extends Service {
    private static final String TAG = "MessagingService";
    private MqttAndroidClient mqttClient;
    String deviceId;



    @Override
    public void onCreate() {
    }
    private void setClientID() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        deviceId = wInfo.getMacAddress();
        if (deviceId == null) {
            deviceId = MqttAsyncClient.generateClientId();
        }
    }

    public class MsgBinder extends Binder {
        public MsgServ getService() {
            return MsgServ.this;
        }
    }

    public void doConnect(){
        // Using some of the Paho sample app classes
        String server = ConfigClass.BROKER_URI;
        MemoryPersistence mem = new MemoryPersistence();
        mqttClient = new MqttAndroidClient(this,ConfigClass.BROKER_URI,deviceId,mem);
        MqttConnectOptions conOpt = new MqttConnectOptions();
        String clientHandle = server + deviceId;
        Connection con = new Connection(clientHandle, deviceId, ConfigClass.BROKER_ADDRESS,
                ConfigClass.BROKER_PORT, this, mqttClient, false);
        conOpt.setCleanSession(false);
        conOpt.setConnectionTimeout(ConfigClass.CONN_TIMEOUT);
        conOpt.setKeepAliveInterval(ConfigClass.CONN_KEEPALIVE);
        conOpt.setUserName("testclient");
        conOpt.setPassword("password".toCharArray());
        String[] actionArgs = new String[1];
        actionArgs[0] = deviceId;
        final ActionListener callback =
                new ActionListener(this, ActionListener.Action.CONNECT, clientHandle,
                        actionArgs);
        mqttClient.setCallback(new MqttCallbackHandler(this, clientHandle));
        mqttClient.setTraceCallback(new MqttTraceCallback());
        con.addConnectionOptions(conOpt);
        Connections.getInstance(this).addConnection(con);
        try {
            mqttClient.connect(conOpt, null, callback);
            Log.d("Con", "Connected");
        } catch (MqttException e) {
            Log.d("Con", "Connection failed");
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doConnect();
        return START_STICKY;
    }

}