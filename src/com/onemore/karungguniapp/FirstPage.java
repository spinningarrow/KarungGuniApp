package com.onemore.karungguniapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FirstPage extends Activity{
	Button login,signup;
	String PREFS_NAME = "com.example.sp.LoginPrefs";
	SharedPreferences preferences ;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.first);
		login = (Button)findViewById(R.id.button2);
		signup = (Button)findViewById(R.id.button1);
		
		preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		if (preferences.getString("logged", "").toString().equals("logged")) 
		{
			//Intent i = new Intent(FirstPage.this,AfterLogin.class);
		//	i.putExtra("EMAIL",preferences.getString("email", "").toString());
			//i.putExtra("PASSWORD",preferences.getString("password", "").toString());
		//	i.putExtra("CHECK", true);
		//(i);
			
		}
		
	login.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
					Intent i = new Intent(FirstPage.this,LoginActivity.class);
					
					startActivity(i);	
			
				
			}
		});
		
	signup.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		
				Intent i = new Intent(FirstPage.this,SignupActivity.class);
				
			startActivity(i);	
		
			
		}
	});	
		
	}
}
