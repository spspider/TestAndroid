package com.example.sp_1.iotmymanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.sp_1.iotmymanager.Activitys.BaseActivity;
import com.example.sp_1.iotmymanager.Activitys.MainDevicesPager;
import com.example.sp_1.iotmymanager.NotInPackage.MainActivity;
import com.example.sp_1.iotmymanager.Service.MQTT_Service_depricated;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sp_1 on 09.01.2017.
 */

public class JsonParcer_MQTT_subscribe{
    ArrayList<HashMap<String, String>> contactList;
    public static ArrayList<HashMap<String,String>> AllOneDvice = new ArrayList<>();
    public static HashMap<String,HashMap<String,String>> AllOneDviceStatus = new HashMap<>();
    ArrayList<HashMap<String, String>> statusList;

    public void insertMessage(String messagePayLoad, String receivedMesageTopic,Context context) {
    //нужно подписываться на каждый топик, который пришел в ответе вместе с сообщениями
        if (receivedMesageTopic.equals(DBHelper.FirstTimeSubscribe)){
            //пришел ответ с топиком /IotTmanager здесь нужно записать в subscribe то, что в ответе, в формате /IoTmanager/dev01-kitchen/config
            //parceButtons(jObject);
            String toSubscribe = DBHelper.FirstTimeSubscribe+"/"+messagePayLoad+"/config";
            subscribeToCurrentDevice(toSubscribe,context);
        };

        //contactList = new ArrayList<>();
        //statusList = new ArrayList<>();
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(messagePayLoad);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jObject!=null) {
            if (!jObject.optString("status").isEmpty()) {
                //parceStatus(jObject);
                parceStatus(receivedMesageTopic,jObject);
            } else if (!jObject.optString("topic").isEmpty()) {



                parceButtons(jObject);
                subscribeToCurrentDevice(jObject.optString("topic")+"/status",context);
            }

        }
            ////////////////



    }

    private void subscribeToCurrentDevicePin(String topic, String messagePayLoad, Context context) {
        Intent intentSend= new Intent(MQTT_Service_depricated.RECIEVE_NEW_CONNECTION_OPTION);
        Bundle bundle = new Bundle();
        // /IoTmanager/dev01-kitchen/config
        // {"id":"0","page":"Kitchen","descr":"Light-0","widget":"toggle","topic":"/IoTmanager/dev01-kitchen/light0","color":"blue"}
        //String toSubscribe=

        //bundle.putString(MqttAndroidClient.IOTMANAGERdeviceCONFIG,toSubscribe);

        intentSend.putExtras(bundle);
        context.sendBroadcast(intentSend);

    }


    private void subscribeToCurrentDevice(String toSubscribe,Context context) {
        //подписаться на данное устройство
        /*
        Intent intentStart;
        intentStart = new Intent(activity, MQTT_Service_depricated.class).setAction(MainActivity.TAG+".START");
        intentStart.putExtras(bundle);
        // стартуем сервис
        activity.startService(intentStart);
*/

        Intent intentSend= new Intent(MQTT_Service_depricated.RECIEVE_NEW_CONNECTION_OPTION);
        Bundle bundle = new Bundle();
        bundle.putString(MqttAndroidClient.IOTMANAGERdeviceCONFIG,toSubscribe);

        intentSend.putExtras(bundle);
        context.sendBroadcast(intentSend);

    }

    private void parceButtons(JSONObject jObject) {
        HashMap<String, String> values = new HashMap<>();
        values.put("id", jObject.optString("id"));
        values.put("page", jObject.optString("page"));
        values.put("descr", jObject.optString("descr"));
        values.put("widget", jObject.optString("widget"));
        values.put("topic", jObject.optString("topic"));
        values.put("color",jObject.optString("color"));
        values.put("style", jObject.optString("style"));
        values.put("badge", jObject.optString("badge"));
        values.put("leftIcon", jObject.optString("leftIcon"));
        values.put("rightIcon", jObject.optString("rightIcon"));

        sendBroadcastV(String.valueOf(values));
        AllOneDvice.add(values);

        //contactList.add(values);
        //sendBroadcastV(String.valueOf(contactList));
        //Log.d(BaseActivity.TAG, String.valueOf(values));
        //AllOneDvice.add(jObject.optString("id"),values);
        // TODO: handle failure on content provider and on main code

        CreateListItem LI = new CreateListItem();
        //LI.printAllHashMap(contactList);
        LI.printAllHashMapMap(values);
        //LI.saveAllHashMap(values);
    }
    private void parceStatus(String receivedMesageTopic,JSONObject jObject){
        HashMap<String, String> values = new HashMap<>();
        values.put("status", jObject.optString("status"));
        if (jObject.optString("sTopic").equals("")){
            values.put("sTopic", receivedMesageTopic);
        }else{
            values.put("sTopic", jObject.optString("sTopic"));
        }
        //statusList.add(values);
        AllOneDviceStatus.put(values.get("sTopic"),values);
        CreateListItem LI = new CreateListItem();
        //Log.d(MainActivity.TAG, String.valueOf(statusList));
        //LI.setStatusWS(statusList);
        LI.setStatusWSMap(values);
    }
    private void parceStatus_mqtt(JSONObject jObject) {

    }


    private void sendBroadcastV(String s) {
        //getMainactivityContext = (MainActivity) GlobalClass.getInstance().getContext();
        Intent intent = new Intent();
        intent.setAction("RECIVEFILTER");
        intent.putExtra("backString", s);

        //getMainactivityContext.sendBroadcast(intent);
        //CreateListItem LI = new CreateListItem();
        //LI.makeList(s);
        //context.sendBroadcast(new Intent (context, MainActivity.class));
    }



}

