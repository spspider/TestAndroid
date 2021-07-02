package com.example.sp_1.jsonparser;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;

import static android.content.ContentValues.TAG;
import static android.os.Looper.getMainLooper;
import static com.example.sp_1.jsonparser.MQTT_Service_depricated.DEBUG_TAG;
import static com.example.sp_1.jsonparser.MainActivity.mContext;

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

                Toast.makeText(MainActivity.getInstance().getContext(), "MQTT Message:\n" + new String(topic) +"msg:"+ new String(msg.getPayload()), Toast.LENGTH_SHORT).show();
                JsonParcer_MQTT_subscribe parcer = new JsonParcer_MQTT_subscribe();

                    parcer.insertMessage(new String(msg.getPayload()),"topic");

            }

        });
        Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
        context.sendBroadcast(i);
    }



    public void connectionLost(Throwable arg0) {

    Log.i(DEBUG_TAG,"Connection lost");
        // TODO Auto-generated method stub

        final Handler h = new Handler(getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(mContext, "reconnected", Toast.LENGTH_LONG).show();

            }

    });


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
                    //MainActivity.getInstance().SetStatus("DelCompl");
                }
            });
        Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
        context.sendBroadcast(i);
        }

    }



