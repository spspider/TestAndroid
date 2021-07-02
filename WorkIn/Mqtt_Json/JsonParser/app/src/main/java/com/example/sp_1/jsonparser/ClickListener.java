package com.example.sp_1.jsonparser;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by sp_1 on 16.01.2017.
 */

public class ClickListener extends Activity implements View.OnClickListener {
    private MainActivity getMainactivity2;
    private int idBtn;
    private String idDevice;

    public ClickListener(int id, String idDevice) {
        this.idBtn = id;
        this.idDevice = idDevice;
    }

    @Override
    public void onClick(View v) {

        Button btn = (Button)v;
        getMainactivity2 = (MainActivity) GlobalClass.getInstance().getActivity();
        getMainactivity2.myLog("нажата кнопка: "+v.getId()+btn.getText());
        //btn.getId()
        //idDevice.
        switch (v.getId()) {
            case 1:
                break;

        }

    }
}