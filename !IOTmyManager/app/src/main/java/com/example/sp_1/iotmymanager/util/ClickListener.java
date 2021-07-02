package com.example.sp_1.iotmymanager.util;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.example.sp_1.iotmymanager.Activitys.BaseActivity;
import com.example.sp_1.iotmymanager.Fragments.FragmentActivityA;
import com.example.sp_1.iotmymanager.NotInPackage.MainActivity;
import com.example.sp_1.iotmymanager.R;

/**
 * Created by sp_1 on 16.01.2017.
 */

public class ClickListener extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener,CompoundButton.OnCheckedChangeListener {
    private MainActivity getMainactivity2;
    private FragmentActivityA fragmentMy;
    //private Activity activity;
    private int idBtn;
    private String idDevice;
    boolean clear_to_send=true,clear_to_send_bar= true;
    public ClickListener(int id, String idDevice) {
        this.idBtn = id;
        this.idDevice = idDevice;
        //fragmentActivityA = (FragmentActivityA)getFragmentManager().findFragmentByTag("tag");
    }

    @Override
    public void onClick(final View v) {

        //Button btn = (Button)v;

        GlobalClass app = GlobalClass.getInstance();
        fragmentMy = (FragmentActivityA) app.getFragment();


        //action_clear_to_send(true);
        if (clear_to_send) {


            switch (idDevice) {
                case "MotionDetector":

                    //Log.d(BaseActivity.TAG,"нажат Motion Detector");
                    break;


            }
            Log.d(BaseActivity.TAG,"нажата кнопка: " + v.getId()+idDevice);
            fragmentMy.myLogB("нажата кнопка: " + v.getId(), 0);
            clear_to_send=false;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    clear_to_send=true;
                    //my_button.setBackgroundResource(R.drawable.defaultcard);
                }
            }, 200);
        }

        //fragmentActivityA = (FragmentActivityA)g

        //btn.getId()
        //idDevice.


    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        GlobalClass app = GlobalClass.getInstance();
        fragmentMy = (FragmentActivityA) app.getFragment();


        if (clear_to_send_bar) {
            fragmentMy.myLogB("новое значение: "+progress+seekBar.getId(),0);

            clear_to_send_bar=false;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    clear_to_send_bar=true;
                    //my_button.setBackgroundResource(R.drawable.defaultcard);
                }
            }, 200);
        }
        else
        {
            //Log.d(MainActivity.TAG,"слишком быстрая отправка");
        }


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        GlobalClass app = GlobalClass.getInstance();
        fragmentMy = (FragmentActivityA) app.getFragment();

        //fragmentActivityA = (FragmentActivityA)g
        fragmentMy.myLogB("нажат переключатель: "+buttonView.getId(),0);
        //buttonView.setText("");
    }
}