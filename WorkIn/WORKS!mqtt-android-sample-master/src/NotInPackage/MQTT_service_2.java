package NotInPackage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.grisaf.mqttsample.MainActivity;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import NotInPackage.MQTTService;

import static android.content.ContentValues.TAG;
import static android.os.Looper.getMainLooper;
import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by sp_1 on 08.11.2016.
 */

public class MQTT_service_2 {
    private void doConnect(){
        Log.d(TAG, "doConnect()");
        IMqttToken token;
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        try {
            IMqttAsyncClient mqttClient = new MqttAsyncClient("tcp://mqtt.example.com:1883", "id", new MemoryPersistence());
            token = mqttClient.connect();
            token.waitForCompletion(3500);
            mqttClient.setCallback(new MqttEventCallback());
            token = mqttClient.subscribe("testtopic", 0);
            token.waitForCompletion(5000);
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            switch (e.getReasonCode()) {
                case MqttException.REASON_CODE_BROKER_UNAVAILABLE:
                case MqttException.REASON_CODE_CLIENT_TIMEOUT:
                case MqttException.REASON_CODE_CONNECTION_LOST:
                case MqttException.REASON_CODE_SERVER_CONNECT_ERROR:
                    Log.v(TAG, "c" +e.getMessage());
                    e.printStackTrace();
                    break;
                case MqttException.REASON_CODE_FAILED_AUTHENTICATION:
                    Intent i = new Intent("RAISEALLARM");
                    i.putExtra("ALLARM", e);
                    Log.e(TAG, "b"+ e.getMessage());
                    break;
                default:
                    Log.e(TAG, "a" + e.getMessage());
            }
        }
    }
    private class MqttEventCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable arg0) {


        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        @SuppressLint("NewApi")
        public void messageArrived(String topic, final MqttMessage msg) throws Exception {
            Log.i(TAG, "Message arrived from topic" + topic);
            Handler h = new Handler(getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Intent launchA = new Intent(MQTT_service_2.this, MainActivity.class);
                    launchA.putExtra("message", msg.getPayload());
                    //TODO write somethinkg that has some sense
                    if(Build.VERSION.SDK_INT >= 11){
                        launchA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    } /*else {
        		        launchA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		    }*/
                    startActivity(launchA);
                    Toast.makeText(MainActivity.getInstance().getContext(), "MQTT Message:\n" + new String(msg.getPayload()), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public String getThread(){
        return Long.valueOf(thread.getId()).toString();
    }

    //@Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind called");
        return null;
    }
}
