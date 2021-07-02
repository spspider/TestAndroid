package com.caplab.iot.capmqtt;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Fragment_2 extends Fragment {

	private View rootView;
	EditText editsearch;
	ArrayList<Device> deviceList = new ArrayList<Device>();

	public static ListViewAdapter deviceListAdapter = null;
	public static ListView deviceListView = null;

	public Fragment_2() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_2, container, false);
		deviceListView = (ListView) rootView.findViewById(R.id.deviceListView);
		deviceListAdapter = new ListViewAdapter(getActivity(), deviceList); 
		deviceListAdapter.clear();
		deviceListView.setAdapter(deviceListAdapter);
				
		editsearch = (EditText) rootView.findViewById(R.id.search);
 
		editsearch.addTextChangedListener(new TextWatcher() {
 
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
				deviceListAdapter.filter(text);
			}
 
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}
 
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
		});
		
		
		Button commandButton = (Button) rootView.findViewById(R.id.buttonCommand);
		Button refreshButton = (Button) rootView.findViewById(R.id.buttonRefresh);
		deviceListAdapter.clear();
		deviceListView.setAdapter(deviceListAdapter);
		refreshButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				editsearch.setText("");
				Fragment_1.refreshFunction();
			}
		});

		commandButton.setOnClickListener(new OnClickListener()
		{
			EditText n = (EditText) rootView.findViewById(R.id.editTextName);
			EditText p = (EditText) rootView.findViewById(R.id.editTextPeriod);
			EditText c = (EditText) rootView.findViewById(R.id.editTextCondition);
			EditText a = (EditText) rootView.findViewById(R.id.editTextAction);		

			@Override
			public void onClick(View arg0)
			{   			
				String message = ""+n.getText().toString().trim()+"#["+p.getText().toString().trim()+"]("+c.getText().toString().trim()+"){"+a.getText().toString().trim()+"}";
				//[1h](light1.brightness > 50){*.playmusic}¡± }

				Bundle data = new Bundle();
				data.putCharSequence(MQTTservice.TOPIC, "command/scenario/add/"+Fragment_1.myClientID);	
				data.putCharSequence(MQTTservice.MESSAGE, message);
				Message msg = Message.obtain(null, MQTTservice.PUBLISH);
				msg.setData(data);
				msg.replyTo = Fragment_1.serviceHandler;
				try
				{
					Fragment_1.service.send(msg);
					Toast.makeText(getActivity(), message + " Sent to "+ Fragment_1.hostInput, Toast.LENGTH_SHORT).show();
				}
				catch (Exception e)
				{
					e.printStackTrace();    				
				}
			}
		});

		return rootView;
	}		

	@Override
	public void onStart()
	{
		super.onStart();
	}

	@Override
	public void onStop()
	{
		super.onStop();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if ( Fragment_1.pushReceiver != null ) {
			getActivity().registerReceiver(Fragment_1.pushReceiver, Fragment_1.intentFilter);
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		//getActivity().unregisterReceiver(Fragment_1.pushReceiver);
	}	
}
