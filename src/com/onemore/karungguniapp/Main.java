package com.onemore.karungguniapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;



public class Main extends Activity implements  OnClickListener,
ConnectionCallbacks, OnConnectionFailedListener {
	
private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;

private static final int REQUEST_CODE_SIGN_IN = 1;
private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;

private TextView mSignInStatus;



private ProgressDialog mConnectionProgressDialog;
private PlusClient mPlusClient;
private ConnectionResult mConnectionResult;
	Button login;

	Button signup;

	SignInButton google;
	String PREFS_NAME = "com.onemore.karungguniapp";
	SharedPreferences preferences ;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPlusClient = new PlusClient.Builder(this, this, this)
        .setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
        .build();
		
		mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
		
		setContentView(R.layout.main);
		
		login = (Button) findViewById(R.id.signin);
		signup =(Button) findViewById(R.id.signup);
		
		preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	//	if (preferences.getString("logged", "").toString().equals("logged")) 
		//{
			//Intent i = new Intent(Main.this,AfterLogin.class);
			//i.putExtra("EMAIL",preferences.getString("email", "").toString());
			//i.putExtra("PASSWORD",preferences.getString("password", "").toString());
		
//			 startActivity(i);
			
	//	}
		
		findViewById(R.id.google).setOnClickListener(this);

		
	login.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
					Intent i = new Intent(Main.this,LoginActivity.class);
					
					startActivity(i);	
			
				
			}
		});
		
	signup.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		
				Intent i = new Intent(Main.this,SignupActivity.class);
				
			startActivity(i);	
		
			
		}
	});	
		
		
	}

	
	@Override
	 public void onClick(View view) {
        if (view.getId() == R.id.google && !mPlusClient.isConnected()) {
            if (mConnectionResult == null) {
                mConnectionProgressDialog.show();
            } else {
                try {
                    mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (SendIntentException e) {
                    // Try connecting again.
                    mConnectionResult = null;
                    mPlusClient.connect();
                }
            }
        }
    }
	 @Override
	    protected void onStart() {
	        super.onStart();
	        mPlusClient.connect();
	    }

	    @Override
	    protected void onStop() {
	        super.onStop();
	        mPlusClient.disconnect();
	    }
	
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		  if (mConnectionProgressDialog.isShowing()) {
              // The user clicked the sign-in button already. Start to resolve
              // connection errors. Wait until onConnected() to dismiss the
              // connection dialog.
              if (result.hasResolution()) {
                      try {
                              result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                      } catch (SendIntentException e) {
                              mPlusClient.connect();
                      }
              }
      }

      // Save the intent so that we can start an activity when the user clicks
      // the sign-in button.
      mConnectionResult = result;
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
		 Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		
		 mConnectionProgressDialog.dismiss();
		 if (mPlusClient.getCurrentPerson() != null) {
			    String email = mPlusClient.getAccountName();
		        Person currentPerson = mPlusClient.getCurrentPerson();
		        String personName = currentPerson.getDisplayName();
		       
		    }
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
	
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
	    if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
	        mConnectionResult = null;
	        mPlusClient.connect();
	    }
	}
	
}
