package com.example.sergey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by sergey on 14.12.2017.
 */

public class MainActivity2 extends Activity {
    Button button;
    //TextView txt1, txtH, txtE, txtER, txtUpdate;
    String msg;
    CharSequence oldMsg = "a";
    Integer updateCount = 0;
    Activity mActivity;
    DatagramSocket socket;
    boolean msgSent = false;
    boolean errorSend = false;
    boolean errorReceive = false;
    private AsyncTask<Void, Void, Void> async_udp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = (Button) findViewById(R.id.button_begin_graph);
        Intent service = new Intent(this,UDPListenerService.class);
        startService(new Intent(this,UDPListenerService.class));
        //When I click this, I send a message
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UDP", "button");

                //stopReceiveUdp();
                Thread sendThread = new Thread(){
                    public void run(){
                        try{
                            if (socket == null){
                                socket = new DatagramSocket(10000);
                                socket.setBroadcast(true);
                            }

                            byte[] data ="Some MSG".getBytes();
                            DatagramPacket p0 = new DatagramPacket(data, data.length, InetAddress.getByName("192.168.1.108"), 10000);

                            socket.send(p0);
                            msgSent=true;
                            Log.i("UDP", "sended");

                        } catch (Exception e){
                            boolean errorSend = true;
                            Log.i("UDP",e.getLocalizedMessage());

                        }
                    }
                };
                sendThread.start();
                try {
                    sendThread.join();
                } catch (InterruptedException e) {
                    Log.i("UDP", e.getLocalizedMessage());
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (msgSent){
                    //txtH.setText("SENT!");
                }
                //startReceiveUdp();
            }
        });



        //startReceiveUdp();
    }

    public void sendMessage(View view)  //function bound to message button
    {
        //runUdpServer();


    }

    public void refreshStatus(View view) {
        //status.setText(statusText);
    }


    ReceiveSocket receiveSocket;

    void startReceiveUdp() {
        if (receiveSocket == null) {
            receiveSocket = new ReceiveSocket();
            receiveSocket.execute("");
        }
    }

    void stopReceiveUdp() {
        if (receiveSocket != null) receiveSocket.cancel(true);
    }

    private class ReceiveSocket extends AsyncTask<String, String, String> {
        DatagramSocket clientsocket;

        @Override
        protected String doInBackground(String... params) {
            while (true) {
                try {
                    publishProgress(receiveMessage());
                    if (isCancelled()) break;
                } catch (Exception e) {
                    //
                }
            }
            return "";
        }

        String[] receiveMessage() {
            String[] rec_arr = null;
            try {
                int port = 10000;
                if (clientsocket == null) clientsocket = new DatagramSocket(port);

                byte[] receivedata = new byte[30];

                String str2 = "TEST MESSAGE !!!";
                byte b1[];
                b1 = new byte[100];
                b1 = str2.getBytes();
                //byte b1[]=new byte[1];

                DatagramPacket p0 = new DatagramPacket(b1, b1.length, InetAddress.getByName("192.168.1.108"), port);
                clientsocket.send(p0);

                DatagramPacket recv_packet = new DatagramPacket(receivedata, receivedata.length);
                Log.d("UDP", "S: Receiving...");
                clientsocket.receive(recv_packet);

                String rec_str = new String(recv_packet.getData()); //stringa con mesasggio ricevuto
                rec_str = rec_str.replace(Character.toString((char) 0), "");
                Log.d("UDP", rec_str);
                //InetAddress ipaddress = recv_packet.getAddress();
                //int port = recv_packet.getPort();
                //Log.d("IPAddress : ",ipaddress.toString());
                //Log.d(" Port : ",Integer.toString(port));

                rec_arr = rec_str.split("\\|");
                return rec_arr;
            } catch (Exception e) {
                Log.e("UDP", "S: Error", e);
            }
            return rec_arr;
        }

        @Override
        protected void onPostExecute(String result) {
            //
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... rec_arr) {
            if (rec_arr.length > 1) {
                String clientType = rec_arr[0];
                String command = rec_arr[1];

                if (command.contentEquals("go")) {
                    //press button go
                    //startAction(null);
                }


            }


        }


    }

}