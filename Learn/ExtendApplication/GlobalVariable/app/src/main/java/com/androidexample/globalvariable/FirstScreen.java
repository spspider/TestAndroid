package com.androidexample.globalvariable;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class FirstScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstscreen);
		
		final Button secondBtn = (Button) findViewById(R.id.second);
		
		// Calling Application class (see application tag in AndroidManifest.xml)
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		
		//Set name and email in global/application context
		globalVariable.setName("Android Example context variable");
		globalVariable.setEmail("xxxxxx@aaaa.com");
		
		
		secondBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				Intent i = new Intent(getBaseContext(), SecondScreen.class);
				startActivity(i);
			}
		});	
	}

	
}
