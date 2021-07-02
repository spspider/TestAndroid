package com.example.sp_1.iotmymanager.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.sp_1.iotmymanager.Fragments.SettingFragmentA;


/**
 * Created by sp_1 on 04.02.2017.
 */

public class MyPagerAdapterSettings extends FragmentPagerAdapter {




    public MyPagerAdapterSettings(FragmentManager fm2, ViewPager smth) {
        super(fm2);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        return SettingFragmentA.newInstance(position);
        //return null;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

}
