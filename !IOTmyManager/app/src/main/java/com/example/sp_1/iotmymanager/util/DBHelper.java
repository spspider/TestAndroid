package com.example.sp_1.iotmymanager.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.sp_1.iotmymanager.NotInPackage.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sp_1 on 12.02.2017.
 */

public class DBHelper extends SQLiteOpenHelper implements BaseColumns {
    public static final int DATABASE_VERSION = 1;

    /** The name of the database file **/
    public static final String DATABASE_NAME = "connections.db";
    /** The name of the connections table **/
    public static final String TABLE_CONNECTIONS = "connections";

    /** Table column for host **/
    public static final String COLUMN_HOST = "host";
    /** Table column for client id **/
    public static final String COLUMN_client_ID = "clientID";
    /** Table column for port **/
    public static final String COLUMN_port = "port";
    /** Table column for ssl enabled**/
    public static final String COLUMN_ssl = "ssl";

    //connection options
    /** Table column for client's timeout**/
    public static final String COLUMN_TIME_OUT = "timeout";
    /** Table column for client's keepalive **/
    public static final String COLUMN_KEEP_ALIVE = "keepalive";
    /** Table column for the client's username**/
    public static final String COLUMN_USER_NAME = "username";
    /** Table column for the client's password**/
    public static final String COLUMN_PASSWORD = "password";
    /** Table column for clean session **/
    public static final String COLUMN_CLEAN_SESSION = "cleanSession";
    /** Table column for **/

    //last will
    /** Table column for last will topic **/
    public static final String COLUMN_TOPIC = "topic";
    /** Table column for the last will message payload **/
    public static final String COLUMN_MESSAGE = "message";
    /** Table column for the last will message qos **/
    public static final String COLUMN_QOS = "qos";
    /** Table column for the retained state of the message **/
    public static final String COLUMN_RETAINED = "retained";
    public static final String FirstTimeSubscribe = "/IoTmanager";
    //sql lite data types
    /** Text type for SQLite**/
    private static final String TEXT_TYPE = " TEXT";
    /** Int type for SQLite**/
    private static final String INT_TYPE = " INTEGER";
    /**Comma separator **/
    private static final String COMMA_SEP = ",";

    /** Create tables query **/
    private static final String SQL_CREATE_ENTRIES =

