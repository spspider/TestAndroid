package com.caplab.iot.capmqtt;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Device> deviceList = null;
	private ArrayList<Device> arrayList;

	public ListViewAdapter(Context context, List<Device> deviceList) {
		mContext = context;
		this.deviceList = deviceList;
		inflater = LayoutInflater.from(mContext);
		this.arrayList = new ArrayList<Device>();
		this.arrayList.addAll(deviceList);
	}

	public class ViewHolder {
		TextView id;
		TextView value;
		TextView function;
	}
	
	public void add(Device dv) {
		arrayList.add(dv);
	}
	
	public void notifychange() {
		notifyDataSetChanged();
	}
	
	public void clear() {
		arrayList.clear();
	}

	@Override
	public int getCount() {
		return deviceList.size();
	}

	@Override
	public Device getItem(int position) {
		return deviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listview_item, null);
			// Locate the TextViews in listview_item.xml
			holder.id = (TextView) view.findViewById(R.id.id);
			holder.value = (TextView) view.findViewById(R.id.value);
			holder.function = (TextView) view.findViewById(R.id.function);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Set the results into TextViews
		holder.id.setText(deviceList.get(position).getId());
		holder.value.setText(deviceList.get(position).getValue());
		holder.function.setText(deviceList.get(position).getFunction());

		// Listen for ListView Item Click
		/*view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, SingleItemView.class);
				intent.putExtra("id",(deviceList.get(position).getId()));
				intent.putExtra("value",(deviceList.get(position).getValue()));
				intent.putExtra("function",(deviceList.get(position).getFunction()));
				// Start SingleItemView Class
				mContext.startActivity(intent);
			}
		});*/

		return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		deviceList.clear();
		if (charText.length() == 0) {
			deviceList.addAll(arrayList);
		} 
		else 
		{
			for (Device dv : arrayList) 
			{
				if (dv.getId().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					deviceList.add(dv);
				}
			}
		}
		notifyDataSetChanged();
	}

}