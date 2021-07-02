package com.caplab.iot.capmqtt;


import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScenarioListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Scenario> scenarioList = null;
	private ArrayList<Scenario> arrayList;

	public ScenarioListViewAdapter(Context context, List<Scenario> scenarioList) {
		mContext = context;
		this.scenarioList = scenarioList;
		inflater = LayoutInflater.from(mContext);
		this.arrayList = new ArrayList<Scenario>();
		this.arrayList.addAll(scenarioList);
	}

	public class ViewHolder {
		TextView id;
		TextView name;
		TextView contents;
	}
	
	public void add(Scenario sc) {
		arrayList.add(sc);
	}
	
	public void notifychange() {
		scenarioList.clear();
		scenarioList.addAll(arrayList);
		notifyDataSetChanged();
	}
	
	public void remove(int position) {
		arrayList.remove(position);
		scenarioList.clear();
		scenarioList.addAll(arrayList);
		notifyDataSetChanged();
	}
	
	public void clear() {
		arrayList.clear();
	}

	@Override
	public int getCount() {
		return scenarioList.size();
	}

	@Override
	public Scenario getItem(int position) {
		return scenarioList.get(position);
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
			view = inflater.inflate(R.layout.scenariolistview_item, null);
			// Locate the TextViews in listview_item.xml
			holder.id = (TextView) view.findViewById(R.id.id);
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.contents = (TextView) view.findViewById(R.id.contents);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Set the results into TextViews
		holder.id.setText(scenarioList.get(position).getId());
		holder.name.setText(scenarioList.get(position).getName());
		holder.contents.setText(scenarioList.get(position).getContents());

		return view;
	}

	

}