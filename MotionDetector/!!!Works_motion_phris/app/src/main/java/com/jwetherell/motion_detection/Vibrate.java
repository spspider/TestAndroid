package com.jwetherell.motion_detection;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by sp_1 on 07.03.2017.
 */

public class Vibrate {
    public void vibration(Context context){
        Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(200);
        Log.i(MotionDetectionActivity.TAG, "vibrate");


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"spspider@mail.ru"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject");
        i.putExtra(Intent.EXTRA_TEXT   , "detected");
        try {
            context.startActivity(Intent.createChooser(i, "Send mail..."));
            Toast.makeText(context, "send", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
