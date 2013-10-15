package com.onemore.karungguniapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

import com.facebook.android.AsyncFacebookRunner.*;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.*;
import com.facebook.android.FacebookError;
import com.facebook.widget.LoginButton;

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


public static String APP_ID = "**************";
// Instance of Facebook Class
 private Facebook facebook = new Facebook(APP_ID);
 private AsyncFacebookRunner mAsyncRunner;
 String FILENAME = "AndroidSSO_data";
 private SharedPreferences mPrefs;


    
	Button login;

	Button signup;

	SignInButton google;
	Button btnfacebook;
	String PREFS_NAME = "com.onemore.karungguniapp";
	String role;
//	SharedPreferences preferences ;
	
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
		
		mPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	//	if (preferences.getString("logged", "").toString().equals("logged")) 
		//{
			//Intent i = new Intent(Main.this,AfterLogin.class);
			//i.putExtra("EMAIL",preferences.getString("email", "").toString());
			//i.putExtra("PASSWORD",preferences.getString("password", "").toString());
		
//			 startActivity(i);
			
	//	}
		
		
		btnfacebook = (LoginButton) findViewById(R.id.fbbtn);
		
		btnfacebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	
            	
            	
            	 LayoutInflater layoutInflater 
                 = (LayoutInflater)getBaseContext()
                  .getSystemService(LAYOUT_INFLATER_SERVICE);  
                View popupView = layoutInflater.inflate(R.layout.role, null);  
                         final PopupWindow popupWindow = new PopupWindow(
                           popupView, 
                           LayoutParams.WRAP_CONTENT,  
                                 LayoutParams.WRAP_CONTENT);  
                         
                         Button kg = (Button)popupView.findViewById(R.id.kg);
                        kg.setOnClickListener(new Button.OnClickListener(){

                 @Override
                 public void onClick(View v) {
                	 role="KG";
                	 
                  // TODO Auto-generated method stub
                  popupWindow.dismiss();
                 }});
                           
            Button seller = (Button)popupView.findViewById(R.id.seller);
                        seller.setOnClickListener(new Button.OnClickListener(){

                 @Override
                 public void onClick(View v) {
                  // TODO Auto-generated method stub
                	 role="seller";
                  popupWindow.dismiss();
                 }});
                       
            	
         	
            	
             Log.d("Image Button", "button Clicked");
             loginToFacebook();

            }
           });
		
		
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
	  /**
     * Function to login into facebook
     * */
    @SuppressWarnings("deprecation")
    public void loginToFacebook() {
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
     
        if (access_token != null) {
            facebook.setAccessToken(access_token);
            
            Intent i = new Intent();
            if (role.equals("Seller")){
//				i = new Intent(getBaseContext(), <Seller>.class);
			}
			else if (role.equals("KG")){
//				i = new Intent(getBaseContext(), <KG>.class);
			}
//			
            startActivity(i);
            
        }
     
        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }
     
        if (!facebook.isSessionValid()) {
            facebook.authorize(this,
                    new String[] { "email", "publish_stream" },
                    new DialogListener() {
     
                        @Override
                        public void onCancel() {
                            // Function to handle cancel event
                        }
     
                        @Override
                        public void onComplete(Bundle values) {
                            // Function to handle complete event
                            // Edit Preferences and update facebook acess_token
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("access_token",
                                    facebook.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook.getAccessExpires());
                            editor.commit();
                        }
     
                        @Override
                        public void onError(DialogError error) {
                            // Function to handle error
     
                        }
     
                        @Override
                        public void onFacebookError(FacebookError fberror) {
                            // Function to handle Facebook errors
     
                        }
     
                    });
        }
    }
    
  
    
    @SuppressWarnings("deprecation")
	public void getProfileInformation() {
        mAsyncRunner.request("me", new RequestListener() {
            @Override
            public void onComplete(String response, Object state) {
                Log.d("Profile", response);
                String json = response;
                try {
                    JSONObject profile = new JSONObject(json);
                    // getting name of the user
                    final String name = profile.getString("name");
                    // getting email of the user
                    final String email = profile.getString("email");
     
                    runOnUiThread(new Runnable() {
     
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Name: " + name + "\nEmail: " + email, Toast.LENGTH_LONG).show();
                        }
     
                    });
     
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
     
            @Override
            public void onIOException(IOException e, Object state) {
            }
     
            @Override
            public void onFileNotFoundException(FileNotFoundException e,
                    Object state) {
            }
     
            @Override
            public void onMalformedURLException(MalformedURLException e,
                    Object state) {
            }
     
            @Override
            public void onFacebookError(FacebookError e, Object state) {
            }
        });
    }
    
    
    
    

	
	@Override
	 public void onClick(View view) {
		
		
		
    	 LayoutInflater layoutInflater 
         = (LayoutInflater)getBaseContext()
          .getSystemService(LAYOUT_INFLATER_SERVICE);  
        View popupView = layoutInflater.inflate(R.layout.role, null);  
                 final PopupWindow popupWindow = new PopupWindow(
                   popupView, 
                   LayoutParams.WRAP_CONTENT,  
                         LayoutParams.WRAP_CONTENT);  
                 
                 Button kg = (Button)popupView.findViewById(R.id.kg);
                kg.setOnClickListener(new Button.OnClickListener(){

         @Override
         public void onClick(View v) {
        	 role="KG";
        	 
          // TODO Auto-generated method stub
          popupWindow.dismiss();
         }});
                   
    Button seller = (Button)popupView.findViewById(R.id.seller);
                seller.setOnClickListener(new Button.OnClickListener(){

         @Override
         public void onClick(View v) {
          // TODO Auto-generated method stub
        	 role="seller";
          popupWindow.dismiss();
         }});
               
		
		
		
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
