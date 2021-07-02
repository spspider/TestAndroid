package com.grisaf.mqttsample;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;

import static android.R.attr.button;
import static com.grisaf.mqttsample.MQTT_Service_depricated.DEBUG_TAG;
import static com.grisaf.mqttsample.R.id.btn_stop;
import static com.grisaf.mqttsample.R.id.publish_btn;
import static com.grisaf.mqttsample.R.id.buttonSubscribe;
import static com.grisaf.mqttsample.R.id.connectButton2;
import static com.grisaf.mqttsample.R.id.connectButton3;
import static com.grisaf.mqttsample.R.id.onStart;
import static com.grisaf.mqttsample.R.id.sendButton;
import static com.grisaf.mqttsample.R.string.messageRecieved;
import static com.grisaf.mqttsample.R.string.publish;
import static com.grisaf.mqttsample.R.string.subscribe;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	//int port1=Integer.parseInt(getString(R.string.port1));;
	static int port1;
	//static String ;
	static String pass,login;
	public static TextView resultText;
	public static Context mContext;
	private static MainActivity instance = null;
	private static Button sendButton;


	public void SetStatus(String published) {
		resultText.setText(published);
	}

	public synchronized static MainActivity getInstance()
	{
		if (instance == null) {
			instance = new MainActivity();
		}

		return instance;
	}

	public Context getContext() {

			return  mContext;//на запрос getContext() отвечаем контекстом
		}
	public void setMessage(){

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		MainActivity.mContext = getApplicationContext();
		setContentView(R.layout.activity_main);
		Button connect = (Button)findViewById(R.id.connectButton);
		resultText = (TextView)findViewById(R.id.resultTextView);
		sendButton = (Button)findViewById(R.id.sendButton);
		Button connectButton2 =(Button)findViewById(R.id.connectButton2);
		Button connectButton3 =(Button)findViewById(R.id.connectButton3);
		Button onStart =(Button)findViewById(R.id.onStart);
		Button Subscribe_btn=(Button)findViewById(R.id.buttonSubscribe);
		Button Publish_btn =(Button)findViewById(R.id.publish_btn);
		Button btn_stop = (Button)findViewById(R.id.btn_stop);

		connectButton2.setOnClickListener(mCorkyListener);
		connectButton3.setOnClickListener(mCorkyListener);
		onStart.setOnClickListener(mCorkyListener);
		Subscribe_btn.setOnClickListener(mCorkyListener);
		Publish_btn.setOnClickListener(mCorkyListener);
		btn_stop.setOnClickListener(mCorkyListener);

		connect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText serverEditText = (EditText)findViewById(R.id.serverText);
				EditText portEditText = (EditText)findViewById(R.id.editPort);
				Button connectButton = (Button)findViewById(R.id.connectButton);
				String server = serverEditText.getText().toString();
				port1 = Integer.parseInt(portEditText.getText().toString());
				login = ((EditText)findViewById(R.id.editLoginText)).getText().toString();
				pass = ((EditText)findViewById(R.id.editPasswordText)).getText().toString();
				//resultText.setText();
				if (MQTTUtils.onrecive(server)) {
				//if (MQTTUtils.connect(server)) {
					connectButton.setEnabled(false);
					serverEditText.setEnabled(false);
					sendButton.setEnabled(true);
					resultText.setText("Connected to the server.");
				} else {
					resultText.setText("Error connecting the server.");
				}

			}

		});



			//andypiper_MQTT connection = new andypiper_MQTT();
			//boolean isConnected = connection.client.isConnected();

		//Button send = (Button)findViewById(R.id.sendButton);
		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
				LocationListener locationListener = new LocationListener() {

					@Override
					public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
					}

					@Override
					public void onProviderEnabled(String arg0) {
					}

					@Override
					public void onProviderDisabled(String arg0) {
					}

					@Override
					public void onLocationChanged(Location arg0) {
					}
				};
				TextView resultText = (TextView)findViewById(R.id.resultTextView);
				String provider = LocationManager.GPS_PROVIDER;
				if (!lm.isProviderEnabled(provider)) {
					provider = LocationManager.NETWORK_PROVIDER;
				}
				if (!lm.isProviderEnabled(provider)) {
					resultText.setText("Providers disabled.");
				} else {
					lm.requestSingleUpdate(provider, locationListener, Looper.getMainLooper());
					Location location = lm.getLastKnownLocation(provider);
					Double lon = 0.0;
					Double lat = 0.0;
					try {
						lon = location.getLongitude();
						lat = location.getLatitude();
					} catch (Exception e) {
						e.printStackTrace();
					}
					String topic = "d1";
					String payload = String.format("{lon: %f, lat: %f}", lon, lat);
					MQTTUtils.pub(topic, payload);
					resultText.setText(String.format("Topic: %s, message: %s", topic, payload));
				}
			}
		});
	}
	private View.OnClickListener mCorkyListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId() /*to get clicked view id**/) {
				case connectButton2:
					String[] args = new String[] {
							"-m","message",
							"-a","subscribe",
							"-t","topic"
					};

					andypiper_MQTT.main(args);
					break;
				case connectButton3:
					String[] args2 = new String[] {
							"-m","message",
							"-a","publish",
							"-t","topic"
					};

					andypiper_MQTT.main(args2);
					break;
				case onStart:
						startIntent();
					break;
				case buttonSubscribe:

						subscribe_int();
					break;
				case publish_btn:
					publish_int();
					break;
				case btn_stop:
					stop_int();
					break;
			}
		}

		;
	};

	private void stop_int() {
		Intent intent;
		intent = new Intent(this, MQTT_Service_depricated.class).setAction("MqttService.STOP");
		// стартуем сервис
		startService(intent);
	}

	private void publish_int() {
		Intent intent;
			intent = new Intent(this, MQTT_Service_depricated.class).setAction(MQTT_Service_depricated.ACTION_PUBLISH);
		// стартуем сервис
		startService(intent);
		//MQTT_Service_depricated.actionKeepalive(getApplicationContext());
	}

	private void subscribe_int() {
		Intent intent;
		intent = new Intent(this, MQTT_Service_depricated.class).setAction(MQTT_Service_depricated.ACTION_SUBSCRIBE);
		// стартуем сервис
		startService(intent);
	}

	private void startIntent() {
		Intent intent;
		intent = new Intent(this, MQTT_Service_depricated.class).setAction("MqttService.START");
		// стартуем сервис
		startService(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
