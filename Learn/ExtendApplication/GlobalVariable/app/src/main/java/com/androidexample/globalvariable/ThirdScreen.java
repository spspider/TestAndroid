package com.androidexample.globalvariable;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ThirdScreen extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thirdscreen); 
    
		TextView showGlobal     = (TextView) findViewById(R.id.showGlobal);
		
		// Calling Application class (see application tag in AndroidManifest.xml)
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		
		// Get name and email from global/application context
		final String name  = globalVariable.getName();
		final String email = globalVariable.getEmail();
		
		String showString = "\n\nName : "+name+"\n"+
        "Email : "+email+"\n\n";
		
		// Show name/email values in TextView
		showGlobal.setText(showString);
		
		
	} 
	
}
