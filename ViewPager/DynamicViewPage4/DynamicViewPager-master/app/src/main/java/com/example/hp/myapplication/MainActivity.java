package com.example.hp.myapplication;

import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public ViewPager pager;
    public StatePagerAdapter adapter;
    int adPosition = 8;
    boolean onScreen = false;
    int counter = 1;
    ArrayList<Fragment> fragments;
    List<Integer> indexAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indexAds = new ArrayList<>();

        fragments = new ArrayList<>();
        fragments.add(new AFragment());
        fragments.add(new BFragment());
        fragments.add(new CFragment());
        fragments.add(new CFragment());

        DFragment d = new DFragment();

        if (adPosition == 1) {
            fragments.add(adPosition - 1, d);
            indexAds.add(adPosition - 1);
        }
        if (adPosition == 2) {
            fragments.add(adPosition - 1, d);
            indexAds.add(adPosition - 1);
        }
        adapter = new StatePagerAdapter(getSupportFragmentManager(), fragments);
        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(adapter);


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            float tempPositionOffset = 0;
            boolean onScrollingRight = false;
            int currentPosition = 0;
            private boolean scrollStarted, checkDirection;
            private static final float thresholdOffset = 0.5f;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                    /*if (tempPositionOffset < positionOffset) {
                         Log.d(TAG, "scrolling left ...");
                        onScrollingRight = true;
                    } else {
                         Log.d(TAG, "scrolling right ...");
                        onScrollingRight = false;
                    }

                    tempPositionOffset = positionOffset;*/

                // Log.d("eric", "position " + position + "; " + " positionOffset " + positionOffset + "; " + " positionOffsetPixels " + positionOffsetPixels + ";");

                if (checkDirection) {
                    if (thresholdOffset > positionOffset) {
                        Log.i(TAG, "going left");
                        onScrollingRight = true;
                    } else {
                        Log.i(TAG, "going right");
                        onScrollingRight = false;
                    }
                    checkDirection = false;
                }
            }

            @Override
            public void onPageSelected(int position) {


                Log.i(TAG, "size " + adapter.getCount() + " position " + position + " counter " + counter);

                if (onScrollingRight) {
                    if (counter == adPosition - 2) {

                        Log.i(TAG, "enter left");

                        adPosition = 0;
                        if (position > 0) {
                            int i = adapter.addFragment(new DFragment(), position);
                            indexAds.add(i);
                        }
                        //if(counter < adapter.getCount() - 1)
                        if (position + 1 < adapter.getCount()) {
                            int i = adapter.addFragment(new DFragment(), position + 2);
                            indexAds.add(i);
                        }
                        pager.setCurrentItem(position + 1, false);

                        return;
                    }
                } else {

                    if (counter == adPosition - 2) {

                        Log.i(TAG, "enter right");
                        adPosition = 0;
                        if (position >= 1) {
                            int i = adapter.addFragment(new DFragment(), position);
                            indexAds.add(i);
                        }else if(position == 0){

                            adapter.addFragment(new DFragment(), position + 1);
                            pager.setCurrentItem(position, false);
                            return;
                        }

                        if (position < adapter.getCount()) {
                            int i = adapter.addFragment(new DFragment(), position + 2);
                            indexAds.add(i);
                        }
                        pager.setCurrentItem(position + 1, false);

                        return;
                    }
                }

                if (adPosition == 1) {
                    onScreen = true;
                    adPosition = 0;
                }

                if (adapter.getFragment(position) instanceof DFragment) {
                    onScreen = true;
                    Log.i(TAG, "remove");
                }

                if (position <= adapter.getCount() - 1) {
                    if (onScreen && !(adapter.getFragment(position) instanceof DFragment)
                            && (( (adapter.getFragment(position - 1) instanceof DFragment)
                            || adapter.getFragment(position + 1) instanceof DFragment))) {

                        onScreen = false;
                        currentPosition = position;

                        Log.i(TAG, "current position " + currentPosition);
                        Log.i(TAG, "delete");

                        Collections.reverse(indexAds);

                        for (int i : indexAds) {
                            adapter.removeFragment(pager, i);
                        }
                        Log.i(TAG, "remove fragment");

                        Collections.reverse(indexAds);

                        if (indexAds.size() == 2) {

                            if (indexAds.get(0) > position && indexAds.get(1) > position) {
                                pager.setCurrentItem(position);
                                Log.i(TAG, "remove 1");
                            }
                            else if (indexAds.get(0) < position && indexAds.get(1) > position) {
                                pager.setCurrentItem(position - 1);
                                Log.i(TAG, "remove 2");
                            }
                            else if (indexAds.get(0) < position && indexAds.get(1) < position) {
                                pager.setCurrentItem(position - 2);
                                Log.i(TAG, "remove 3");
                            }
                        }else if(indexAds.size() == 1){

                            if(indexAds.get(0) <= position) {
                                pager.setCurrentItem(position - 1);
                                Log.i(TAG, "remove 4");
                            }
                            else{
                                pager.setCurrentItem(position);
                                Log.i(TAG, "remove 5");
                            }
                        }

                        indexAds.clear();
                    }
                }
                /*
                if ( position + 1 == currentPosition || (position - 1 == currentPosition)){

                    currentPosition = 0;

                    Collections.reverse(indexAds);

                    for (int i : indexAds) {

                        adapter.removeFragment(pager, i);
                    }
                    Log.i(TAG, "remove fragment");

                    indexAds.clear();
                    pager.setCurrentItem(position, false);
                }*/
                counter++;

                //Log.i(TAG, "counter " + counter);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        });
    }
}