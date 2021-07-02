package com.adt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.adt.adapters.AdapterCategories;
import com.adt.database.DBQuery;

/**
 * Shayan Rais (http://shanraisshan.com)
 * created on 1/4/2017
 */

public class Activity1 extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Data
        DBQuery db = new DBQuery(this);
        db.open();
        //adapter.setCategory(db.getCategories(0));
        db.close();
    }

    //______________________________________________________________________________________________

    //______________________________________________________________________________________________
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
