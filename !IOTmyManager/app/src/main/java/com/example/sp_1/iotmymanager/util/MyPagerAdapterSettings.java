package com.example.sp_1.iotmymanager.util;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.sp_1.iotmymanager.Fragments.SettingFragmentA;


import java.util.ArrayList;

/**
 * Created by sp_1 on 04.02.2017.
 */

public class MyPagerAdapterSettings extends FragmentPagerAdapter {
    private FragmentManager fragMan;

    private ArrayList<View> views = new ArrayList<View>();

    public MyPagerAdapterSettings(FragmentManager fm, ViewPager mViewPager) {

        super(fm);
        this.fragMan =fm;
        GlobalClass GC = GlobalClass.getInstance();
        GC.setPager(mViewPager);
    }



    @Override
    public Fragment getItem(int position) {
        return SettingFragmentA.newInstance(position);
        //return null;
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "MQTT";
            case 1:
                return "Webscoket";

        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //View v = views.get (position);
        //container.addView (v);
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        //getSupportFragmentManager().beginTransaction().add(createdFragment, "first").commit();
        //Log.d(MainActivity.TAG, "TAG:" + createdFragment.getTag());
        return createdFragment;

    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
//        container.removeView((RelativeLayout) object);
    }
}
