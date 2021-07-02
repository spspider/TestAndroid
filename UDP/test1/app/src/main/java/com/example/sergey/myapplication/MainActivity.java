package com.example.sergey.myapplication;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity {

    WifiManager w;
    TextView status;
    InetAddress server_ip;
    int server_port = 10000;

    String statusText;

    private AsyncTask<Void, Void, Void> async_udp;
    private boolean Server_Active = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        status = (TextView) findViewById(R.id.status);

        w = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        if (!w.isWifiEnabled()) {
            status.setText("switching ON wifi ");
            w.setWifiEnabled(true);
        } else {
            status.setText("Its already ON ");

        }

    }

    public void sendMessage(View view)  //function bound to message button
    {
        runUdpServer();


    }

    public void refreshStatus(View view)
    {
        status.setText(statusText);
    }

    public void runUdpServer()
    {

        int x;
        WifiInfo info = w.getConnectionInfo();
        status.setText(" ");
        status.append("\n\nWiFi Status: " + info.toString());

        x = info.getIpAddress();
        String str1 = info.getMacAddress();

        status.append("\n\nmac address===" + str1 + "  ,ip===" + x);

        try {
            server_ip = InetAddress.getByName("192.168.1.108"); // ip of THE OTHER DEVICE - NOT THE PHONE

        } catch (UnknownHostException e) {
            status.append("Error at fetching inetAddress");
        }

        async_udp = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                String str2 = "TEST MESSAGE !!!";
                byte b1[];
                b1 = new byte[100];
                b1 = str2.getBytes();
                //DatagramPacket p1 = new DatagramPacket(b1, b1.length, server_ip, server_port);


                try {
                    //DatagramSocket s = new DatagramSocket(server_port, server_ip);
                    DatagramSocket s = new DatagramSocket(server_port);
                    s.connect(server_ip, server_port);

                    DatagramPacket p0 = new DatagramPacket(b1, b1.length, InetAddress.getByName("192.168.1.108"), server_port);
                    s.send(p0);
                    //The above two line can be used to send a packet - the other code is only to recieve

                    DatagramPacket p1 = new DatagramPacket(b1,b1.length);
                    s.receive(p1);

                    s.close();
                    b1=p1.getData();
                    String str = new String( b1);

                    server_port = p1.getPort();
                    server_ip=p1.getAddress();
                    Log.i("UDP",str.substring(1));


                    String str_msg = "RECEIVED FROM CLIENT IP =" + server_ip + " port=" + server_port + " message no = " + b1[0] +
                            " data=" + str.substring(1);  //first character is message number
                    //WARNING: b1 bytes display as signed but are sent as signed characters!

                    //status.setText(str_msg);
                    statusText = str_msg;

                } catch (SocketException e)
                {
                    Log.i("UDP",e.getLocalizedMessage());
                    //status.append("Error creating socket");
//                    statusText.concat(" Error creating socket");   //this doesnt work!
                } catch (IOException e)
                {
                    Log.i("UDP",e.getLocalizedMessage());
                    //status.append("Error recieving packet");
//                    statusText.concat(" Error recieving packet");  //this doesnt work!
                }


                return null;
            }
        };

        if (Build.VERSION.SDK_INT >= 11)
        {
            async_udp.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            async_udp.execute();
        }
        status.setText(statusText); //need to set out here, as above is in an async thread

    }
}