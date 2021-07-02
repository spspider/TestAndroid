package com.example.ttester_paukov_new_tests.ExternalDB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by sp_1 on 03.04.2017.
 */
public class DatabaseOpener extends SQLiteOpenHelper {
    private Context myContext;
    static String  DB_NAME="db";
    static int  DB_VERSION=13;
    SQLiteDatabase myDataBase;
static String DB_PATH;

    public DatabaseOpener(Context context) throws IOException {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;

        if (Build.VERSION.SDK_INT >= 4.2) {
            DB_PATH = myContext.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + myContext.getPackageName() + "/databases/";
        }

        boolean dbexist = checkDataBase();
        if (dbexist) {
            openDataBase();
        } else {
            System.out.println("Database doesn't exist");
            createDataBase();
        }
    }

    public void createDataBase() throws IOException {
        boolean dbexist = checkDataBase();
        if (dbexist) {
            // System.out.println(" Database exists.");
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    private void copyDataBase() throws IOException {
        // Open your local db as the input stream
        InputStream myinput = myContext.getAssets().open("databases/" + DB_NAME);

        // Path to the just created empty db
        String outfilename = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(DB_PATH + DB_NAME);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer)) > 0) {
            myoutput.write(buffer, 0, length);
        }

        // Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    public void openDataBase() throws SQLException {
        // Open the database
        String mypath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}