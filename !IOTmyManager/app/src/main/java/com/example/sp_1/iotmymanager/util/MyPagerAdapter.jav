package com.example.sp_1.iotmymanager.util;




import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

import com.example.sp_1.iotmymanager.Activitys.BaseActivity;
import com.example.sp_1.iotmymanager.Fragments.FragmentActivityA;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sp_1 on 04.02.2017.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    public static ViewPager mViewPager;
    private List<Fragment> fragments = new ArrayList<>();
    public static ArrayList<String> pagesMy = new ArrayList<>();
    //private final List<PageInfo> pages;
    private final Context context;
    private ArrayList<Fragment> mFragments;
    public MyPagerAdapter(FragmentManager fm, ViewPager mViewPager, Context context) {
        super(fm);
        mFragments = new ArrayList<>();
        //pages.add("DefaultPage");
        //mViewPager.setAdapter(this);
        this.context = context;
        this.mViewPager = mViewPager;
        mViewPager.setAdapter(this);
        //this.pages = new LinkedList<PageInfo>();
        GlobalClass GC = GlobalClass.getInstance();
        GC.setPager(mViewPager);

    }

    @Override
    public Fragment getItem(int pos) {


        return mFragments.get(pos);


    }
    public void setItems(ArrayList<Fragment> fragments) {
        mFragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return mFragments.indexOf(object);
    }

    /*
            @Override
            public Fragment getItem(int pos) {
                switch (pos) {

                    case 0:
                        return FragmentActivityA.newInstance();
                    case 1:
                        //return TutorialFragmentB.newInstance();
                    case 2:
                        //return TutorialFragmentC.newInstance(getResources().getString(R.string.tutorial_pageC1_title), getResources().getString(R.string.tutorial_pageC1_text));
                    case 3:
                        //return TutorialFragmentC.newInstance(getResources().getString(R.string.tutorial_pageC2_title), getResources().getString(R.string.tutorial_pageC2_text));
                    default:
                        return FragmentActivityB.newInstance();
                        //return null;
                }
            }
    */
    @Override
    public int getCount() {
        return mFragments.size();
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
        return pagesMy.get(position);

        //return null;
    }


    public void addFragment(Fragment fragment, String tabTitle) {
        //notifyDataSetChanged();
        //FragmentActivityA.newInstance(pos);

        fragments.add(fragment);
        pagesMy.add(tabTitle);
        mViewPager.setAdapter(this);
        Log.d(BaseActivity.TAG,"fragmentAdded "+fragment+tabTitle);
        notifyDataSetChanged();
        //tabTitles.add(tabTitle);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        //getSupportFragmentManager().beginTransaction().add(createdFragment, "first").commit();
        //Log.d(MainActivity.TAG, "TAG:" + createdFragment.getTag());
        return createdFragment;

    }

}
