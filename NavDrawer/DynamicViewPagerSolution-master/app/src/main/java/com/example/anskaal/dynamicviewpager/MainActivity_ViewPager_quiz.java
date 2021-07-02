package com.example.anskaal.dynamicviewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity_ViewPager_quiz extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DynamicViewPager mViewPager;
    private static ArrayList<Fragment> fragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (DynamicViewPager) findViewById(R.id.viewPager);
        DynamicViewPagerAdapter adapter = new DynamicViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);


        fragments.add(Quiz_Fragment.newInstance(fragments.size()));
        fragments.add(Quiz_Fragment.newInstance(fragments.size()));
        fragments.add(Quiz_Fragment.newInstance(fragments.size()));
        adapter.setItems(fragments);

        mViewPager.setCurrentItem(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fullscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        int itemNow = mViewPager.getCurrentItem();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_download) {

            //ArrayList<Fragment> fragments = new ArrayList<>();
            fragments.add(Quiz_Fragment.newInstance(fragments.size()));


            DynamicViewPagerAdapter adapter = new DynamicViewPagerAdapter(getSupportFragmentManager());
            adapter.setItems(fragments);

            mViewPager.setAdapter(adapter);
            mViewPager.setCurrentItem(itemNow);

            return true;
        }


        return false;
    }
}
