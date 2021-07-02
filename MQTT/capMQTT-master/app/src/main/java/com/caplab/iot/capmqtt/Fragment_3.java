package com.caplab.iot.capmqtt;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Fragment_3 extends Fragment {

	private View rootView;

	ArrayList<Scenario> scenarioList = new ArrayList<Scenario>();

	public static ScenarioListViewAdapter scenarioListAdapter = null;
	public static ListView scenarioListView = null;
	public Fragment_3() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_3, container, false);
		//Button removeButton = (Button) rootView.findViewById(R.id.buttonScenario);

		Bundle data = new Bundle();
		data.putCharSequence(MQTTservice.TOPIC, "result/"+Fragment_1.myClientID+"/#");	
		Message msg = Message.obtain(null, MQTTservice.SUBSCRIBE);
		msg.setData(data);
		msg.replyTo = Fragment_1.serviceHandler;
		try
		{
			Fragment_1.service.send(msg);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		scenarioListAdapter = new ScenarioListViewAdapter(getActivity(),scenarioList);
		scenarioListView = (ListView) rootView.findViewById(R.id.scenarioListView);
		Button refreshButton = (Button) rootView.findViewById(R.id.buttonRefresh);
		refreshButton.setOnClickListener(new OnClickListener()
		{
			AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
			@Override
			public void onClick(View arg0)
			{
				Fragment_1.refreshFunction();				
				scenarioListView.setAdapter(scenarioListAdapter);
				Fragment_3.scenarioListAdapter.notifychange();	
				SwipeDismissListViewTouchListener touchListener =
						new SwipeDismissListViewTouchListener(scenarioListView,
								new SwipeDismissListViewTouchListener.DismissCallbacks() {
							@Override
							public boolean canDismiss(int position) {
								return true;
							}
							@Override
							public void onDismiss(ListView listView, int[] reverseSortedPositions) {
								for (final int position : reverseSortedPositions) {

									alert_confirm.setMessage("Are you sure to stop this scenario? \nThis Action cannot be undone.").setCancelable(false).setPositiveButton("OK",
											new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {

											String message = scenarioListAdapter.getItem(position).getId();
											//scenario id 

											Bundle data = new Bundle();
											data.putCharSequence(MQTTservice.TOPIC, "command/scenario/delete/"+Fragment_1.myClientID);	
											data.putCharSequence(MQTTservice.MESSAGE, message);
											Message msg = Message.obtain(null, MQTTservice.PUBLISH);
											msg.setData(data);
											msg.replyTo = Fragment_1.serviceHandler;
											try
											{
												Fragment_1.service.send(msg);
												scenarioListAdapter.remove(position);
											}
											catch (Exception e)
											{
												e.printStackTrace();    				
											}
										}
									}).setNegativeButton("Cancel",
											new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Toast.makeText(getActivity(),"remove canceled", Toast.LENGTH_SHORT).show();
											return;
										}
									});
									AlertDialog alert = alert_confirm.create();
									alert.show();
								}
								scenarioListAdapter.notifyDataSetChanged();
							}
						});
				scenarioListView.setOnTouchListener(touchListener);
				scenarioListView.setOnScrollListener(touchListener.makeScrollListener());
			}
		});

		/* String[] items = new String[20];
        for (int i = 0; i < items.length; i++) {
            items[i] = "Item " + (i + 1);
        }

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text2,
                new ArrayList<String>(Arrays.asList(items)));
        mListView.setAdapter(mAdapter);
		 */

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
		if ( Fragment_1.pushReceiver == null ) {
			getActivity().registerReceiver(Fragment_1.pushReceiver, Fragment_1.intentFilter);
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}	
}
