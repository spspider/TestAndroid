package com.example.sp_1.jsonparser;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Created by sp_1 on 13.01.2017.
 */

public class GlobalClass extends Application {
    private static Activity activity;
    private static MainActivity Mactivity;
    private String name;
    private String email;
    private static GlobalClass instance = null;
    private static Context context;

    public synchronized static GlobalClass getInstance()
    {
        if (instance == null) {
            instance = new GlobalClass();
        }

        return instance;
    }

    public Context getContext() {
        return this.context;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate() {

    }

    public String getName() {

        return name;
    }


    public void setContext(Context context) {
        this.context = context;
    }
}
