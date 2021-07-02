package com.alleviate.dynamicviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by felix on 10/6/16.
 * Created at Alleviate.
 * shirishkadam.com
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 0:
                return new Phase1Fragment();
            case 1:
                return new Phase2Fragment();
            case 2:
                return new Phase3Fragment();
            case 3:
                return new Phase4Fragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Phase 1";
            case 1:
                return "Phase 2";
            case 2:
                return "Phase 3";
            case 3:
                return "Phase 4";
        }
        return null;
    }
}