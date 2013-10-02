package com.onemore.karungguniapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AfterLogin extends Activity {
	
	Button reset;
	TextView textEmail , textPass;
	SharedPreferences preferences ;
	String PREFS_NAME = "com.example.sp.LoginPrefs";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.afterlogin); 
        textEmail = (TextView)findViewById(R.id.textemail);
        textPass = (TextView)findViewById(R.id.textpass);
        reset = (Button)findViewById(R.id.button2);
        
       
		preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String uemail = getIntent().getExtras().getString("EMAIL");
        String upass = getIntent().getExtras().getString("PASSWORD");
        boolean check = getIntent().getExtras().getBoolean("CHECK");
        
        
        if(!check)
        {
        	reset.setText("Back");
        }
        
        textEmail.setText("Username : " + uemail);
        textPass.setText("Password : " + upass);
        
        reset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				SharedPreferences.Editor editor = preferences.edit();
				editor.clear();
				editor.commit();
				
				Intent intent = new Intent(AfterLogin.this,LoginActivity.class);
				startActivity(intent);
				
				
			}
		});
        
	}
}
