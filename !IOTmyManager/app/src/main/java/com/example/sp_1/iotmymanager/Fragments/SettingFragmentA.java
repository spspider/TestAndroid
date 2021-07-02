package com.example.sp_1.iotmymanager.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sp_1.iotmymanager.NotInPackage.MainActivity;
import com.example.sp_1.iotmymanager.R;
import com.example.sp_1.iotmymanager.Service.MQTT_Service_depricated;
import com.example.sp_1.iotmymanager.util.DBHelper;
import com.example.sp_1.iotmymanager.util.GlobalClass;
import com.example.sp_1.iotmymanager.util.MqttAndroidClient;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.sp_1.iotmymanager.Fragments.FragmentActivityA.ARGUMENT_PAGE_NUMBER;

/**
 * Created by sp_1 on 04.02.2017.
 */

public class SettingFragmentA extends Fragment {
    //private View view;
    private int pageIndex;
    private Button BtnSave;
    private EditText enterServer;
    private EditText inetPort;
    private EditText MQTTlogin;
    private EditText MQTTpassword;
    private DBHelper persistence = null;
    private long Rowid=1;
    HashMap<String,String> currentconn=new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        View v = inflater.inflate(R.layout.settings_a, container, false);

        if (bundle != null) {
            pageIndex = bundle.getInt(ARGUMENT_PAGE_NUMBER, 0);
        switch (pageIndex){
            case 0:
                //v = inflater.inflate(R.layout.settings_a, container, false);
                break;
            case 1:
                //v = inflater.inflate(R.layout.layout_fragment, container, false);
                break;
            default:
                //v = inflater.inflate(R.layout.settings_a, container, false);
                break;
        }
            //TextView tvSection = new (TextView) view.findViewById(R.id.tvSection);
            //tvSection.setText(getString(R.string) + " " + String.valueOf(pageIndex + 1));
        }
        else{
            //v = inflater.inflate(R.layout.settings_a, container, false);
        }

        //HashMap<Integer, HashMap<String,String>> connections = new HashMap<>();
        //attempt to restore state




        //LinearLayout ll = (LinearLayout) view.findViewById(R.id.lMain);
        BtnSave = (Button) v.findViewById(R.id.ButtonOk);
        enterServer= (EditText) v.findViewById(R.id.EnterServer);
        inetPort= (EditText) v.findViewById(R.id.InetPort);
        MQTTlogin = (EditText) v.findViewById(R.id.MQTTlogin);
        MQTTpassword = (EditText) v.findViewById(R.id.MQTTPassword);
        switch (pageIndex) {
            case 0:
                loadPreference();
        }

//loadPresistence();

        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        //savePersistense();
                        savePreferece();
                        Log.d(MainActivity.TAG,"clicked savePersistense"+v.getId());
            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ButtonOk:
                        Log.d(MainActivity.TAG,"clicked"+v.getId());
                        break;
                    default:
                        break;
                }

            }

        });
        //

        //BtnSave.setOnClickListener(buttonListener);

        return v;

    }
    private void loadPreference() {
        MqttAndroidClient GC = new MqttAndroidClient();
        HashMap<String,String> Connectionmap= GC.loadPreference(getActivity());

        String host = Connectionmap.get(DBHelper.COLUMN_HOST);
        String port = Connectionmap.get(DBHelper.COLUMN_port);
        String username = Connectionmap.get(DBHelper.COLUMN_USER_NAME);
        String password = Connectionmap.get(DBHelper.COLUMN_PASSWORD);


        enterServer.setText(host);
        inetPort.setText(port);
        MQTTlogin.setText(username);
        MQTTpassword.setText(password);

        //MqttAndroidClient mAClient = new MqttAndroidClient();
        //String connection="";
        //sendNewPrefernces(host,port,username,password);
        //mAClient.startBind(getActivity(),connection);

        /*
        mAClient.setupConnection(
                getActivity(),
                host,
                port,
                username,
                password);
        */

    }

    private void sendNewPrefernces(String columnHost, String column_port, String columnUserName, String columnPassword) {
        Intent intentSend= new Intent(MQTT_Service_depricated.RECIEVE_NEW_CONNECTION_OPTION);
        Bundle bundle = new Bundle();
        bundle.putString(DBHelper.COLUMN_HOST,columnHost);
        bundle.putString(DBHelper.COLUMN_port,column_port);
        bundle.putString(DBHelper.COLUMN_USER_NAME,columnUserName);
        bundle.putString(DBHelper.COLUMN_PASSWORD,columnPassword);
        intentSend.putExtras(bundle);
        getActivity().sendBroadcast(intentSend);
        //startMyService();
    }


    private void savePreferece() {

        //sPref = getActivity().getPreferences(MODE_PRIVATE);
        //SharedPreferences.Editor ed = sPref.edit();

        String host = enterServer.getText().toString();
        String port = inetPort.getText().toString();
        String username = MQTTlogin.getText().toString();
        String password = MQTTpassword.getText().toString();

        HashMap<String,String> map = new HashMap<>();


        map.put(DBHelper.COLUMN_HOST,host);
        map.put(DBHelper.COLUMN_port,port);
        map.put(DBHelper.COLUMN_USER_NAME,username);
        map.put(DBHelper.COLUMN_PASSWORD,password);
        MqttAndroidClient GC = new MqttAndroidClient();
        GC.savePreferece(getActivity(),map);
        //sendNewPrefernces(host,port,username,password);
    }
    private void loadPresistence() {

        persistence = new DBHelper(getActivity());
        try {
            ArrayList<HashMap<String,String>> connections = persistence.restoreConnections(getActivity());
            currentconn= connections.get(connections.size()-1);//возьмем последнее
            for (int i=0;i<connections.size();i++) {
            }


        }
        catch (DBHelper.PersistenceException e) {
            e.printStackTrace();
        }

        String host="";
        String port="";

        host= currentconn.get(DBHelper.COLUMN_HOST);
        port = (currentconn.get(DBHelper.COLUMN_port));

        enterServer.setText(host== null ? "" : host);
        inetPort.setText(port== null ? "" : port);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
        GlobalClass GC = GlobalClass.getInstance();



        setHasOptionsMenu(true);


    }

    private void savePersistense() {
        String server = enterServer.getText().toString();
        String port = inetPort.getText().toString();
        HashMap<String,String> connection = new HashMap<>();
        connection.put(DBHelper.COLUMN_HOST,server);
        connection.put(DBHelper.COLUMN_port,port);
        Log.d(MainActivity.TAG,"connection"+connection);
        //connection.put(DBHelper.COLUMN_port,conOpt);
        persistence = new DBHelper(getActivity());
        try {
            Rowid = persistence.persistConnection(connection);
        } catch (DBHelper.PersistenceException e) {
            e.printStackTrace();
        }


    }

    public static SettingFragmentA newInstance(int pos) {

        SettingFragmentA f = new SettingFragmentA();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, pos);
        //arguments.putString("pagetag",f.getTargetFragment().getTag());
        //arguments.putString("","");
        //arguments.putString("");
        f.setArguments(arguments);
        return f;
    }


}
