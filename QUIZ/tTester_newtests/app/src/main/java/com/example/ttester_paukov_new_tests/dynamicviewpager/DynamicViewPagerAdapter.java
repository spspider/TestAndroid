package com.example.ttester_paukov_new_tests.dynamicviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by anskaal on 2/29/16.
 */
public class DynamicViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;

    public DynamicViewPagerAdapter(FragmentManager fm){
        super(fm);

        mFragments = new ArrayList<>();
    }

    public void setItems(ArrayList<Fragment> fragments) {
        mFragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return mFragments.indexOf(object);
    }


}
