package com.example.sp_1.iotmymanager.NotInPackage;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.sp_1.iotmymanager.Activitys.SettingPager;
import com.example.sp_1.iotmymanager.R;
import com.example.sp_1.iotmymanager.util.GlobalClass;
import com.example.sp_1.iotmymanager.util.JsonParcer_MQTT_subscribe;


public class MainActivity extends AppCompatActivity {
    private GlobalClass app;
    //public ViewPager pager;
    private int mCurrentSelectedPosition;
    int tabis=0;
    //private NavigationView ;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private static final int PAsettings = 1;
    private static final int PAdefault = 0;

    private android.support.v4.app.ActionBarDrawerToggle actionBarDrawerToggle;
    public static String firstTag;
    public static String TAG = "log_m";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //setContentView(R.layout.activity_main);



        setUpNavigationDrawer();
        //setTabs(PAdefault);


    }

    private void setUpNavigationDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = (GlobalClass) getApplication().getApplicationContext();
        app.setActivity(this);
        app.setContext(this);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView NavigationView  = (NavigationView) findViewById(R.id.nav_view);
        NavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

        /*
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
*/
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                JsonParcer_MQTT_subscribe parcer = new JsonParcer_MQTT_subscribe();
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_camera) {
                    //tabis=PAdefault;
                    //setTabs(PAdefault);
                    //String Allvalues = parcer.insertMessage("","");
                    String message ="{\"id\":\"0\",\"page\":\"Kitchen\",\"descr\":\"Light-0\",\"widget\":\"toggle\",\"topic\":\"/IoTmanager/dev01-kitchen/light0\",\"color\":\"blue\"}";
                    String message2 = "{\"id\":\"1\",\"page\":\"Kitchen\",\"descr\":\"Light-1\",\"widget\":\"toggle\",\"topic\":\"/IoTmanager/dev01-kitchen/light1\",\"color\":\"orange\"}";
                    String message3 = "{\"id\":\"2\",\"page\":\"Kitchen\",\"descr\":\"Dimmer\",\"widget\":\"range\",\"topic\":\"/IoTmanager/dev01-kitchen/dim-light\",\"style\":\"range-calm\",\"badge\":\"badge-assertive\",\"leftIcon\":\"ion-ios-rainy-outline\",\"rightIcon\":\"ion-ios-rainy\"}";
                    String message4 =  "{\"id\":\"3\",\"page\":\"Kitchen\",\"descr\":\"ADC\",\"widget\":\"small-badge\",\"topic\":\"/IoTmanager/dev01-kitchen/ADC\",\"badge\":\"badge-balanced\"}";
                    String message5 = "{\"id\":\"4\",\"page\":\"Outdoor\",\"descr\":\"Garden light\",\"widget\":\"toggle\",\"topic\":\"/IoTmanager/dev01-kitchen/light4\",\"color\":\"red\"}";
                    //message = getResources().getString(R.string.json_str);
                    //String message3  = Resources.getSystem().getString(R.string.json_str);
                    //myLog(message3);

                    parcer.insertMessage(message,"",getApplicationContext());
                    parcer.insertMessage(message2,"",getApplicationContext());
                    parcer.insertMessage(message3,"",getApplicationContext());
                    parcer.insertMessage(message4,"",getApplicationContext());
                    parcer.insertMessage(message5,"",getApplicationContext());

                    // Handle the camera action
                } else if (id == R.id.nav_gallery) {
                    //tabis=PAdefault;

                    String[] jsonRecieveArrStatus=new String[8];
                    jsonRecieveArrStatus[0]="{\"sTopic\":\"/IoTmanager/dev01-kitchen/light0\",\"status\":\"0\"}";
                    jsonRecieveArrStatus[1]="{\"sTopic\":\"/IoTmanager/dev01-kitchen/light1\",\"status\":\"0\"}";
                    jsonRecieveArrStatus[2]="{\"sTopic\":\"/IoTmanager/dev01-kitchen/dim-light\",\"status\":\"1023\"}";
                    jsonRecieveArrStatus[3]="{\"sTopic\":\"/IoTmanager/dev01-kitchen/ADC\",\"status\":\"0\"}";
                    jsonRecieveArrStatus[4]="{\"sTopic\":\"/IoTmanager/dev01-kitchen/light4\",\"status\":\"0\"}";
                    jsonRecieveArrStatus[5]="{\"sTopic\":\"/IoTmanager/dev01-kitchen/red\",\"status\":\"0\"}";
                    jsonRecieveArrStatus[6]="{\"sTopic\":\"/IoTmanager/dev01-kitchen/green\",\"status\":\"0\"}";
                    jsonRecieveArrStatus[7]="{\"sTopic\":\"/IoTmanager/dev01-kitchen/blue\",\"status\":\"200\"}";
                    for(int i=0;i<jsonRecieveArrStatus.length;i++){
                        parcer.insertMessage(jsonRecieveArrStatus[i],"",getApplicationContext());
                    }
                } else if (id == R.id.nav_slideshow) {
                    //Intent secActivity = new Intent(this,secondActivity.class);
                    startIntent(SettingPager.class);

                    //tabis=PAsettings;


                } else if (id == R.id.nav_manage) {
                    tabis=PAdefault;

                } else if (id == R.id.nav_share) {


                } else if (id == R.id.nav_send) {

                }


                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                //mDrawerLayout.closeDrawer(mNavigationView);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

          });
        //setTabs(tabis);
/*
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(getString(R.string.drawer_opened));
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
*/
        }

    private void startIntent(Class<SettingPager> secondActivityClass) {
        Intent startInt = new Intent(this,secondActivityClass);
        startActivity(startInt);
    }

    ;
/*
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
*/
        /////////////////////pager////////////////////////////////////////
        //setTabs(mCurrentSelectedPosition);


    public void setTabs(int count) {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        switch (count){
                case PAsettings:
                    //MyPagerAdapterSettings adapterViewPagerSettings = ;
                    //pager.setAdapter(new MyPagerAdapterSettings(getSupportFragmentManager(), pager));

                break;
            default:

                //pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),pager));
                //MyPagerAdapter adapterViewPager =
                //pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), pager));
                break;
        }

        pager.setOffscreenPageLimit(5);


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


/*
    private class MyPagerAdapter extends FragmentPagerAdapter {

    }
*/
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */
/*
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
*/


}
