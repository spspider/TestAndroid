package com.example.sergey.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by sergey on 15.12.2017.
 */

public class UDPListenerService extends Service {
    static String UDP_BROADCAST = "UDPBroadcast";
    private AsyncTask<Void, Void, Void> async_udp;
    //Boolean shouldListenForUDPBroadcast = false;
    DatagramSocket socket;
    InetAddress server_ip;
    int server_port = 10000;

    private void listenAndWaitAndThrowIntent(InetAddress broadcastIP, Integer port) throws Exception {
        byte[] recvBuf = new byte[15000];
        if (socket == null || socket.isClosed()) {
            socket = new DatagramSocket(port);
            socket.setBroadcast(true);
        }
/*
        String str2 = "TEST MESSAGE !!!";
        byte b1[];
        b1 = new byte[100];
        b1 = str2.getBytes();
        DatagramPacket p0 = new DatagramPacket(b1, b1.length, InetAddress.getByName("192.168.1.255"), port);
        socket.send(p0);
*/
        //socket.setSoTimeout(1000);
        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);

        //socket.send(packet);
        Log.e("UDP", "Waiting for UDP broadcast");
        socket.receive(packet);

        String senderIP = packet.getAddress().getHostAddress();
        String message = new String(packet.getData()).trim();

        Log.e("UDP", "Got UDB broadcast from " + senderIP + ", message: " + message);

        broadcastIntent(senderIP, message);
        socket.close();
    }
    private void send(InetAddress broadcastIP, Integer port) throws Exception {
        byte[] recvBuf = new byte[15000];
        if (socket == null || socket.isClosed()) {
            socket = new DatagramSocket(port);
            socket.setBroadcast(true);
        }

        String str2 = "TEST MESSAGE !!!";
        byte b1[];
        b1 = new byte[100];
        b1 = str2.getBytes();
        DatagramPacket p0 = new DatagramPacket(b1, b1.length, InetAddress.getByName("192.168.1.108"), port);
        socket.send(p0);


        //socket.close();
    }
    private void broadcastIntent(String senderIP, String message) {
        Intent intent = new Intent(UDPListenerService.UDP_BROADCAST);
        intent.putExtra("sender", senderIP);
        intent.putExtra("message", message);
        sendBroadcast(intent);
    }

    Thread UDPBroadcastThread;

    void startListenForUDPBroadcast() {
        UDPBroadcastThread = new Thread(new Runnable() {
            public void run() {
                try {
                    InetAddress broadcastIP = InetAddress.getByName("192.168.1.108"); //172.16.238.42 //192.168.1.255
                    Integer port = 10000;
                    send(broadcastIP, port);
                    while (shouldRestartSocketListen) {
                        listenAndWaitAndThrowIntent(broadcastIP, port);
                    }
                    //if (!shouldListenForUDPBroadcast) throw new ThreadDeath();
                } catch (Exception e) {
                    Log.i("UDP", "no longer listening for UDP broadcasts cause of error " + e.getMessage());
                }
            }
        });
        UDPBroadcastThread.start();
    }

    private Boolean shouldRestartSocketListen = true;

    void stopListen() {
        shouldRestartSocketListen = false;
        socket.close();
    }

    @Override
    public void onCreate() {

    }

    ;

    @Override
    public void onDestroy() {
        stopListen();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        shouldRestartSocketListen = true;
        startListenForUDPBroadcast();
        Log.i("UDP", "Service started");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void runUdpServer() {
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

                    DatagramPacket p1 = new DatagramPacket(b1, b1.length);
                    s.receive(p1);

                    s.close();
                    b1 = p1.getData();
                    String str = new String(b1);

                    server_port = p1.getPort();
                    server_ip = p1.getAddress();
                    Log.i("UDP", str.substring(1));


                    String str_msg = "RECEIVED FROM CLIENT IP =" + server_ip + " port=" + server_port + " message no = " + b1[0] +
                            " data=" + str.substring(1);  //first character is message number
                    //WARNING: b1 bytes display as signed but are sent as signed characters!

                } catch (SocketException e) {
                    Log.i("UDP", e.getLocalizedMessage());
                    //status.append("Error creating socket");
//                    statusText.concat(" Error creating socket");   //this doesnt work!
                } catch (IOException e) {
                    Log.i("UDP", e.getLocalizedMessage());
                    //status.append("Error recieving packet");
//                    statusText.concat(" Error recieving packet");  //this doesnt work!
                }


                return null;
            }
        };

        if (Build.VERSION.SDK_INT >= 11) {
            async_udp.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            async_udp.execute();
        }

    }
}