package net.mustafaozcan.materialnavigation;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sp_1 on 06.02.2017.
 */

public class ContentAdapter2 extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 1;
    private final Context c;

    public ContentAdapter2(FragmentManager fragmentManager, Context context, int item_count) {
        super(fragmentManager);
        NUM_ITEMS = item_count;
        c = context;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return ContentFragment2.newInstance(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return c.getString(R.string.tab) + " " + String.valueOf(position + 1);
    }
}
