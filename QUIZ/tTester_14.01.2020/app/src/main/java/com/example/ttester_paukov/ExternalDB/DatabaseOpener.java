package com.example.ttester_paukov.ExternalDB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.example.ttester_paukov.DbHelper;
import com.example.ttester_paukov.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.example.ttester_paukov.OpenFileActivity.LOG_TAG;
import static com.example.ttester_paukov.OpenFileActivity.activity;

/**
 * Created by sp_1 on 03.04.2017.
 */
public class DatabaseOpener extends SQLiteOpenHelper {
    private Context myContext;
    public static String DB_NAME3 = "dbADV";
    //public static String DB_NAME2= activity.getString(R.string.dbName);
    public static String DB_NAME =activity.getString(R.string.dbname_gradle);
            //R.string.dbName;
    static int DB_VERSION = 1;
    SQLiteDatabase myDataBase;
    static String DB_PATH;

    public DatabaseOpener(Context context) throws IOException {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;

        if (Build.VERSION.SDK_INT >= 4.2) {
            DB_PATH = myContext.getApplicationInfo().dataDir+ "/databases/";
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
        this.getReadableDatabase();
        this.close();
            try {
                String filePath = myContext.getDatabasePath(DB_NAME).getAbsolutePath();
                //copyDataBase();
                File file = new File(DB_PATH + DB_NAME);
                copyDataBase2(file,DB_NAME);
            } catch (IOException e) {
                Log.e(LOG_TAG,"не копируется!!!!!!!!!!!!!!!");
                throw new Error("Error copying database");

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
    private void copyDataBase2(File dbFile, String db_name) throws IOException{

        InputStream is = null;
        OutputStream os = null;

        SQLiteDatabase db = myContext.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
        //db.close();
        try {
            DbHelper dbHelper=new DbHelper(myContext);
            dbHelper.onCreate(db);
            is = myContext.getAssets().open("databases/"+db_name);
            os = new FileOutputStream(dbFile);

            byte[] buffer = new byte[1024];
            while (is.read(buffer) > 0) {
                os.write(buffer);
                //Log.d(LOG_TAG, String.valueOf(buffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw(e);
        } finally {
            try {
                if (os != null) os.close();
                if (is != null) is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myinput = null;
            myinput = myContext.getAssets().open("databases/" + DB_NAME);

        // Path to the just created empty db
        String outfilename = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myoutput = null;

            myoutput = new FileOutputStream(DB_PATH + DB_NAME);



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
    class MySQLiteOpenHelper extends SQLiteOpenHelper {

        MySQLiteOpenHelper(Context context, String databaseName) {
            super(context, databaseName, null, 2);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
    public void openDataBase() throws SQLException {
        // Open the database
        //MySQLiteOpenHelper helper = new MySQLiteOpenHelper(myContext,DB_PATH +DB_NAME);
        //SQLiteDatabase database = helper.getReadableDatabase();
        //String filePath = database.getPath();
       // database.close();

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