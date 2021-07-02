package com.example.sp_1.iotmymanager.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ServiceConnection;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.sp_1.iotmymanager.NotInPackage.MainActivity;
import com.example.sp_1.iotmymanager.Fragments.FragmentActivityA;


/**
 * Created by sp_1 on 13.01.2017.
 */

public class GlobalClass extends Application {
    private static Activity activity;
    private static Fragment fragment;
    //private static FragmentActivityA fragmentActivityA;
    //private static MainActivity Mactivity;
    private int selectedPage;
    private static GlobalClass instance = null;
    private static Context context;
    private ViewPager pager;

    ServiceConnection sConn;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MainActivity.TAG,"Application created "+getActivity());
        ////MqttAndroidClient mAndClient = new MqttAndroidClient(getActivity()).startBind();
        //mAndClient.onReceive();

    }


    public synchronized static GlobalClass getInstance()
    {
        if (instance == null) {
            instance = new GlobalClass();
        }

        return instance;
    }

   public Fragment getFragment() {
        //fragmentActivityA = (FragmentActivityA) getFragment(i);
        return fragment;
    }

    public void setFragment(FragmentActivityA fragment) {

        this.fragment = fragment;    }

    public Context getContext() {
        return this.context;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public void setSelectedPage(int selectedPage) {
        this.selectedPage = selectedPage;
        //FragmentActivityA fragmentActivityA = (FragmentActivityA) getFragment().getFragmentManager()
    }

    public void setPager(ViewPager pager) {        this.pager = pager;    }

    public ViewPager getPager() {        return pager;    }



}
