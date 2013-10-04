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
	TextView textName , textPass;
	SharedPreferences preferences ;
	String PREFS_NAME = "com.onemore.karungguniapp";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.afterlogin); 
        textName = (TextView)findViewById(R.id.textname);
        textPass = (TextView)findViewById(R.id.textpass);
        reset = (Button)findViewById(R.id.button2);
        
       
		preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String uname = getIntent().getExtras().getString("EMAIL");
        String upass = getIntent().getExtras().getString("PASSWORD");

 
        
        textName.setText("Username : " + uname);
        textPass.setText("Password : " + upass);
        
        reset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				SharedPreferences.Editor editor = preferences.edit();
				editor.clear();
				editor.commit();
				
				Intent intent = new Intent(AfterLogin.this,Main.class);
				startActivity(intent);
				
				
			}
		});
        
	}
}
