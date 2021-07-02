package com.grisaf.mqttsample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;





import static android.content.ContentValues.TAG;
import static android.os.Looper.getMainLooper;
import static android.support.v4.app.ActivityCompat.startActivity;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static com.grisaf.mqttsample.MQTT_Service_depricated.DEBUG_TAG;
import static com.grisaf.mqttsample.R.string.topic;


/**
 * Created by sp_1 on 14.11.2016.
 */

public class MqttEventCallback implements MqttCallback {

    private Context context;
    private Handler handler;
    MainActivity Minstance = MainActivity.getInstance();

    public MqttEventCallback(Context context)
    {
        this.context = context;

    }


    @Override
    public void messageArrived(final String topic, final MqttMessage msg) throws Exception{
        Log.i(TAG, "Message arrived from topic" + topic);
        final Handler h = new Handler(getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(Minstance.getContext(), "MQTT Message:\n" + new String(topic) +"msg:"+ new String(msg.getPayload()), Toast.LENGTH_SHORT).show();
                MainActivity.getInstance().SetStatus("msg:"+msg.getPayload());
            }

        });
    }





    public void connectionLost(Throwable arg0) {

    Log.i(DEBUG_TAG,"Connection lost");
        // TODO Auto-generated method stub
        MainActivity.getInstance().SetStatus("ConLost");
        final Handler h = new Handler(getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(MainActivity.getInstance().getContext(), "reconnected", Toast.LENGTH_LONG).show();
                MainActivity.getInstance().SetStatus("reconnected");
            }

    });

        andypiper_MQTT i = new andypiper_MQTT();
        andypiper_MQTT.MqttConnector con = i.new MqttConnector();
       // con.doConnect();
        MQTT_Service_depricated MQTT_dep_inst;
        MQTT_dep_inst= MQTT_Service_depricated.getInstance();

        if(MQTT_dep_inst.isNetworkAvailable()) {
            MQTT_dep_inst.reconnectIfNecessary();
        }
    }



    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken){
    // TODO Auto-generated method stub
        final Handler h = new Handler(getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.getInstance().SetStatus("DelCompl");
                }
            });
        }

    }



