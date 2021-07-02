package com.example.sp_1.iotmymanager.Activitys;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sp_1.iotmymanager.Fragments.FragmentActivityA;
import com.example.sp_1.iotmymanager.R;
import com.example.sp_1.iotmymanager.util.MqttAndroidClient;
import com.example.sp_1.iotmymanager.util.MyPagerAdapter_imp;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * Created by sp_1 on 11.02.2017.
 */

public class MainDevicesPager extends BaseActivity {

    private static final int MENU_ITEM_ITEM1 = 1;
    private static final int MENU_ITEM_ITEM_REFRESH = 2;
    private static final int MENU_ITEM_ITEM_setTabs2 = 3;

    //public static Activity activity = null;
    public static MyPagerAdapter_imp adapter;

    HashMap<Integer,LayoutInflater> ltInflater= new HashMap<>();
    public static ArrayList<Fragment> mFragments=new ArrayList<>();
    public static ArrayList<String> mFragmentsName = new ArrayList<>();
    ViewPager Vpager;
    /*
        private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSwipeRefreshLayout.isRefreshing()){
                    //if (pager==null) {
                        //setTabs();
                   // }


                    //MqttAndroidClient MA = new MqttAndroidClient();
                    //MA.loadAllPreferences(activity);//включить для старта сервиса!!!

                    //JsonParcer_MQTT_subscribe JP = new JsonParcer_MQTT_subscribe();
                    JsonParcer_MQTT_subscribe.AllOneDvice.clear();

                    Toast.makeText(getApplication().getApplicationContext(), "Refreshing...", Toast.LENGTH_SHORT).show();
                }
                mSwipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        Random random = new Random();
                        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

                        //mTxtColor.setTextColor(color);

                        //– setRefreshing(boolean): Desativa a visibilidade do swipeRefreshLayout.
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
                //coloringTriadworks();
            }
        };
        */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity = this;
        setAdapter();
        Toast.makeText(this,"startSomeActivity",Toast.LENGTH_LONG).show();
        //setTabs();
        //setTabs2();

        // load views

        //adapter.addPage( "first", FragmentActivityA.class);
        //adapter.addPage( "second", FragmentActivityA.class);
        //pager.setAdapter(adapter);
        ////////////////
        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer1);
        //mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        //mSwipeRefreshLayout.setActivity(this);
        //– setColorSchemeResources(): Ele recebe quatro cores diferentes que vão ser utilizados para colorir a animação.
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.orange, R.color.purple);
//////////////////
        //mNavigationView.getMenu().getItem(0).setChecked(true);
        //mNavigationView.se.setItemChecked(position, true);
        //MqttAndroidClient MA = new MqttAndroidClient();
        //MA.loadAllPreferences(this);//включить для старта сервиса!!!

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_ITEM1, Menu.NONE, "addFragment");
        menu.add(Menu.NONE, MENU_ITEM_ITEM_REFRESH, Menu.NONE, "Refresh");
        menu.add(Menu.NONE, MENU_ITEM_ITEM_setTabs2, Menu.NONE, "setTabs2");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_ITEM1:
                addPage();
                return true;
            case MENU_ITEM_ITEM_REFRESH:
                    startFragments();
                return true;
            case MENU_ITEM_ITEM_setTabs2:
                setTabs2();
                return true;
            default:
                return false;
        }
    }

    private void startFragments() {
        MqttAndroidClient MA = new MqttAndroidClient();
        MA.loadAllPreferences(this);//включить для старта сервиса!!!
    }

public void setAdapter(){
    Vpager = (ViewPager) findViewById(R.id.pager);
    adapter = new MyPagerAdapter_imp( getSupportFragmentManager());
    adapter.notifyDataSetChanged();
    //adapter.setItems(PageFragments);
    Vpager.setOffscreenPageLimit(5);
    Vpager.setAdapter(adapter);
}
    public void addPage(){

        int NewPage = mFragments.size();
        mFragmentsName.add(NewPage,"name");//добавим имя фрагмента
        mFragments.add(FragmentActivityA.newInstance(NewPage));//добавляем фрагмент
        //ViewPager Vpager = (ViewPager) findViewById(R.id.pager);
        //adapter = new MyPagerAdapter_imp( getActivtiy().getSupportFragmentManager());
        adapter.setItems(mFragments);
        //adapter.notifyDataSetChanged();
        //создаем новый вид
        //fragmentMy = (FragmentActivityA) getFragmentStatic(page);
        //fragmentMy = (FragmentActivityA) getFragmentStatic(NewPage);

//        ltInflater.put(NewPage,MyPagerAdapter_imp.mFragments.get(NewPage).getActivity().getLayoutInflater());

}

public void setTabs2(){
    //Vpager = (ViewPager) findViewById(R.id.pager);
    //adapter = new MyPagerAdapter_imp(getSupportFragmentManager());
    //Vpager.setAdapter(adapter);

    Fragment AddFragment;
    Integer PagePos;
    PagePos= mFragments.size();
    AddFragment = FragmentActivityA.newInstance(PagePos);
    mFragmentsName.add("name");
    mFragments.add(AddFragment);
    //Vpager.setOffscreenPageLimit(5);

    adapter.setItems(mFragments);

}

    /*
    public void setTabs() {

        //MyPagerAdapter.pagesMy.add("DefaultPage");
        // ViewPager pager = (ViewPager) getActivity().findViewById(R.id.pager);
        // pager.setAdapter(new MyPagerAdapter(((FragmentActivity)getActivity()).getSupportFragmentManager(), pager,getApplication().getApplicationContext()));
        // pager.setOffscreenPageLimit(5);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter adapter = new MyPagerAdapter( getSupportFragmentManager(), pager, activity);
        PageFragments.add(FragmentActivityA.newInstance(PageFragments.size()));
        PageFragments.add(FragmentActivityA.newInstance(PageFragments.size()));
        mViewPager.setAdapter(adapter);
        adapter.setItems(PageFragments);


    }
*/

    //public Activity getActivity() {
    //    return activity;
    //}

}
