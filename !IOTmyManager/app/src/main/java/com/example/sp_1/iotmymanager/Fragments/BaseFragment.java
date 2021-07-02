package com.example.sp_1.iotmymanager.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sp_1.iotmymanager.Activitys.MainDevicesPager;
import com.example.sp_1.iotmymanager.NotInPackage.MainActivity;
import com.example.sp_1.iotmymanager.R;
import com.example.sp_1.iotmymanager.util.GlobalClass;


import static android.content.ContentValues.TAG;



/**
 * Created by sp_1 on 25.01.2017.
 */

public abstract class BaseFragment extends Fragment {
    BroadcastReceiver br;
    private GlobalClass app;
    private static TextView tv,tv2;
    public static ScrollView scrollView1;
    LinearLayout ll;

    private Bundle savedState;
    private boolean saved;
    private static final String _FRAGMENT_STATE = "FRAGMENT_STATE";

    @Override
    public void onSaveInstanceState(Bundle state) {
        if (getView() == null) {
            state.putBundle(_FRAGMENT_STATE, savedState);
        } else {
            Bundle bundle = saved ? savedState : getStateToSave();

            state.putBundle(_FRAGMENT_STATE, bundle);
        }

        saved = false;

        super.onSaveInstanceState(state);
    }


    @Override
    public void onDestroyView() {
        savedState = getStateToSave();
        saved = true;

        super.onDestroyView();
    }

    protected Bundle getSavedState() {
        return savedState;
    }

    protected abstract boolean hasSavedState();

    protected abstract Bundle getStateToSave();

    public FragmentActivityA getFragment_my(int page){

        //MyPagerAdapter MPA= (MyPagerAdapter) g
        //ViewPager mViewPager = MPA.getPagerAdapter();
        //Log.d(MainActivity.TAG, String.valueOf(mViewPager));
        //GlobalClass GC = GlobalClass.getInstance();
        //ViewPager mViewPager = GC.getPager();
        String name = makeFragmentName(R.id.pager, page);
        //Log.d(MainActivity.TAG,"find for:"+name);
        FragmentActivityA fragmentMy = (FragmentActivityA) getFragmentManager().findFragmentByTag(name);

//        FragmentPagerAdapter adapter = (FragmentPagerAdapter)mViewPager.getAdapter();
 //       adapter.getItem(mViewPager.getCurrentItem());
        //Log.d(MainActivity.TAG, String.valueOf(fragmentMy));
        //Fragment viewPagerFragment = fragmentMy.getFragmentManager().findFragmentByTag(name);
        if(fragmentMy != null) {
            Log.d(MainActivity.TAG,"Fragment found");
            return fragmentMy;

        }
        Log.d(MainActivity.TAG,"Fragment NULL");
        return null;


    }

    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState != null) {
            savedState = savedInstanceState.getBundle(_FRAGMENT_STATE);
        }
/////////broadcast reciever////////////////////////////

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String task = intent.getStringExtra("backString");

                myLogB(task,0);
                //getFragment(0).


                Log.d("my_log", "recieved");
            }

        };

        IntentFilter intFilt = new IntentFilter("RECIVEFILTER");
        //Log.d(MainActivity.TAG, "Reciever: " + "RECIVEFILTER");
        // регистрируем (включаем) BroadcastReceiver
        getActivity().registerReceiver(br, intFilt);

///////////////////////////////



    }

    public void myLogB(String str, int position) {
        if (ll==null){create_layout(position);}

            tv.append(str + "\n");
            scrollView1.post(new Runnable() {
                @Override
                public void run() {
                    scrollView1.fullScroll(View.FOCUS_DOWN);
                }
            });

        //myLog("test");
    }

    private void create_layout(int position) {


        View frView = getFragment_my(position).getView();
        ll = (LinearLayout) frView.findViewById(R.id.lMain);
        tv = new TextView(getActivity());
        tv.setText("myLogB");
        ll.addView(tv);
        scrollView1 = (ScrollView) frView.findViewById(R.id.scrollView2);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, ".onCreateOptions() entered");
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}
