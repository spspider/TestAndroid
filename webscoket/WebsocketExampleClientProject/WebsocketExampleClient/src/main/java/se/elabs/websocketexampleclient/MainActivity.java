package se.elabs.websocketexampleclient;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;

import static android.R.attr.text;
import static se.elabs.websocketexampleclient.R.id.container;
import static se.elabs.websocketexampleclient.SocketService.BROADCAST_SERVICE_MSG;

public class MainActivity extends Activity {
    private WebSocketClient mWebSocketClient;
    static Button sendMessage;
    private static final int sendmsg=0;
    private static Context context;
    private MainActivity getMainactivity2;
    private GlobalClass app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = (GlobalClass) getApplication().getApplicationContext();
        app.setActivity(this);
        app.setContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BroadcastReceiver receiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("Websocket", "recieved");
                String message = intent.getStringExtra("msg");
                TextView textView = (TextView)findViewById(R.id.messages);
                textView.setText(textView.getText() + "\n" + message);

            }
        };
        //connectWebSocket();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(container, new PlaceholderFragment())
                    .commit();
        }

        registerReceiver(receiver, new IntentFilter(SocketService.BROADCAST_ACTION));
    }
    // Method to start the service
    public void startService() {
        startService(new Intent(getBaseContext(), SocketService.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), SocketService.class));
    }
    private View.OnClickListener oclBtnOk = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Меняем текст в TextView (tvOut)
            switch (v.getId()) {
                case R.id.sendMessage:
                    sendMessage();
                    break;

            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        menu.add(Menu.NONE, sendmsg, Menu.NONE, "Connect");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                startService();
                return true;
            case sendmsg:
                Intent intent = new Intent(SocketService.BROADCAST_SERVICE_MSG);
                intent.putExtra("msg", "HELLO");
                sendBroadcast(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private MainActivity getMainactivity2;

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            getMainactivity2 = (MainActivity) GlobalClass.getInstance().getActivity();
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            Button button = (Button) rootView.findViewById(R.id.sendMessage);
            button.setOnClickListener(getMainactivity2.oclBtnOk);
            return rootView;

        }

    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://192.168.1.104:81/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri, new Draft_17()){
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
//                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
                mWebSocketClient.send("HELLO");


            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView)findViewById(R.id.messages);
                        textView.setText(textView.getText() + "\n" + message);
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public void sendMessage() {
        Intent intent = new Intent(SocketService.BROADCAST_SERVICE_MSG);
        intent.putExtra("msg", "HELLO");
        sendBroadcast(intent);
    }
}
