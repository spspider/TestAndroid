package com.example.sp_1.iotmymanager.Fragments;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sp_1.iotmymanager.Activitys.BaseActivity;

import com.example.sp_1.iotmymanager.Activitys.MainDevicesPager;
import com.example.sp_1.iotmymanager.NotInPackage.MainActivity;
import com.example.sp_1.iotmymanager.R;
import com.example.sp_1.iotmymanager.Service.MQTT_Service_depricated;
import com.example.sp_1.iotmymanager.util.ClickListener;
import com.example.sp_1.iotmymanager.util.CreateListItem;
import com.example.sp_1.iotmymanager.util.GlobalClass;
import com.example.sp_1.iotmymanager.util.JsonParcer_MQTT_subscribe;
import com.example.sp_1.iotmymanager.util.MqttAndroidClient;

import com.example.sp_1.iotmymanager.util.MyPagerAdapter_imp;
import com.jjoe64.motiondetection.motiondetection.ImageProcessing;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import static com.example.sp_1.iotmymanager.R.id.myLiLa;
import static com.example.sp_1.iotmymanager.R.id.pager;
import static com.example.sp_1.iotmymanager.util.JsonParcer_MQTT_subscribe.AllOneDvice;


/**
 * Created by sp_1 on 25.01.2017.
 */

public class FragmentActivityA extends BaseFragment {
    private static TextView tv,tv_up;
    private GlobalClass app;
    BroadcastReceiver br;
    public Activity RunningActivity;
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    static FragmentActivityA FragmentA;
    public int pageNumber;
    public String pageTag;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<String> myData;

    private TextView vstup;
    private Bundle savedState = null;
/////////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment, container, false);


        //getFragmentManager().findFragmentById(R.id.viewpager).getView().findViewById(R.id.viewpager);
        //RunningActivity = FragmentActivityA.getActivity();
        //getFragmentManager().findFragmentByTag("");
        //LinearLayout ll = (LinearLayout) v.findViewWithTag("");
        setRetainInstance(true);
        app = (GlobalClass) getActivity().getApplication().getApplicationContext();
        app.setFragment(this);
        //View frView = getFragment(0).getView();
        LinearLayout ll = (LinearLayout) v.findViewById(R.id.lMain);
        tv = new TextView(getActivity());
        tv.setText("");
        ll.addView(tv);
        scrollView1 = (ScrollView) v.findViewById(R.id.scrollView2);
        myLog("page: "+pageNumber);
        myLog("thisTag:"+getTag());

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        //– setColorSchemeResources(): Ele recebe quatro cores diferentes que vão ser utilizados para colorir a animação.
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.orange, R.color.purple);

        if (pageNumber==3){
            LinearLayout linLayout = (LinearLayout) v.findViewById(myLiLa);
            LayoutInflater ltInflater =  getActivity().getLayoutInflater();
            View item = ltInflater.inflate(R.layout.item, linLayout, false);
            Button btnNew = new Button(getActivity());
            //Button Btn = new Button(fragmentMy.getActivity());
            btnNew.setText("MotionDetector");
            //btnNew.setId();
            //btnNew.setOnClickListener(new ClickListener(btnNew.getId(),"MotionDetector"));
            btnNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent MotionDetect;
                    MotionDetect = new Intent(getActivity(), com.jjoe64.motiondetection.MainActivity.class);
                    startActivity(MotionDetect);
                }
            });
            //seekBar.setOnSeekBarChangeListener(new ClickListener(seekBar.getId(), get("id")));
            linLayout.addView(btnNew, item.getLayoutParams());

        }

        Log.d(BaseActivity.TAG,"!!!!!!!!!!!!!!DeviceList:"+String.valueOf(AllOneDvice));
        CreateListItem CLI = new CreateListItem();
        //MyPagerAdapter_imp.mFragmentsName.clear();
        //MyPagerAdapter_imp.mFragments.clear();
        for(int i=0;i<AllOneDvice.size();i++){
            //CLI.printAllHashMapMap(AllOneDvice.get(i));
        }

        //myFragment
        return v;

    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (mSwipeRefreshLayout.isRefreshing()){

                //MqttAndroidClient MA = new MqttAndroidClient();
                //MA.loadAllPreferences(getActivity());//включить для старта сервиса!!!
                //AllOneDvice.clear();


                //ViewPager pager = (ViewPager) activity.findViewById(R.id.pager);
                //MyPagerAdapter adapter = new MyPagerAdapter(((FragmentActivity)getActivity()).getSupportFragmentManager(), pager,activity);
                //log = (TextView) findViewById(R.id.logView);
                //adapter.addFragment(FragmentActivityA.newInstance(2),"Second");
                //adapter.notifyDataSetChanged();
                //pager.setAdapter(adapter);


                Toast.makeText(getActivity(), "Refreshing FragmentActivity...", Toast.LENGTH_SHORT).show();
            }
            mSwipeRefreshLayout.setRefreshing(true);
            new Handler().postDelayed(new Runnable() {
                @Override public void run() {

                    Random random = new Random();
                    int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

                    //mTxtColor.setTextColor(color);

                    //– setRefreshing(boolean): Desativa a visibilidade do swipeRefreshLayout.
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
            //coloringTriadworks();
        }
    };



    public void myLog(String str) {
        tv.append(str + "\n");
        scrollView1.post(new Runnable() {
            @Override
            public void run() {
                scrollView1.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //GlobalClass GC = GlobalClass.getInstance();
        //GC.setFragment(this);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        pageTag=getArguments().getString("pagetag");
        //Log.d(BaseActivity.TAG,pageTag.);

        setHasOptionsMenu(true);
    }

    public static FragmentActivityA newInstance(int pos) {
//        MainDevicesPager.adapter.notifyDataSetChanged();

        FragmentActivityA f = new FragmentActivityA();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, pos);
        //arguments.putString("pagetag",f.getTargetFragment().getTag());
        //arguments.putString("","");
        //arguments.putString("");

        //Log.d(BaseActivity.TAG,"onActivityCreated");


        f.setArguments(arguments);




        return f;
    }

    @Override
    protected boolean hasSavedState() {
        Bundle state = getSavedState();

        if (state == null) {
            return false;
        }

    Log.d(BaseActivity.TAG,"RestoreData");
        //restore your data here

        return true;
    }

    @Override
    protected Bundle getStateToSave() {
        Log.d(BaseActivity.TAG,"getStateToSave");
        return null;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        if (hasSavedState()) {
            return;
        }


        //your code here
    }



}
