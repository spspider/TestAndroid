package com.example.a1.myapplication;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import fi.iki.elonen.NanoHTTPD;

import static java.net.HttpURLConnection.HTTP_OK;

public class AndroidWebServerActivity extends Activity {
    private static final int PORT = 8765;
    private static String IPadress = "";
    private TextView hello;
    private ScrollView chat_ScrollView;
    private MyHTTPD server;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_server);
        hello = (TextView) findViewById(R.id.hello);
        this.chat_ScrollView = (ScrollView) this.findViewById(R.id.chat_ScrollView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView textIpaddr = (TextView) findViewById(R.id.ipaddr);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        IPadress = formatedIpAddress;
        textIpaddr.setText("Please access! http://" + formatedIpAddress + ":" + PORT);

        try {
            if (server==null)
            server = new MyHTTPD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // if (server != null)
        //  server.stop();
    }

    public void addTextToTextView(final StringBuilder strTemp) {
        //String strTemp = "TestlineOne\nTestlineTwo\n";
        //append the new text to the bottom of the TextView

        //scroll chat all the way to the bottom of the text
        //HOWEVER, this won't scroll all the way down !!!
        //chat_ScrollView.fullScroll(View.FOCUS_DOWN);
        //INSTEAD, scroll all the way down with:
        hello.post(new Runnable() {
            public void run() {
                hello.append(strTemp);
                chat_ScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private class MyHTTPD extends NanoHTTPD {
        public MyHTTPD() throws IOException {
            super(null, PORT);
        }

        @Override
        public Response serve(IHTTPSession session) {
            String msg = "<html><body><h1>Hello server</h1>\n";
            Map<String, String> parms = session.getParms();
            if (parms.get("username") == null) {
                msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
            } else {
                msg += "<p>Hello, "+ IPadress + ":" + PORT + parms.get("username") + "!</p>";
            }
            return newFixedLengthResponse(msg + "</body></html>\n");
            //return super.serve(session);
        }
    }

}