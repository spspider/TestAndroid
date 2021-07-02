package com.alleviate.dynamicviewpager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public static SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Marvel Cinematic Universe");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refresh", Snackbar.LENGTH_LONG).show();
                mSectionsPagerAdapter.notifyDataSetChanged();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                // Whatever you intent to do here...

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    private void populate_db() {

        String[] mcu_p1 = getResources().getStringArray(R.array.mcu_p1);

        SQLiteHelper db = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase dbw = db.getWritableDatabase();

        for (String p1 : mcu_p1) {
            ContentValues insert_values = new ContentValues();
            insert_values.put(db.db_mcu_name, p1);
            insert_values.put(db.db_mcu_phase, "Phase 1");


            long resid = dbw.insert(db.db_mcu, null, insert_values);
            Log.d("Database", "Data Inserted for P1 Id " + resid);
        }

        String[] mcu_p2 = getResources().getStringArray(R.array.mcu_p2);

        for (String p2 : mcu_p2) {
            ContentValues insert_values = new ContentValues();
            insert_values.put(db.db_mcu_name, p2);
            insert_values.put(db.db_mcu_phase, "Phase 2");

            long resid = dbw.insert(db.db_mcu, null, insert_values);
            Log.d("Database", "Data Inserted for P2 Id " + resid);
        }

        String[] mcu_p3 = getResources().getStringArray(R.array.mcu_p3);

        for (String p3 : mcu_p3) {
            ContentValues insert_values = new ContentValues();
            insert_values.put(db.db_mcu_name, p3);
            insert_values.put(db.db_mcu_phase, "Phase 3");

            long resid = dbw.insert(db.db_mcu, null, insert_values);
            Log.d("Database", "Data Inserted for P3 Id " + resid);
        }

        String[] mcu_p4 = getResources().getStringArray(R.array.mcu_p4);

        for (String p4 : mcu_p4) {
            ContentValues insert_values = new ContentValues();
            insert_values.put(db.db_mcu_name, p4);
            insert_values.put(db.db_mcu_phase, "Phase 4");

            long resid = dbw.insert(db.db_mcu, null, insert_values);
            Log.d("Database", "Data Inserted for P4 Id " + resid);
        }

        dbw.close();
        db.close();
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

}
