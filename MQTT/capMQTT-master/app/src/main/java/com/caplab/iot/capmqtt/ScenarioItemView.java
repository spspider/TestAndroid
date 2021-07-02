package com.caplab.iot.capmqtt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
 
public class ScenarioItemView extends Activity {
	// Declare Variables
	TextView txtid;
	TextView txtname;
	TextView txtcontents;
	String id;
	String name;
	String contents;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scenarioitemview);
		// Retrieve data from MainActivity on item click event
		Intent i = getIntent();
		// Get the results of id
		id = i.getStringExtra("id");
		// Get the results of name
		name = i.getStringExtra("name");
		// Get the results of contents
		contents = i.getStringExtra("contents");
 
		// Locate the TextViews in singleitemview.xml
		txtid = (TextView) findViewById(R.id.id);
		txtname = (TextView) findViewById(R.id.name);
		txtcontents = (TextView) findViewById(R.id.contents);
 
		// Load the results into the TextViews
		txtid.setText(id);
		txtname.setText(name);
		txtcontents.setText(contents);
	}
}