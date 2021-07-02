package com.example.hp.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by HP on 12/08/2016.
 */
public class StatePagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG ="StatePagerAdapter";

    public ArrayList<Fragment> fragments = new ArrayList<>();

    public StatePagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public int addFragment(Fragment v)
    {
        return addFragment(v, fragments.size());
    }

    public int addFragment(Fragment v, int position)
    {
        fragments.add (position, v);
        notifyDataSetChanged();
        return position;
    }

    public int removeFragment (ViewPager pager, Fragment v)
    {
        return removeFragment (pager, fragments.indexOf (v));
    }

    public int removeFragment (ViewPager pager, int position)
    {

        pager.setAdapter (null);
        fragments.remove (position);
        pager.setAdapter (this);
        notifyDataSetChanged();

        return position;
    }

    public Fragment getFragment(int position)
    {
        return fragments.get (position);
    }

    public void removeAllFragments(ViewPager pager){

        pager.setAdapter (null);
        //fragments.removeAll(fragments);
        fragments.clear();
        pager.setAdapter (this);

    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}
