package com.example.sp_1.jsonparser;

import android.content.Intent;

import com.example.sp_1.jsonparser.CreateListItem;
import com.example.sp_1.jsonparser.GlobalClass;
import com.example.sp_1.jsonparser.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sp_1 on 09.01.2017.
 */

public class JsonParcer_MQTT_subscribe{
    private MainActivity activity;

    //ArrayList<HashMap<String, String>> values;
   //private final Context context;
    private static JsonParcer_MQTT_subscribe instance;
    ArrayList<HashMap<String, String>> contactList;
    private MainActivity getMainactivityContext;

/*

    public static JsonParcer_MQTT_subscribe getInstance(Context context) {
        if (instance == null) {
            instance = new JsonParcer_MQTT_subscribe(context);
        }
        return instance;
    }
        */

    public ArrayList<HashMap<String, String>> insertMessage(String messagePayLoad, String receivedMesageTopic) {
        //messagePayLoad = "{\"id\":\"2\",\"page\":\"Kitchen\",\"descr\":\"Dimmer\",\"widget\":\"range\",\"topic\":\"/IoTmanager/dev01-kitchen/dim-light\",\"style\":\"range-calm\",\"badge\":\"badge-assertive\",\"leftIcon\":\"ion-ios-rainy-outline\",\"rightIcon\":\"ion-ios-rainy\"}";
//messagePayLoad = MainActivity.JsonString;


        contactList = new ArrayList<>();
        String Allvalues = null;
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(messagePayLoad);
            //jArray = new JSON
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //try {


            // ill just add the subscription if the serviceId matches the list of services
            // im subscribed to
            //jObject.getJSONArray()
            // real inserting

            //values = new ArrayList<>();
            HashMap<String, String> values = new HashMap<>();
            //values = new ArrayList<>();
            //ContentValues values = new ContentValues();
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
            //MainActivity.myLog(String.valueOf(values));
            sendBroadcastV(String.valueOf(values));

            Allvalues = String.valueOf(values);
            contactList.add(values);
            sendBroadcastV(String.valueOf(contactList));
            //context.sendBroadcast(new Intent("test"),"123");
            //Uri instertedUri = getContentResolver().insert(NotificationContentProvider.CONTENT_URI, values);
            // TODO: handle failure on content provider and on main code
            //value = jObject.getString("value");


        //}
        /*
        catch (final JSONException e) {
            String TAG = "my_log";
            Log.d(TAG, "Failure parsing json message + " + messagePayLoad);
            e.printStackTrace();
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            MainActivity.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });



    }
    */

//        CreateListItem LI = new CreateListItem((MainActivity) MainActivity.activity);
        CreateListItem LI = new CreateListItem();
        //LI.createList2(contactList);
        //LI.createList2(contactList);
        //LI.createList(contactList);
        LI.printAllHashMap(contactList);
        return contactList;

    }

    private void sendBroadcastV(String s) {
        getMainactivityContext = (MainActivity) GlobalClass.getInstance().getContext();
        Intent intent = new Intent();
        intent.setAction("RECIVEFILTER");
        intent.putExtra("backString", s);
        getMainactivityContext.sendBroadcast(intent);
        //CreateListItem LI = new CreateListItem();
        //LI.makeList(s);
        //context.sendBroadcast(new Intent (context, MainActivity.class));
    }


}

