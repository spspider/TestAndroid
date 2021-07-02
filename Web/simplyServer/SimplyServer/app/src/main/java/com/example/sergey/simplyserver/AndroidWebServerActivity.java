package com.example.sergey.simplyserver;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Properties;

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
        setContentView(R.layout.layout_main);
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
            super(PORT, null);
        }

        @Override
        public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
            final StringBuilder buf = new StringBuilder();
            //for (Entry<Object, Object> kv : header.entrySet())
                //buf.append(kv.getKey() + " : " + kv.getValue() + "\n");
                if (parms.getProperty("username") != null) {
                    //buf.substring(0);
                    buf.append(parms.getProperty("username")+ "\n");
                    System.out.println(buf);
                    System.out.println(parms.getProperty("username"));

                    //buf.append(buf.length());
                }
            //System.out.println("FILES:"+ files );
            //System.out.println("parms:" + parms);
            //System.out.println("header:"+ header );
            //System.out.println("method:"+ method );
            //System.out.println("uri:"+ uri );
            //HTTPprocessing htttp_processing=new HTTPprocessing(parms,uri,method,header,files,IPadress,PORT);
            HTTPprocessing HTTPprocessing = new HTTPprocessing(getApplicationContext());
            String html= HTTPprocessing.HTTPprocessing(parms,uri,method,header,files,IPadress,PORT);
                    //System.out.println("!!!!!!!!!username:" + parms.getProperty("username"));

/*
            handler.post(new Runnable() {
                @Override
                public void run() {
                    hello.setText(buf);
                }
            });
            */
            addTextToTextView(buf);

            handler.post(new Runnable() {
                             @Override
                             public void run() {

                             }
                         }
            );





            return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, html);
        }

    }
}