package com.example.ttester_paukov_new_tests.dynamicviewpager;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sergey on 16.07.2017.
 */

public class LogFragment extends Fragment {
    View rootView;
    TextView LogTextView;
    public static String Text = "";
    public static String BROADCAST_LOG = "Log_Broadcast";
    public static String StringExtra = "Log";
    static BroadcastReceiver br;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // rootView = inflater.inflate(R.layout.logview, null);
        //LogTextView = (TextView) rootView.findViewById(R.id.LogTextView);
       // LogTextView.append(Text);

        return rootView;


    }


}