            "CREATE TABLE " + TABLE_CONNECTIONS + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_HOST + TEXT_TYPE + COMMA_SEP +
                    COLUMN_client_ID + TEXT_TYPE + COMMA_SEP +
                    COLUMN_port + INT_TYPE + COMMA_SEP +
                    COLUMN_ssl + INT_TYPE + COMMA_SEP +
                    COLUMN_TIME_OUT + INT_TYPE + COMMA_SEP +
                    COLUMN_KEEP_ALIVE + INT_TYPE + COMMA_SEP +
                    COLUMN_USER_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    COLUMN_CLEAN_SESSION + INT_TYPE + COMMA_SEP +
                    COLUMN_TOPIC + TEXT_TYPE + COMMA_SEP +
                    COLUMN_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_QOS + INT_TYPE + COMMA_SEP +
                    COLUMN_RETAINED + " INTEGER);";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_CONNECTIONS;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    /*
     * (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onDowngrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public long persistConnection(HashMap<String, String> connection) throws PersistenceException {

        //MqttConnectOptions conOpts = getConnectionOptions();
        //MqttMessage lastWill = conOpts.getWillMessage();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();


        String[] connectionColumns = {
                COLUMN_HOST,
                COLUMN_port,
                COLUMN_client_ID,
                COLUMN_ssl,
                COLUMN_KEEP_ALIVE,
                COLUMN_CLEAN_SESSION,
                COLUMN_TIME_OUT,
                COLUMN_USER_NAME,
                COLUMN_PASSWORD,
                COLUMN_TOPIC,
                COLUMN_MESSAGE,
                COLUMN_RETAINED,
                COLUMN_QOS,
                _ID

        };

        //how to sort the data being returned
        String sort = COLUMN_HOST;
        Cursor c = db.query(TABLE_CONNECTIONS, connectionColumns, null, null, null, null, sort);

        //put the column values object

        values.put(COLUMN_HOST, connection.get(COLUMN_HOST));
        values.put(COLUMN_port, connection.get(COLUMN_port));
        //values.put(COLUMN_client_ID, connection.getId());
        //values.put(COLUMN_ssl, connection.isSSL());

        values.put(COLUMN_KEEP_ALIVE, connection.get(COLUMN_KEEP_ALIVE));
        values.put(COLUMN_TIME_OUT, connection.get(COLUMN_TIME_OUT));
        values.put(COLUMN_USER_NAME, connection.get(COLUMN_USER_NAME));
        //values.put(COLUMN_TOPIC, conOpts.getWillDestination());

        //uses "condition ? trueValue: falseValue" for in line converting of values
        //char[] password = conOpts.getPassword();
        //values.put(COLUMN_CLEAN_SESSION, conOpts.isCleanSession() ? 1 : 0); //convert boolean to int and then put in values
        values.put(COLUMN_PASSWORD, connection.get(COLUMN_PASSWORD)); //convert char[] to String
        values.put(COLUMN_MESSAGE, connection.get(COLUMN_MESSAGE)); // convert byte[] to string
        values.put(COLUMN_QOS, connection.get(COLUMN_QOS));
        long newRowId =-1;
        if (c.getCount()<1) {
            newRowId = db.insert(TABLE_CONNECTIONS, null, values);
        }

        if (c.getCount()>0) {
            int id=1;
            newRowId = db.update(TABLE_CONNECTIONS,values, _ID +"=?", new String[] { String.valueOf(id) });
            //Log.d(MainActivity.TAG,"table"+i+"deleted");
            for (int i=2;i<c.getCount();i++) {
                deleteConnection(i);
                //db.delete(TABLE_CONNECTIONS, _ID + "=?", new String[]{String.valueOf(id)});
                //int delCount = db.delete(TABLE_CONNECTIONS, _ID +"=?" + i, null);
                Log.d(MainActivity.TAG,"table"+i+"deleted"+_ID+c.getCount());
            }

        }

        db.close(); //close the db then deal with the result of the query

        if (newRowId == -1) {
            Log.d(MainActivity.TAG,"newRowId"+newRowId);
            throw new PersistenceException("Failed to persist connection: " + connection.get(COLUMN_HOST));

        }
        else { //Successfully persisted assigning persistecneID
           // connection.assignPersistenceId(newRowId);
            Log.d(MainActivity.TAG,"newRowId"+newRowId);
        }
        return newRowId;
    }

    /**
     * Recreates connection objects based upon information stored in the database
     * @param context Context for creating {@link Connection} objects
     * @return list of connections that have been restored
     * @throws PersistenceException if restoring connections fails, this is thrown
     */
    public ArrayList<HashMap<String, String>> restoreConnections(Context context) throws PersistenceException
    {
        //columns to return
        String[] connectionColumns = {
                COLUMN_HOST,
                COLUMN_port,
                COLUMN_client_ID,
                COLUMN_ssl,
                COLUMN_KEEP_ALIVE,
                COLUMN_CLEAN_SESSION,
                COLUMN_TIME_OUT,
                COLUMN_USER_NAME,
                COLUMN_PASSWORD,
                COLUMN_TOPIC,
                COLUMN_MESSAGE,
                COLUMN_RETAINED,
                COLUMN_QOS,
                _ID

        };

        //how to sort the data being returned
        String sort = COLUMN_HOST;

        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(TABLE_CONNECTIONS, connectionColumns, null, null, null, null, sort);
        HashMap<String,String> list = new HashMap<>(c.getCount());
        ArrayList<HashMap<String,String>> connections = new ArrayList<>();
        for (int i = 0; i < c.getCount(); i++) {
            if (!c.moveToNext()) { //move to the next item throw persistence exception, if it fails
                throw new PersistenceException("Failed restoring connection - count: " + c.getCount() + "loop iteration: " + i);
            }
            //get data from cursor
            list.put(_ID, String.valueOf((c.getLong(c.getColumnIndexOrThrow(_ID)))));
            //basic client information
            list.put(COLUMN_HOST,c.getString(c.getColumnIndexOrThrow(COLUMN_HOST)));
            //String host = c.getString(c.getColumnIndexOrThrow(COLUMN_HOST));
            list.put(COLUMN_client_ID,c.getString(c.getColumnIndexOrThrow(COLUMN_client_ID)));
            list.put(COLUMN_port,c.getString(c.getColumnIndexOrThrow(COLUMN_port)));

            //String clientID = c.getString(c.getColumnIndexOrThrow(COLUMN_client_ID));
            //int port = c.getInt(c.getColumnIndexOrThrow(COLUMN_port));

            //connect options strings
            list.put(COLUMN_USER_NAME,c.getString(c.getColumnIndexOrThrow(COLUMN_USER_NAME)));
            list.put(COLUMN_PASSWORD,c.getString(c.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            list.put(COLUMN_TOPIC,c.getString(c.getColumnIndexOrThrow(COLUMN_TOPIC)));
            list.put(COLUMN_MESSAGE,c.getString(c.getColumnIndexOrThrow(COLUMN_MESSAGE)));


            //String username = c.getString(c.getColumnIndexOrThrow(COLUMN_USER_NAME));
            //String password = c.getString(c.getColumnIndexOrThrow(COLUMN_PASSWORD));
            //String topic = c.getString(c.getColumnIndexOrThrow(COLUMN_TOPIC));
            //String message = c.getString(c.getColumnIndexOrThrow(COLUMN_MESSAGE));
            list.put(COLUMN_QOS,c.getString(c.getColumnIndexOrThrow(COLUMN_QOS)));
            list.put(COLUMN_KEEP_ALIVE,c.getString(c.getColumnIndexOrThrow(COLUMN_KEEP_ALIVE)));
            list.put(COLUMN_TIME_OUT,c.getString(c.getColumnIndexOrThrow(COLUMN_TIME_OUT)));
            //connect options integers
            //int qos = c.getInt(c.getColumnIndexOrThrow(COLUMN_QOS));
            // int keepAlive = c.getInt(c.getColumnIndexOrThrow(COLUMN_KEEP_ALIVE));
            //int timeout = c.getInt(c.getColumnIndexOrThrow(COLUMN_TIME_OUT));
            list.put(COLUMN_CLEAN_SESSION,c.getString(c.getColumnIndexOrThrow(COLUMN_CLEAN_SESSION)));
            list.put(COLUMN_RETAINED,c.getString(c.getColumnIndexOrThrow(COLUMN_RETAINED)));
            list.put(COLUMN_ssl,c.getString(c.getColumnIndexOrThrow(COLUMN_ssl)));
            //get all values that need converting and convert integers to booleans in line using "condition ? trueValue : falseValue"
            //boolean cleanSession = c.getInt(c.getColumnIndexOrThrow(COLUMN_CLEAN_SESSION)) == 1 ? true : false;
            //boolean retained = c.getInt(c.getColumnIndexOrThrow(COLUMN_RETAINED)) == 1 ? true : false;
            //boolean ssl = c.getInt(c.getColumnIndexOrThrow(COLUMN_ssl)) == 1 ? true : false;

            //now create the connection object
            //connection = Connection.createConnection(clientID, host, port, context, ssl);
            //connection.addConnectionOptions(opts);
            //connection.assignPersistenceId(id);
            //store it in the list
            //list.add(connection);
            //Log.d(MainActivity.TAG, String.valueOf(list));
            int id = (int) c.getLong(c.getColumnIndexOrThrow(_ID));
            connections.add(list);
        }
        //close the cursor now we are finished with it
        c.close();
        db.close();
        return connections;

    }


    public void deleteConnection(int id) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_CONNECTIONS, _ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        //don't care if it failed, means it's not in the db therefore no need to delete

    }
    public class PersistenceException extends Exception {

        /**
         * Creates a persistence exception with the given error message
         * @param message The error message to display
         */
        public PersistenceException(String message) {
            super(message);
        }

        /** Serialisation ID**/
        private static final long serialVersionUID = 5326458803268855071L;

    }
}
