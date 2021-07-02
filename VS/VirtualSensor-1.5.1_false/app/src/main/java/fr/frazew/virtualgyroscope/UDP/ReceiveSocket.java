package fr.frazew.virtualgyroscope.UDP;

import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;

import fr.frazew.virtualgyroscope.hooks.SensorChange;
import fr.frazew.virtualgyroscope.hooks.sensorchange.API18;

import static fr.frazew.virtualgyroscope.UDP.UdpClientHandler.returnFloat;

/**
 * Created by sergey on 14.12.2017.
 */

public class ReceiveSocket extends AsyncTask<String, String, String> {

    static DatagramSocket clientsocket;
    private String address;
    private int port;
    UdpClientHandler handler;
    API18 api18;
    public static float[] values = {0.3F, 0.3F, 0.3F};

    public ReceiveSocket(String s, int i, UdpClientHandler handler) {
        this.address = s;
        this.port = i;
        this.handler = handler;

    }


    public ReceiveSocket() {
        this.address = "192.168.1.108";
        this.port = 10000;

    }

    private void sendState(String state) {
        handler.sendMessage(
                Message.obtain(handler,
                        UdpClientHandler.UPDATE_STATE, state));
    }

    @Override
    protected String doInBackground(String... params) {
        while (!Thread.currentThread().isInterrupted()) {
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
        String[] rec_arr = new String[1];
        try {
            //int port = 10000;
            if (clientsocket == null) {
                if (available(port)) {
                    clientsocket = new DatagramSocket(port);
                }
            } else {
                //clientsocket.setReuseAddress(true);
                return new String[1];
            }
            if (clientsocket!=null) {
                //clientsocket.setReuseAddress(true);
//            clientsocket.bind(new InetSocketAddress(port));
                byte[] receivedata = new byte[30];

                byte b1[] = new byte[0];
                DatagramPacket p0 = new DatagramPacket(b1, b1.length, InetAddress.getByName(address), port);
                clientsocket.send(p0);

                DatagramPacket recv_packet = new DatagramPacket(receivedata, receivedata.length);
                Log.d("UDP", "S: Receiving...");
                clientsocket.receive(recv_packet);


                String rec_str = new String(recv_packet.getData()); //stringa con mesasggio ricevuto
                rec_str = rec_str.replace(Character.toString((char) 0), "");
                //API18.setValue(updateRxMsg(rec_str));
                //API18.values=updateRxMsg(rec_str);
                this.values = updateRxMsg(rec_str);
                //handler.sendMessage(
                //       Message.obtain(handler, UdpClientHandler.UPDATE_MSG, rec_str));
                //handler.sendMessage(
                //   Message.obtain(handler, MainActivity.UdpClientHandler.UPDATE_MSG, rec_str));
                Log.d("UDP", rec_str);
                //InetAddress ipaddress = recv_packet.getAddress();
                //int port = recv_packet.getPort();
                //Log.d("IPAddress : ",ipaddress.toString());
                //Log.d(" Port : ",Integer.toString(port));

                rec_arr = rec_str.split("\\|");
            }
            return rec_arr;
        } catch (Exception e) {
            Log.e("UDP", "S: Error", e);
        }
        return rec_arr;
    }
    public static boolean available(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                /* should not be thrown */
                }
            }
        }

        return false;
    }
    private float[] updateRxMsg(String rxmsg) {
        String jsonStr = rxmsg;
        float[] values = new float[3];
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                values[0] = (float) (jsonObj.getLong("X")) / 10000;
                values[1] = (float) (jsonObj.getLong("Y")) / 10000;
                values[2] = (float) (jsonObj.getLong("Z")) / 10000;

            } catch (final JSONException e) {

            }
        }
        SensorChange.new_values = returnFloat;
        //Singleton.getInstance().setValues(returnFloat);
        return values;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("UDP", result);
        //
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(String... rec_arr) {
if (rec_arr!=null){
        if (rec_arr.length > 1) {
            String clientType = rec_arr[0];
            String command = rec_arr[1];
            Log.e("UDP", rec_arr[0] + " " + rec_arr[1]);
            if (command.contentEquals("go")) {
                //press button go
                //startAction(null);
            }


        }


    }
    }
}

