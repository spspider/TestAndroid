package com.grisaf.mqttsample;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by sp_1 on 07.11.2016.
 */

public class MQ_read   {

    private Context context;
    /** Client handle to reference the connection that this handler is attached to**/
    private String clientHandle;

    public MQ_read(Context context, String clientHandle) {

        this.context = context;
        this.clientHandle = clientHandle;
    }


}
