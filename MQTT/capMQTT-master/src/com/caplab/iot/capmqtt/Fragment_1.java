package com.caplab.iot.capmqtt;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_1 extends Fragment {

	private View rootView;
	public static Messenger service = null;
	public static Messenger serviceHandler = null;
	public static IntentFilter intentFilter = null;

	public static String hostInput;
	public static int portInput;
	public static String myClientID;
	
	public static PushReceiver pushReceiver;
	
	public Fragment_1() {
		serviceHandler = new Messenger(new ServiceHandler());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_1, container, false);
		intentFilter = new IntentFilter();
		intentFilter.addAction("com.caplab.iot.capmqtt.PushReceived");
		pushReceiver = new PushReceiver();
		
		Button connectButton = (Button) rootView.findViewById(R.id.buttonConnect);

		connectButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(getActivity(), MQTTservice.class);
				EditText h = (EditText) rootView.findViewById(R.id.editTextHost);
				EditText p = (EditText) rootView.findViewById(R.id.editTextPort);
				hostInput = h.getText().toString().trim();
				portInput = Integer.parseInt(p.getText().toString());
				intent.putExtra("HOST", hostInput);
				intent.putExtra("PORT", portInput);
				getActivity().registerReceiver(pushReceiver, intentFilter, null, null);
				getActivity().getApplicationContext().startService(intent);
				Toast.makeText(getActivity(),"Connected to "+ hostInput,Toast.LENGTH_SHORT).show();
			}
		});
		return rootView;
	}	

	@Override
	public void onStart()
	{
		super.onStart();
		getActivity().bindService(new Intent(getActivity(), MQTTservice.class), serviceConnection, 0);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		getActivity().unbindService(serviceConnection);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if ( pushReceiver != null ) {
			getActivity().registerReceiver(pushReceiver, intentFilter);
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		getActivity().unregisterReceiver(pushReceiver);
	}	
	
	public class PushReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent i)
		{
			String message = i.getStringExtra(MQTTservice.MESSAGE);
			String topic = i.getStringExtra(MQTTservice.TOPIC);

			//message = message.substring(0, message.length()-1)+"";
			Log.d("message :","length: "+message.length()+", "+ message);
			Log.d("topic :",topic);
			
			try {
				JSONObject jsonObject = new JSONObject(message);	
				
				if(topic.equals("result/"+myClientID+"/device_list"))
				{
					JSONArray deviceInfoArray = (JSONArray) jsonObject.get("devices");
					Fragment_2.deviceListAdapter.clear();
				
					for(int k=0; k<deviceInfoArray.length(); k++){						 
						JSONObject deviceObject = (JSONObject) deviceInfoArray.get(k);
						Log.d("obj :",deviceObject.toString());
						Device dv = new Device( deviceObject.getString("id"), deviceObject.getString("values"), deviceObject.getString("functions") );
						Fragment_2.deviceListAdapter.add(dv);
					}
					Fragment_2.deviceListAdapter.notifychange();
					Fragment_2.deviceListAdapter.filter("");
				}
				if(topic.equals("result/"+myClientID+"/scenario_list") && Fragment_3.scenarioListAdapter!=null )
				{
					JSONArray scenarioInfoArray = (JSONArray) jsonObject.get("scenarios");
					Fragment_3.scenarioListAdapter.clear();
					Log.d("sc list length :",scenarioInfoArray.length()+"");
					for(int k=0; k<scenarioInfoArray.length(); k++){						 
						JSONObject scenarioObject = (JSONObject) scenarioInfoArray.get(k);
						Log.d("obj :",scenarioObject.toString());
						Scenario sc = new Scenario( scenarioObject.getString("id"), scenarioObject.getString("name"), scenarioObject.getString("contents") );
						Fragment_3.scenarioListAdapter.add(sc);
					}
					Fragment_3.scenarioListAdapter.notifychange();	
				}
			} catch (Exception e) {
				//Toast.makeText(getActivity(),"malformed JSON data", Toast.LENGTH_SHORT).show();
				Log.e("jsoned msg", e.toString());
			}		
		}
	}
	
	class ServiceHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case MQTTservice.SUBSCRIBE: 	break;
			case MQTTservice.PUBLISH:		break;
			case MQTTservice.REGISTER:		break;
			default:
				super.handleMessage(msg);
				return;
			}

			Bundle b = msg.getData();
			if (b != null)
			{
				Boolean status = b.getBoolean(MQTTservice.STATUS);
				if (status == false)
				{
					Toast.makeText(getActivity(),"CONNECTION FAIL",Toast.LENGTH_SHORT).show();
				}
				else
				{
					//Toast.makeText(getActivity(),"CONNECTION SUCCESS",Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	public ServiceConnection serviceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binder)
		{
			myClientID = MQTTservice.myClientID.toString();				
			Log.d("myClientID : ", myClientID);
			TextView result = (TextView) rootView.findViewById(R.id.textConnectStatus);
			service = new Messenger(binder);
			Bundle data = new Bundle();
			//data.putSerializable(MQTTservice.CLASSNAME, MainActivity.class);
			data.putCharSequence(MQTTservice.INTENTNAME, "com.caplab.iot.capmqtt.PushReceived");
			Message msg = Message.obtain(null, MQTTservice.REGISTER);
			msg.setData(data);
			msg.replyTo = serviceHandler;
			try
			{
				service.send(msg);
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
			
			if (hostInput.isEmpty()==false)
			{
				data = new Bundle();
				data.putCharSequence(MQTTservice.TOPIC, "result/"+Fragment_1.myClientID+"/#");	
				msg = Message.obtain(null, MQTTservice.SUBSCRIBE);
				msg.setData(data);
				msg.replyTo = serviceHandler;
				Log.d("msg equals : ", "before");
				try
				{
					service.send(msg);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}					
			}
			else
			{
				result.setText("Host and Port required.");
				Toast.makeText(getActivity(),"Host and Port required.",Toast.LENGTH_SHORT).show();
			}
			
			String topic = "refresh/client/"+myClientID;
			String message = "I'm sorry";
			
			result.setText("");
			data = new Bundle();
			data.putCharSequence(MQTTservice.TOPIC, topic);			// topic	: refresh
			data.putCharSequence(MQTTservice.MESSAGE, message);		// message	: not neccesary 
			msg = Message.obtain(null, MQTTservice.PUBLISH);
			msg.setData(data);
			msg.replyTo = serviceHandler; 
			try
			{
				service.send(msg);
				//result.setText("Connected to "+ hostInput);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				//result.setText("Connect failed with exception:" + e.getMessage());
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0)
		{
		}
	};
	
	public static void refreshFunction(){
		Bundle data = new Bundle();
		data.putCharSequence(MQTTservice.TOPIC, "refresh/client/"+myClientID);
		data.putCharSequence(MQTTservice.MESSAGE, "refreshFunction");
		Message msg = Message.obtain(null, MQTTservice.PUBLISH);
		msg.setData(data);
		msg.replyTo = serviceHandler;
		try
		{
			service.send(msg);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}				
		return;
	}
}
