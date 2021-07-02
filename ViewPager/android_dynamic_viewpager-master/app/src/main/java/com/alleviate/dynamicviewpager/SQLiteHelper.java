package com.alleviate.dynamicviewpager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by felix on 1/6/16.
 * Created at Alleviate.
 * shirishkadam.com
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String db_database = "MCU";
    public static final int db_version = 1;

    public static final String db_mcu = "MCU_Movies";
    public static final String db_mcu_id = "Id";
    public static final String db_mcu_name = "Name";
    public static final String db_mcu_phase = "Phase";



    public SQLiteHelper(Context context) {
        super(context, db_database, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_db_client = "CREATE TABLE "+db_mcu+" ( "+db_mcu_id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+db_mcu_name+" VARCHAR NOT NULL, "+db_mcu_phase+" VARCHAR NOT NULL);";

        sqLiteDatabase.execSQL(create_db_client);}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {

    }
}
