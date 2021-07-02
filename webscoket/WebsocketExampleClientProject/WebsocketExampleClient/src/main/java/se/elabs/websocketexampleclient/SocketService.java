package se.elabs.websocketexampleclient;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by sp_1 on 24.01.2017.
 */

public class SocketService extends Service {
    boolean status=false;
    public static final String BROADCAST_ACTION = "ACTIONSERVICEBACK";
    public static final String BROADCAST_SERVICE_MSG = "BROADCAST_SERVICE_MSG";
    BroadcastReceiver broadcaster;
    BroadcastReceiver broadreciever;
    Intent intent;
    Handler handler;
    WebSocketClient client;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent=new Intent(BROADCAST_ACTION);
        handler=new Handler();
        registerReceiver(message_sent, new IntentFilter(SocketService.BROADCAST_SERVICE_MSG));

    }
    private final BroadcastReceiver message_sent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra("msg");
            Log.d("Websocket", message);
            client.send(message);

            //Intent i = new Intent(BROADCAST_ACTION).putExtra("msg", client.getReadyState());;
            //sendBroadcast(i);
        }
    };
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started",Toast.LENGTH_LONG).show();// Let it continue running until it is stopped.
        ////////////////////////////////////////////////////////////////////////////////


        try {
            client = new WebSocketClient(new URI(
                    "ws://192.168.1.104:81"),new Draft_17()) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("Websocket", "service open");
                    intent.putExtra("msg", "service open");
                    sendBroadcast(intent);
                    status = true;
                    client.send("HELLO");
                }

                @Override
                public void onMessage(String message) {


                    Log.d("Websocket", message);
                    //intent.putExtra("msg", message);
                    //sendBroadcast(intent);
                    Intent i = new Intent(BROADCAST_ACTION).putExtra("msg", message);
                    sendBroadcast(i);
                }

                @Override
                public void onError(Exception ex) {
                    // TODO Auto-generated method stub
                    Log.d("Websocket", ex.toString());
                    intent.putExtra("msg", ex.toString());
                    sendBroadcast(intent);
                }

                @Override
                public void onClose(int code, String reason, boolean remote){
                    status = false;
                    Log.d("Websocket", "onClose");
                }
            };




        }catch(Exception e){
            Log.d("Websocket", e.toString());

        }
        ///////////////////////////////////////////////////////////////////////////////

//
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 1000);
        return START_STICKY;
    }




    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            client.connect();

            for(int i=0; i<5;i++){
                if(status){
                    intent.putExtra("msg","open");
                    sendBroadcast(intent);
                    break;
                }else{
                    try {
                        intent.putExtra("msg",client.getReadyState().toString());
                        sendBroadcast(intent);
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!status) {
                intent.putExtra("msg","Time Out");
                sendBroadcast(intent);

            }



        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
