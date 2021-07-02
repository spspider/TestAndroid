package com.example.sp_1.jsonparser;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static android.os.Looper.getMainLooper;

/**
 * Created by sp_1 on 06.01.2017.
 */

public class JsonParser extends AsyncTask<Void, Void, Void> {

    Context mContext;

    public JsonParser(Context ctx) {
        mContext = ctx;
    }

    public interface AsyncResponse {
        void processFinish(Void contactList);

    }

    public AsyncResponse delegate = null;

    public JsonParser(AsyncResponse delegate){
        this.delegate = delegate;
    }

    ArrayList<HashMap<String, String>> contactList;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        contactList = new ArrayList<>();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String url = "http://api.androidhive.info/contacts/";
        String jsonStr = sh.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray contacts = jsonObj.getJSONArray("contacts");

                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String id = c.optString("id");
                    String name = c.optString("name");
                    String email = c.getString("email");
                    String address = c.getString("address");
                    String gender = c.getString("gender");

                    // Phone node is JSON Object
                    JSONObject phone = c.getJSONObject("phone");
                    String mobile = phone.getString("mobile");
                    String home = phone.getString("home");
                    String office = phone.getString("office");

                    // tmp hash map for single contact
                    HashMap<String, String> contact = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contact.put("id", id);
                    contact.put("name", name);
                    contact.put("email", email);
                    contact.put("mobile", mobile);

                    // adding contact to contact list
                    contactList.add(contact);
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());


            }

        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //super.onPostExecute(result);
        delegate.processFinish(result);
            //MainActivity.getInstance().addAdapter(contactList);
        //MainActivity.addAdapter(contactList);
        super.onPostExecute(result);
        // Start Activity C
        Intent intent = new Intent(mContext, MainActivity.class);
        //intent.setAction("action!");
        mContext.startActivity(intent);
    }




}