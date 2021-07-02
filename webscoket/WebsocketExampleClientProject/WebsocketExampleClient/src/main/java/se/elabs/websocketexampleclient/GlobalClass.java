package se.elabs.websocketexampleclient;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Created by sp_1 on 25.01.2017.
 */

public class GlobalClass extends Application {
    private static GlobalClass instance = null;
    private static Context context;
    private static Activity activity;
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
    public void setContext(Context context) {
        this.context = context;
    }
}
