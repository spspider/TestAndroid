package com.example.ttester_paukov.ExternalDB_notworked;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.ttester_paukov.ExternalDB.DatabaseOpener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by sp_1 on 03.04.2017.
 */

public class CopyDataBase {
    private static final String DB_NAME = DatabaseOpener.DB_NAME;

    private Context context;
    public CopyDataBase(Context context) {
        this.context = context;
    }

    public SQLiteDatabase openDatabase() {
        File dbFile = context.getDatabasePath(DB_NAME);

        if (!dbFile.exists()) {
            try {
                SQLiteDatabase checkDB = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
                if(checkDB != null){

                    checkDB.close();

                }
                copyDatabase(dbFile);
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    private void copyDatabase(File dbFile) throws IOException {
        InputStream is = context.getAssets().open(DB_NAME);
        OutputStream os = new FileOutputStream(dbFile);

        byte[] buffer = new byte[1024];
        while (is.read(buffer) > 0) {
            os.write(buffer);
        }

        os.flush();
        os.close();
        is.close();
    }
    public boolean copyDatabaseFromAssets(Context context, String databaseName , boolean overwrite)  {

        File outputFile = context.getDatabasePath(databaseName);
        if (outputFile.exists() && !overwrite) {
            return true;
        }

        outputFile = context.getDatabasePath(databaseName + ".temp");
        outputFile.getParentFile().mkdirs();

        try {
            InputStream inputStream = context.getAssets().open(databaseName);
            OutputStream outputStream = new FileOutputStream(outputFile);


            // transfer bytes from the input stream into the output stream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close the streams
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            outputFile.renameTo(context.getDatabasePath(databaseName));

        } catch (IOException e) {
            if (outputFile.exists()) {
                outputFile.delete();
            }
            return false;
        }

        return true;
    }
    public static void movedb(File srcdb, File destdb)
    {
        try
        {
            if (Environment.getExternalStorageDirectory().canWrite())
            {
                if (srcdb.exists())
                {
                    FileChannel src = new FileInputStream(srcdb).getChannel();
                    FileChannel dst = new FileOutputStream(destdb).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
                else
                {
                    //ERROR: "Database file references are incorrect"
                }
            }
            else
            {
                //ERROR: "Cannot write to file"
            }
        }
        catch (Exception e)
        {
            //ERROR: e.getMessage()
        }
    }
}
