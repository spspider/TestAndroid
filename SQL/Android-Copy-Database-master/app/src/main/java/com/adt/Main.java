package com.adt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.adt.database.DBQuery;
import com.adt.preferences.PDefaultValue;
import com.adt.preferences.Prefs;
import com.adt.util.AppVersionCode;

/**
 * Shayan Rais (http://shanraisshan.com)
 * created on 1/4/2017
 */

public class Main extends AppCompatActivity {

    /*---------------------------------------*/
    /* ------- Activity Without View ------- */
    /*---------------------------------------*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if(Prefs.getApplicationVersionCode(this) < AppVersionCode.getApkVersionCode(this))
            checkApplicationVersionCode();

        checkStages();
    }

    //______________________________________________________________________________________________
    void checkApplicationVersionCode() {
        DBQuery db1 = new DBQuery(this);
        db1.createDatabase();
        db1.close();
/*
        switch(Prefs.getApplicationVersionCode(this)) {


            case PDefaultValue.VERSION_CODE:
                DBQuery db = new DBQuery(this);
                db.createDatabase();
                db.close();
                //put this apk version code in Shared Preference
                Prefs.putApplicationVersionCode(this, AppVersionCode.getApkVersionCode(this));
                //recall this method so that doWorkOnLatestVersion() can be called
                checkApplicationVersionCode();
                break;

            default:

                //do this for every version
                break;
        }
        */

    }

    public void checkStages() {
        startActivity(new Intent(this, Activity1.class));
        finish();
        /*
        switch(Prefs.getStage(this)) {
            case PDefaultValue.STAGE:

                break;
            default:
                startActivity(new Intent(this, Activity1.class));
                finish();
                break;
        }
        */
     }
}
