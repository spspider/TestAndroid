package com.example.sp_1.iotmymanager.Activitys;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sp_1.iotmymanager.R;
import com.example.sp_1.iotmymanager.Service.MQTT_Service_depricated;
import com.example.sp_1.iotmymanager.util.GlobalClass;
import com.example.sp_1.iotmymanager.util.JsonParcer_MQTT_subscribe;
import com.example.sp_1.iotmymanager.util.MqttAndroidClient;

import java.util.HashMap;
import java.util.Random;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * @author dipenp
 *
 * This activity will add Navigation Drawer for our application and all the code related to navigation drawer.
 * We are going to extend all our other activites from this BaseActivity so that every activity will have Navigation Drawer in it.
 * This activity layout contain one frame layout in which we will add our child activity layout.
 */
public class BaseActivity extends AppCompatActivity {


	protected NavigationView mNavigationView;
	protected static int position;
	private static boolean isLaunch = true;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	//public SharedPreferences sPref = getDefaultSharedPreferences(getApplicationContext());;
	public static String TAG = "log_m";
	private SwipeRefreshLayout mSwipeRefreshLayout;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//mAClient.ActionStart(this);
		//mAClient.ActionConnect(this);
		//Log.d(MainActivity.TAG,"onCreate");

		setContentView(R.layout.navigation_drawer_base_layout);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();

		try {
			assert actionBar != null;
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setSubtitle(getString(R.string.menu1));
			actionBar.setDisplayShowTitleEnabled(true);
		} catch (Exception ignored) {
		}

		//frameLayout = (FrameLayout)findViewById(R.id.content_frame);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
		mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				menuItem.setChecked(true);
				switch (menuItem.getItemId()) {
					case R.id.navigation_item_1:
						openActivity(0);
						break;
					case R.id.navigation_item_2:
						openActivity(1);
						break;
					case R.id.navigation_item_3:

						break;
					case R.id.navigation_item_4:
						//setTabs2(0);
						break;
				}

				//setTabs(mCurrentSelectedPosition + 1);

				mDrawerLayout.closeDrawer(mNavigationView);
				return true;
			}
		});
		//mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer opens
		//mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		// set up the drawer's list view with items and click listener


		// enable ActionBar app icon to behave as action to toggle nav drawer
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//getSupportActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions between the sliding drawer and the action bar app icon
		actionBarDrawerToggle = new ActionBarDrawerToggle(
				this,						/* host Activity */
				mDrawerLayout, 				/* DrawerLayout object */
				toolbar,     /* nav drawer image to replace 'Up' caret */
				R.string.open_drawer,       /* "open drawer" description for accessibility */
				R.string.close_drawer)      /* "close drawer" description for accessibility */
		{
			@Override
			public void onDrawerClosed(View drawerView) {
//				getSupportActionBar().setTitle(listArray[position]);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
//				getSupportActionBar().setTitle(getString(R.string.app_name));
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				super.onDrawerStateChanged(newState);
			}
		};
		mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

		/**
		 * As we are calling BaseActivity from manifest file and this base activity is intended just to add navigation drawer in our app.
		 * We have to open some activity with layout on launch. So we are checking if this BaseActivity is called first time then we are opening our first activity.
		 * */
		if(isLaunch){
			/**
			 *Setting this flag false so that next time it will not open our first activity.
			 *We have to use this flag because we are using this BaseActivity as parent activity to our other activity.
			 *In this case this base activity will always be call when any child activity will launch.
			 */
			isLaunch = false;
			openActivity(0);
		}
		//mDrawerToggle.setDrawerIndicatorEnabled(true);
		//mDrawerLayout.setDrawerListener(mDrawerToggle);
		//startMyService();

	}


	/**
	 * @param position
	 *
	 * Launching activity when any list item is clicked.
	 */
	protected void openActivity(int position) {

		/**
		 * We can set title & itemChecked here but as this BaseActivity is parent for other activity,
		 * So whenever any activity is going to launch this BaseActivity is also going to be called and
		 * it will reset this value because of initialization in onCreate method.
		 * So that we are setting this in child activity.
		 */
//		mDrawerList.setItemChecked(position, true);
//		setTitle(listArray[position]);
		//mDrawerLayout.closeDrawer(mDrawerList);
		BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.

		switch (position) {
			case 0:
				startActivity(new Intent(this, MainDevicesPager.class));
				break;
			case 1:
				startActivity(new Intent(this, SettingPager.class));
				break;
			case 2:
				//startActivity(new Intent(this, Item3Activity.class));
				break;
			case 3:
				//startActivity(new Intent(this, Item4Activity.class));
				break;
			case 4:
				//startActivity(new Intent(this, Item5Activity.class));
				break;

			default:
				break;
		}

		//Toast.makeText(this, "Selected Item Position::"+position, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
			mDrawerLayout.closeDrawer(mNavigationView);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item != null && item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
				mDrawerLayout.closeDrawer(mNavigationView);
			} else {
				mDrawerLayout.openDrawer(mNavigationView);
			}
			return true;
		}

		return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
	}
}
