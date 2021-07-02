package com.multitouch.example;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MultiTouch extends Activity {

    public static final String LOG_TAG = "myLogs";
    // these matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;
    //float scale = 0;
    float txtsize=0;
    float prevscale=1,prevscaleRadioBtn=1,txtsizeRb=1;
    float scale=1;
    float multiplysize=10;
    float radioTXTsize =0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //ImageView view = (ImageView) findViewById(R.id.imageView);
        //view.setOnTouchListener(this);
        TextView tview = (TextView) findViewById(R.id.textView1);
        Utils_onTouchSize utils = new Utils_onTouchSize();
        utils.setPrevscale(tview.getTextSize());
        tview.setOnTouchListener(utils.touchListener2);
        //float tSize = tview.getTextSize();

        RadioGroup rgrp = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioButton rb0 = (RadioButton) findViewById(R.id.radio0);
        RadioButton rb1 = (RadioButton) findViewById(R.id.radio1);
        RadioButton rb2 = (RadioButton) findViewById(R.id.radio2);
        RadioButton rb3 = (RadioButton) findViewById(R.id.radio3);
        utils.setRadioGroup(rgrp);
        //rgrp.setOnTouchListener(utils.touchListenerRadioBtn);
        rb0.setOnTouchListener(utils.touchListener2);
        rb1.setOnTouchListener(utils.touchListener2);
        rb2.setOnTouchListener(utils.touchListener2);
        rb3.setOnTouchListener(utils.touchListener2);
        //rgrp.getChil

        utils.setPrevscaleRbtn(rb0.getTextSize());

        //prevscale=2;
    }



    /**
     * Determine the space between the first two fingers
     */

}
