package com.example.sp_1.iotmymanager.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.sp_1.iotmymanager.Activitys.MainDevicesPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static android.media.CamcorderProfile.get;
import static com.example.sp_1.iotmymanager.Activitys.MainDevicesPager.mFragments;
import static com.example.sp_1.iotmymanager.Activitys.MainDevicesPager.mFragmentsName;


/**
 * Created by sp_1 on 19.03.2017.
 */

public class MyPagerAdapter_imp extends FragmentPagerAdapter {

    public MyPagerAdapter_imp(FragmentManager fm){
        super(fm);

        //mFragments = new ArrayList<>();
    }



    public void setItems(ArrayList<Fragment> fragments) {
        mFragments = fragments;
        notifyDataSetChanged();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        /*
        switch (position) {
            case 0:
                return "Welcome";
            case 1:
                return "Kitchen";
            case 2:
                return "Phone";
            case 3:
                return "Registered Device";
        }
        */
        String PageName = "DefaultName";

        //if (mFragmentsName.size()!=0){
        //    PageName=mFragmentsName.get(position);
       // }
        return PageName;

        //return null;
    }
    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {

        //mPageReferenceMap.put(position, mFragments.get(position));
        return mFragments.get(position);
    }
    /*
    public Fragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }
    */
    @Override
    public int getItemPosition(Object object) {
        return MainDevicesPager.mFragments.indexOf(object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        //mPageReferenceMap.put(position, fragment);
        return fragment;
    }

}
