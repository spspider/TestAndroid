package com.example.ttester_paukov.ExternalDB_notworked;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.example.ttester_paukov.OpenFileActivity.LOG_TAG;

/**
 * Created by sp_1 on 02.04.2017.
 */

public class Databasehelper extends SQLiteOpenHelper
{
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String DATABASE_NAME = "db1.sqlite";

    public static String DATABASE_PATH;
    public static final int DATABASE_VERSION = 1;
    //public static final int DATABASE_VERSION_old = 1;

    //Constructor
    public Databasehelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ContextWrapper cw =new ContextWrapper(context);
        DATABASE_PATH =cw.getFilesDir().getAbsolutePath()+ "/databases/"; //edited to databases
        this.myContext = context;
    }

    //Create a empty database on the system
    public void createDatabase() throws IOException
    {
        boolean dbExist = checkDataBase();

        if(dbExist)
        {
            Log.v(LOG_TAG, "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }
else
        {
            this.getReadableDatabase();
            this.close();
            copyDataBase();
        }
    }

    //Check database already exist or not
    private boolean checkDataBase()
    {
        boolean checkDB = false;
        try
        {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        }
        catch(SQLiteException e)
        {
        }
        return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase()
    {
        Log.i(LOG_TAG,
                "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        // Open your local db as the input stream
        InputStream myInput = null;
        try
        {
            myInput =myContext.getAssets().open(DATABASE_NAME);
            // transfer bytes from the inputfile to the
            // outputfile
            myOutput =new FileOutputStream(DATABASE_PATH+ DATABASE_NAME);
            while((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i(LOG_TAG,
                    "New database has been copied to device!");


        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    //delete database
    public void db_delete()
    {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if(file.exists())
        {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    //Open database
    public void openDatabase() throws SQLException
    {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase()throws SQLException
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public void onCreate(SQLiteDatabase db)
    {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (newVersion > oldVersion)
        {
            Log.v(LOG_TAG, "Database version higher than old.");
            db_delete();
        }
    }

    //add your public methods for insert, get, delete and update data in database.
}