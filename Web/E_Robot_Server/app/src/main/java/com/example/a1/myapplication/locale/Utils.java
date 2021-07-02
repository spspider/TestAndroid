package com.example.a1.myapplication.locale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Башня1 on 12.01.2019.
 */

public class Utils {
    private static final String TAG = BackgroundService_httpd.TAG;
    Context context;

    private static byte[] convert2Bytes(int hostAddress) {
        byte[] addressBytes = {(byte) (0xff & hostAddress),
                (byte) (0xff & (hostAddress >> 8)),
                (byte) (0xff & (hostAddress >> 16)),
                (byte) (0xff & (hostAddress >> 24))};
        return addressBytes;
    }

    public String getIpAddress(Context applicationContext) {

        context = applicationContext;
        //TextView textIpaddr = (TextView) findViewById(R.id.ipaddr);
        WifiManager wifiManager = (WifiManager) applicationContext.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        String IPadress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        //textIpaddr.setText("Please access! http://" + formatedIpAddress + ":" + PORT);
        IPadress = determineHostAddress();


        return IPadress;
    }

    public String getWifiApIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                Log.i(TAG, String.valueOf(en));
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("ap0")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                            .hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            Log.d(TAG, inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }

    private String determineHostAddress() {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {

                NetworkInterface ni = nis.nextElement();

                for (Enumeration<InetAddress> ips = ni.getInetAddresses(); ips.hasMoreElements(); ) {
                    InetAddress ip = ips.nextElement();
                    if (ip.getAddress().length == 4) {
                        if (!ip.getHostAddress().equals("127.0.0.1") && (ip.getHostAddress() != null)) {
                            if (ip.isSiteLocalAddress()) {
                                return  ip.getHostAddress();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }
    public List<String> determineHostAddress_List() {
        List<String> IpadressList =new ArrayList<>();

        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {

                NetworkInterface ni = nis.nextElement();

                for (Enumeration<InetAddress> ips = ni.getInetAddresses(); ips.hasMoreElements(); ) {
                    InetAddress ip = ips.nextElement();
                    if (ip.getAddress().length == 4) {
                        if (!ip.getHostAddress().equals("127.0.0.1") && (ip.getHostAddress() != null)) {
                            IpadressList.add(ip.getHostAddress());
                         }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return IpadressList;
    }





    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_WIFI = 1;
    public static final int NETWORK_STATUS_MOBILE = 2;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusString(Context context) {
        int conn = Utils.getConnectivityStatus(context);
        int status = 0;
        if (conn == Utils.TYPE_WIFI) {
            status = NETWORK_STATUS_WIFI;
        } else if (conn == Utils.TYPE_MOBILE) {
            status = NETWORK_STATUS_MOBILE;
        } else if (conn == Utils.TYPE_NOT_CONNECTED) {
            status = NETWORK_STATUS_NOT_CONNECTED;
        }
        return status;
    }
}
