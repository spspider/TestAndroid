package com.androidexample.globalvariable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SecondScreen extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secondscreen); 
    
		TextView showGlobal     = (TextView) findViewById(R.id.showGlobal);
		final Button thirdBtn = (Button) findViewById(R.id.third);
		
		// Calling Application class (see application tag in AndroidManifest.xml)
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		
		// Get name and email from global/application context
		final String name  = globalVariable.getName();
		final String email = globalVariable.getEmail();
		
		String showString = "\n\nName : "+name+"\n"+
		                    "Email : "+email+"\n\n";
		
		// Show name/email values in TextView
		showGlobal.setText(showString);
		
		thirdBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				Intent i = new Intent(getBaseContext(), ThirdScreen.class);
				startActivity(i);
			}
		});
		
	} 
	
	@Override
    protected void onDestroy() {
		
        super.onDestroy();
        
    }
}
