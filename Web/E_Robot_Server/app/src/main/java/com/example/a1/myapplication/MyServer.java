package com.example.a1.myapplication;


import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.a1.myapplication.locale.BackgroundService_httpd;
import com.example.a1.myapplication.locale.EditActivity;
import com.example.a1.myapplication.locale.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.iki.elonen.NanoHTTPD;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Башня1 on 19.12.2018.
 */

public class MyServer extends NanoHTTPD {
    public final static int PORT = 8080;
    private static Set<String> HTTPS;
    Context applicationContext;
    String TAG = BackgroundService_httpd.TAG;
    Context context;
    Utils utils;
    public MyServer(Context applicationContext, int isStartEdit) throws IOException {
        super(PORT);
        context=applicationContext;

        HTTPS = new HashSet<String>();
        utils=new Utils();
        this.applicationContext = applicationContext;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        String IpAdress = utils.getIpAddress(applicationContext);
        Log.v(TAG, "сервер запущен" + IpAdress);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

/*
        @Override
        public Response serve(IHTTPSession session) {
            //Context applicationContext = BackgroundService_httpd.getContext();
            String request="";

            try {
                Map<String, List<String>> parms = session.getParameters();
                List<String> requestList = parms.get("username");
                Log.i(TAG, String.valueOf(parms));
                //request = requestList.get(0);
                if (applicationContext != null) {
                    //applicationContext.sendBroadcast(BackgroundService_httpd.REQUEST_REQUERY);
                    Log.i(TAG, "контекст есть броадкаст отправлен");
                    if (!request.equals("")) {
                        Log.i(TAG, request);
                        //HTTPS.add(request);
                    }
                } else {
                    Log.i(TAG, "нет контекста");
                }


            }
            catch (Exception e){
                Log.e(TAG, String.valueOf(e));
            }
            String html=    "<html>\n" +
                    "<body>\n" +
                    "<form action='?' enctype=\"multipart/form-data\" method=\"POST\">\n" +
                    "    <input type=\"text\" id=\"input\" name=\"username\" value='" + request +
                    "' />\n" +
                    "    <input type=\"submit\" />\n" +
                    "</form>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";
            String msg = "<html><body><h1>Hello server</h1>\n";
            msg += "<p>We serve " + session.getUri() + " !</p>";
            return newFixedLengthResponse(html);
        }
        */

    public static Set<String> getHTTPS() {
        return HTTPS;
    }

    public static void clearHTTPS() {
        HTTPS.clear();
    }


    @Override
    public Response serve(IHTTPSession session) {
       // String msg = "<html><body><h1>Hello server</h1>\n";

        String request = null;
        String uri = session.getUri();
        String getQueryParam=session.getQueryParameterString();
        if (!uri.contains("/favicon.ico")) {
            request = uri;

            if(getQueryParam!=null){
            request += "?" + session.getQueryParameterString();};
        }

        //Log.i(TAG,"!!!!!!!!URI:"+session.getUri());
        //Log.i(TAG,"!!!!!!!!QueryParameter:"+session.getQueryParameterString());
        if(request!=null){
            if (!HTTPS.contains(request))
            HTTPS.add(request);
        }
        Log.i(TAG, "HTTPS:" + HTTPS);
        //Log.i(TAG, "session.getQueryParameterString():" + session.getQueryParameterString());

        sendBroadcast_toEditText(HTTPS,request);
        //Log.i(TAG, "session.getQueryParameterString():" + session.parseBody(););
        String html=    "<html>\n" +
                "<body> <h1>Navigate your device here, to make request</h1>\n" +
/*                "<form action='?' enctype=\"multipart/form-data\" method=\"GET\">\n" +
                "    <input type=\"text\" id=\"input\" name=\"username\" value='' />\n" +
                "    <input type=\"submit\" />\n" +
                "</form>\n" +*/
                "<br>from IP:<br>\n"+
                session.getRemoteIpAddress()+
                "<br>request:<br>\n"+
                request+
                "\n" +
                "</body>\n" +
                "</html>";


            applicationContext.sendBroadcast(BackgroundService_httpd.REQUEST_REQUERY);

        return newFixedLengthResponse(html + "</body></html>\n");
    }

    private void sendBroadcast_toEditText(Set<String> HTTPS, String request) {
        Intent intent_Edit = new Intent(EditActivity.ACTION_FILTER);
        //Http_list=HTTPs;
        ArrayList<String> Http_list = new ArrayList<String>();
        //HTTPS.remove("/");
        //HTTPS.remove("/favicon.ico");
        Http_list.addAll(HTTPS);
        intent_Edit.putExtra(EditActivity.HTTPS_ACTIVITY_RESULT, Http_list);

        intent_Edit.putExtra(EditActivity.IP_ADRESS, utils.getIpAddress(context));
        context.sendBroadcast(intent_Edit);
    }



}
