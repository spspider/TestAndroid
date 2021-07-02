package com.example.a1.myapplication.locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a1.myapplication.MyServer;
import com.example.a1.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the "Edit" activity for a <i>Locale</i> plug-in.
 */
public final class EditActivity extends Activity {
    public static final String ACTION_FILTER = "ACTION_filter_e_robot";
    public static final String START_FROM_EDIT = "start";
    public static final String HTTPS_ACTIVITY_RESULT_STRING = "HTTPS_ACTIVITY_RESULT_STRING";
    public static final String IP_ADRESS = "ipadress";
    public static final String HTTPS_ACTIVITY_RESULT = "activity_E-Robot_result";
    /**
     * Menu ID of the save item.
     */
    private static final int MENU_SAVE = 1;
    /**
     * Menu ID of the don't save item.
     */
    private static final int MENU_DONT_SAVE = 2;
    private static final int MENU_UPDATE = 0;
    public static int EditActivityOpen = 0;
    Context context;
    String TAG = BackgroundService_httpd.TAG;
    String[] HTTPS = null;
    String[] http_array = new String[0];
    final List<String> http_list = new ArrayList<String>(Arrays.asList(http_array));
    BroadcastReceiver updateUIReciver;
    TextView ipTV;
    /**
     * {@inheritDoc}
     */

    Utils utils;
    String Selected_request = null;
    /**
     * Flag boolean that can only be set to true via the "Don't Save" menu item in {@link #onMenuItemSelected(int, MenuItem)}. If
     * true, then this {@code Activity} should return {@link Activity#RESULT_CANCELED} in {@link #finish()}.
     * <p>
     * There is no need to save/restore this field's state when the {@code Activity} is paused.
     */
    private boolean isCancelled;

    void updateIpAdress() {


        List<String> IpadressList = utils.determineHostAddress_List();
        StringBuilder sb = new StringBuilder();
        for (int a = 0; a < IpadressList.size(); a++) {
            sb.append(IpadressList.get(a) + ":" + MyServer.PORT + "\n");

        }
        ipTV.setText(sb);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locale_main);
        context = getApplicationContext();
        TextView httpTV = (TextView) findViewById(R.id.http);
        httpTV.setText(R.string.http_navigate);
        //httpTV.setText();
        ipTV = (TextView) findViewById(R.id.ip);
        final ListView lvMain = (ListView) findViewById(R.id.lvMain);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_singlechoice, http_list);
        lvMain.setAdapter(adapter);
        lvMain.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvMain.setStackFromBottom(true);
        lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        utils = new Utils();

        updateIpAdress();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_FILTER);
        updateUIReciver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                ArrayList<String> https = intent.getStringArrayListExtra(HTTPS_ACTIVITY_RESULT);
                String request = intent.getStringExtra(HTTPS_ACTIVITY_RESULT_STRING);

                Log.i(TAG, "recieve:" + https);
                http_list.clear();
                http_list.addAll(https);
                adapter.notifyDataSetChanged();

                String ip = intent.getStringExtra(IP_ADRESS);
                ipTV.setText(ip + ":" + MyServer.PORT);
                //lvMain.smoothScrollToPosition(adapter.getCount() -1);
            }

        };
        registerReceiver(updateUIReciver, filter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Selected_request = lvMain.getItemAtPosition(position).toString();
                Log.i(TAG, "Selected_request:" + Selected_request);
            }
        });

        final Intent serviceIntent = new Intent(context, BackgroundService_httpd.class);
        serviceIntent.putExtra(START_FROM_EDIT, 1);
        Log.e(TAG, "запуск сервиса");
        context.startService(serviceIntent);

        EditActivityOpen = 1;



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // дерегистрируем (выключаем) BroadcastReceiver
        unregisterReceiver(updateUIReciver);
        EditActivityOpen = 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //String HTTPS_ = new String[0];
        //List<String> HTTPS_ = null;
        ArrayList<String> HTTPS_result = data.getStringArrayListExtra(HTTPS_ACTIVITY_RESULT);
        Log.i(TAG, "activity result" + HTTPS_result);
/*		HTTPS = new String[HTTPS_result.size()];
        int i = 0;
		for (String s: HTTPS_result)
			HTTPS[i++] = s;*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finish() {
        if (isCancelled)
            setResult(RESULT_CANCELED);
        else {

            /*
             * This is the return Intent, into which we'll put all the required extras
             */
            final Intent returnIntent = new Intent();

            /*
             * This extra is the data to ourselves: either for the Activity or the BroadcastReceiver. Note that anything placed in
             * this Bundle must be available to Locale's class loader. So storing String, int, and other basic objects will work
             * just fine. You cannot store an object that only exists in your app, as Locale will be unable to serialize it.
             */
            final Bundle storeAndForwardExtras = new Bundle();
            //String HTTPS= ((Editable) input.getText()).toString();

            Log.i(TAG, "Selected_request:" + Selected_request);
            if (Selected_request == null) {
                setResult(RESULT_CANCELED);
            } else {
                stopService(new Intent(EditActivity.this, BackgroundService_httpd.class));
                storeAndForwardExtras.putCharSequence(BackgroundService_httpd.BUNDLE_HTTPS, Selected_request);
                returnIntent.putExtra(com.twofortyfouram.locale.api.Intent.EXTRA_STRING_BLURB, Selected_request);

                returnIntent.putExtra(com.twofortyfouram.locale.api.Intent.EXTRA_BUNDLE, storeAndForwardExtras);

                setResult(RESULT_OK, returnIntent);
            }
        }

        super.finish();
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        final PackageManager manager = getPackageManager();

        // TODO: fill in your help URL here
        // Note: title set in onCreate

        /*
         * We are dynamically loading resources from Locale's APK. This will only work if Locale is actually installed
         */
/*		menu.add(SharedPreferences.getTextResource(manager, SharedResources.STRING_MENU_HELP))
			.setIcon(SharedResources.getDrawableResource(manager, SharedResources.DRAWABLE_MENU_HELP)).setIntent(helpIntent);

		menu.add(0, MENU_DONT_SAVE, 0, SharedResources.getTextResource(manager, SharedResources.STRING_MENU_DONTSAVE))
			.setIcon(SharedResources.getDrawableResource(manager, SharedResources.DRAWABLE_MENU_DONTSAVE)).getItemId();

		menu.add(0, MENU_SAVE, 0, SharedResources.getTextResource(manager, SharedResources.STRING_MENU_SAVE))
			.setIcon(SharedResources.getDrawableResource(manager, SharedResources.DRAWABLE_MENU_SAVE)).getItemId();
*/
        menu.add(0,MENU_UPDATE,0,R.string.updateIP);

        menu.add(0, MENU_DONT_SAVE, 0, "Dont save");

        menu.add(0, MENU_SAVE, 0, "Save").getItemId();


        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SAVE: {
                finish();
                return true;
            }
            case MENU_DONT_SAVE: {
                isCancelled = true;
                finish();
                return true;
            }
            case MENU_UPDATE: {
                updateIpAdress();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(networkChangeReceiver);
    }


        private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG,"Network connectivity change");
                updateIpAdress();
            }
        };







}