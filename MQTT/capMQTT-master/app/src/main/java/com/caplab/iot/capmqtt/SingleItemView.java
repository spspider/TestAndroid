package com.caplab.iot.capmqtt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
 
public class SingleItemView extends Activity {
	// Declare Variables
	TextView txtid;
	TextView txtvalue;
	TextView txtfunction;
	String id;
	String value;
	String function;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singleitemview);
		// Retrieve data from MainActivity on item click event
		Intent i = getIntent();
		// Get the results of id
		id = i.getStringExtra("id");
		// Get the results of value
		value = i.getStringExtra("value");
		// Get the results of function
		function = i.getStringExtra("function");
 
		// Locate the TextViews in singleitemview.xml
		txtid = (TextView) findViewById(R.id.id);
		txtvalue = (TextView) findViewById(R.id.value);
		txtfunction = (TextView) findViewById(R.id.function);
 
		// Load the results into the TextViews
		txtid.setText(id);
		txtvalue.setText(value);
		txtfunction.setText(function);
	}
}