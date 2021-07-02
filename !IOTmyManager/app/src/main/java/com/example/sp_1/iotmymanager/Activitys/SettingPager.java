package com.example.sp_1.iotmymanager.Activitys;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.Toast;

import com.example.sp_1.iotmymanager.R;
import com.example.sp_1.iotmymanager.util.MyPagerAdapterSettings;

/**
 * Created by sp_1 on 11.02.2017.
 */

public class SettingPager extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this,"start_secondActivity",Toast.LENGTH_LONG).show();
        setTabs();
        mNavigationView.getMenu().getItem(1).setChecked(true);
    }
    public void setTabs() {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        //pager.removeAllViews();
        pager.setAdapter(new MyPagerAdapterSettings(getSupportFragmentManager(), pager));
        pager.setOffscreenPageLimit(5);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //MenuItem searchItem = menu.findItem(R.id.action_search);


        return super.onCreateOptionsMenu(menu);
    }
}
