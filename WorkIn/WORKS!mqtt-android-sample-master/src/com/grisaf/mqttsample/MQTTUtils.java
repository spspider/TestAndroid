package com.grisaf.mqttsample;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static com.grisaf.mqttsample.MainActivity.login;
import static com.grisaf.mqttsample.MainActivity.pass;
import static com.grisaf.mqttsample.MainActivity.port1;
import static com.grisaf.mqttsample.MainActivity.resultText;
import static java.security.AccessController.getContext;
//import static com.grisaf.mqttsample.MainActivity.resultText;


public class MQTTUtils {

	//Log.i(LOGTAG, "MQTT Start");

	private static Context context;
	/**
	 * Client handle to reference the connection that this handler is attached to
	 **/
	private static String clientHandle;
	private static IMqttAsyncClient mqttClient;
	private static MqttAsyncClient mqttClient2;
	//private static MqttClient client;

	public MQTTUtils(Context context, String clientHandle) {
		this.context = context;
		this.clientHandle = clientHandle;
	}

	 static boolean connect(String url) {

		IMqttToken token;

		//conOpt.setCleanSession(true);

		try {
			/*
			MemoryPersistence persistance = new MemoryPersistence();
			client = new MqttClient("tcp://" + url + ":" + port1, "client1",  persistance);
			MqttConnectOptions conOpt = new MqttConnectOptions();
			conOpt.setUserName(login);
			conOpt.setPassword(pass.toCharArray());
			client.connect(conOpt);
			*/

			MqttConnectOptions conOpt = new MqttConnectOptions();
			mqttClient = new MqttAsyncClient("tcp://" + url + ":" + port1, "client1", new MemoryPersistence());
			//conOpt.setCleanSession(true);
			conOpt.setUserName(login);
			conOpt.setPassword(pass.toCharArray());
			token = mqttClient.connect(conOpt);
			token.waitForCompletion(3500);
			mqttClient.setCallback(new MqttEventCallback(MainActivity.getInstance().getContext()));
			token = mqttClient.subscribe("d1", 0);
			token.waitForCompletion(5000);




			//client.connect(conOpt);
			//client.connect(null, new IMqttActionListener());
			//client.setCallback(new MQ_read(context, clientHandle));
			//sub();
			//client.subscribe("d1");
			//client.subscribe("d1");
			//MqttMessage message = new MqttMessage();
			return true;
		} catch (MqttException e) {
			e.printStackTrace();
	//		resultText.setText(e.getMessage());
			switch (e.getReasonCode()) {

				case MqttException.REASON_CODE_BROKER_UNAVAILABLE:
				case MqttException.REASON_CODE_CLIENT_TIMEOUT:
				case MqttException.REASON_CODE_CONNECTION_LOST:
				case MqttException.REASON_CODE_SERVER_CONNECT_ERROR:
					Log.v(TAG, "c" +e.getMessage());
					Toast.makeText(MainActivity.getInstance().getContext(),e.getMessage().toString(), Toast.LENGTH_LONG);
					e.printStackTrace();
					break;
				case MqttException.REASON_CODE_FAILED_AUTHENTICATION:
					//Intent i = new Intent("RAISEALLARM");
					//i.putExtra("ALLARM", e);
					Log.e(TAG, "b"+ e.getMessage());
					Toast.makeText(MainActivity.getInstance().getContext(),e.getMessage().toString(), Toast.LENGTH_LONG);
					break;
				default:
					Log.e(TAG, "a" + e.getMessage());
			}
		} catch (Exception e) {
	//		resultText.setText(e.getMessage());
			e.printStackTrace();
		}
		//default;
		return false;
	}


	static boolean pub(String topic, String payload) {
		MqttMessage message = new MqttMessage(payload.getBytes());
		try {
			if (mqttClient!=null){
			mqttClient.publish(topic, message);
			}
			//client.publish(topic, message);
			return true;
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////
static boolean sendMessage_connect(String url){
	//MqttAsyncClient mqttClient2 = null;
	try {
		mqttClient2 = new MqttAsyncClient("tcp://" + url + ":" + port1, "client1", new MemoryPersistence());
		MqttConnectOptions conOpt = new MqttConnectOptions();
		conOpt.setUserName(login);
		conOpt.setPassword(pass.toCharArray());
		mqttClient2.connect(conOpt, new IMqttActionListener() {
            @Override
			 public void onSuccess(IMqttToken asyncActionToken) {
                while (true) {
                    try {
						mqttClient2.publish("/register", "hello".getBytes(), 0, false);
                        System.out.println("Sent hello...");
                        TimeUnit.SECONDS.sleep(5);

                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
		return true;
	} catch (MqttException e) {
		e.printStackTrace();
	}
	return false;
}

static boolean onrecive(String url){

	//MqttAsyncClient client = null;
	try {
		mqttClient2 = new MqttAsyncClient("tcp://" + url + ":" + port1, "client1", new MemoryPersistence());
	} catch (MqttException e) {
		e.printStackTrace();
	}
	try {
		MqttConnectOptions conOpt = new MqttConnectOptions();
		conOpt.setUserName(login);
		conOpt.setPassword(pass.toCharArray());
		mqttClient2.connect(conOpt, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                try {
                mqttClient2.subscribe("d1", 0, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                        }
                    });


                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
		return true;
	} catch (MqttException e) {
		e.printStackTrace();
	}

	mqttClient2.setCallback(new MqttCallback() {
		@Override
		public void connectionLost(Throwable cause) {

		}

		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception {
			System.out.println("Received message on topic: " + topic + " -> " + new String(message.getPayload()));
	//		resultText.setText(new String(message.getPayload()));
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {

		}
	});
	return false;}
}


	/*
	public void doDemo() {
		try {
			client = new MqttClient("tcp://192.168.118.11:1883", "Sending");
			client.connect();
			client.setCallback(this);
			client.subscribe("foo");
			MqttMessage message = new MqttMessage();
			message.setPayload("A single message from my computer fff"
					.getBytes());
			client.publish("foo", message);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	*/

