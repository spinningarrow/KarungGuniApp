package com.onemore.karungguniapp;

import java.io.ByteArrayInputStream;
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
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

import com.facebook.*;
import com.facebook.android.*;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.internal.*;
import com.facebook.model.*;
import com.facebook.widget.*;




public class Main extends Activity implements  View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
	
private static final int REQUEST_CODE_RESOLVE_ERR = 9000;



private ProgressDialog signingIn;

private ProgressDialog mConnectionProgressDialog;
private PlusClient mPlusClient;
private ConnectionResult mConnectionResult;
private SharedPreferences fbPrefs;

public static String APP_ID = "521174844642024";
// Instance of Facebook Class
 private Facebook facebook = new Facebook(APP_ID);

 
 private Bundle mPrefs;

 private GraphUser user;
    
	Button login;

	Button signup;

	SignInButton google;
	LoginButton btnfacebook;
	String role;
	
	static boolean chosen=false;
	


	 /*     private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.d("Facebook", String.format("Error: %s", error.toString()));
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.d("Facebook", "Success!");
	        }
	    };
	  private void updateUI() {
	        Session session = Session.getActiveSession();
	      

	    

	        if (session != null && user != null) {
	           
	        	 String displayName=user.getUsername();
            	 String email=displayName+"@facebook.com";

                   // Create a new user with the supplied details
            	 
           //      AccountManager.setCurrentUser(getApplicationContext(), email, null);
                              
	        }
	      
	    }*/
	
	  
	
	
	
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
		
		mPrefs = AccountManager.getCurrentUser(getApplicationContext());
		if (mPrefs != null) 
		{
			if (mPrefs.get("role").equals(AppData.ROLE_KG)){
				Intent i = new Intent(Main.this,KarungGuniActivity.class);
				startActivity(i);
			}
			else if (mPrefs.get("role").equals(AppData.ROLE_SELLER)){
				Intent i = new Intent(Main.this,SellerActivity.class);
				startActivity(i);
			}else if  (mPrefs.get("role") == null){
			
				
				
			///////////method add role to the user in db 
			///////
			///////
				
				
				
			
				
			//	 showPopup(Main.this) ;
				//AccountManager.setCurrentUser(getApplicationContext(), mPrefs.get("email").toString(), role);
				
				
			}
		}
		 fbPrefs = getPreferences(MODE_PRIVATE);
	      String access_token = fbPrefs.getString("access_token", null);
	      long expires = fbPrefs.getLong("access_expires", 0);

	      if (access_token != null) {
	       facebook.setAccessToken(access_token);
	       Intent i = new Intent();
	        i.setClass(Main.this, SellerActivity.class);
	        startActivity(i);
	      }
		btnfacebook = (LoginButton) findViewById(R.id.fbbtn);

		

		

		btnfacebook.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
         	
        	        	
             FacebookUtil.login(Main.this,
                     new Session.StatusCallback() {

                         @Override
                         public void call(Session _session, SessionState _state, Exception _exception) {
                             if(_session.isOpened()) {
                             FacebookUtil.askMe(new Request.GraphUserCallback() {
                                 public void onCompleted(GraphUser user, Response response) {
                                     if (user != null) {
                                    	 
                                    	 String displayName=user.getUsername();
                                    	 String email=displayName+"@facebook.com";
                

                                           // Create a new user with the supplied details
                                         //
                           		           //                       	 
                                    	 //
                                    	 //
                                    	 
                                    	 
                                      AccountManager.setCurrentUser(getApplicationContext(), email, null);
                                       
                                    
                                    	
                                  	 

                                     }

                                 }
                             }); }
                         }
                     });
        
          
          
          
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
	
	@Override
	 public void onClick(View view) {
		
		
		showPopup(Main.this);
		
		
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
	
	 public void onResume(){
		 super.onResume();
		
				
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
		        String displayName = currentPerson.getDisplayName();
		       
		        
		        
		        
                Handler.Callback createWithEmailCallback = new Handler.Callback() {
                    Bundle result;
                    JSONObject user;
//                    Uri uri;

                    @Override
                    public boolean handleMessage(Message message) {
                        result = message.getData();

                        if (result.getInt("success") != 1 || result.getInt("status") != 201) {
                            // Dismiss the progress dialog
                            signingIn.dismiss();
                            Log.w("ACCOUNT_MANAGER", "Insert role table error occurred");

                            // Show an error to the user if a user with that email address already exists
                            if (result.getInt("status") == 409) {
                                Toast toast = Toast.makeText(getApplicationContext(), R.string.signup_user_exists, Toast.LENGTH_LONG);
                                toast.show();
                            }
                            return false;
                        }

                        try {
                            user = RestClient.parseJsonObject(new ByteArrayInputStream(result.getString("response").getBytes("UTF-8")));

                            // Set the current user in the Shared Preferences so it can be used by other activities
                            AccountManager.setCurrentUser(getApplicationContext(), user.getString("email"), role);
                            Intent intent = null;

                            if (role.equals(AppData.ROLE_KG)) {
                                intent = new Intent(getBaseContext(), KarungGuniActivity.class);
                            }

                            else if (role.equals(AppData.ROLE_SELLER)) {
                                intent = new Intent(getBaseContext(), SellerActivity.class);
                            }

                            // Dismiss the progress dialog and start the new activity
                            signingIn.dismiss();
                            startActivity(intent);

                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            return false;
                        }

                        catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }

                        return true;
                    }
                };
		        
                // Show a progress dialog to the user
                signingIn = ProgressDialog.show(this, getString(R.string.signup_progress_title), getString(R.string.signup_progress_message), true);

                // Create a new user with the supplied details
                AccountManager.createWithEmail(email, "", role, displayName, createWithEmailCallback);
		        
		        
		        
		        
		        
		        
		        
		        
		    }
		 
		 
		 
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
	    if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
	        mConnectionResult = null;
	        mPlusClient.connect();
	    } else{
	    	 super.onActivityResult(requestCode, responseCode, intent);
	 	    FacebookUtil.onActivityResult(this, requestCode, responseCode, intent);
	 	}
	    }
	}*/
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
	    super.onActivityResult(_requestCode, _resultCode, _data);
	    FacebookUtil.onActivityResult(this, _requestCode, _resultCode, _data);
	    
	    
	    
	}

	private boolean showPopup(final Activity context) {
		   int popupWidth = 400;
		   int popupHeight = 350;
		  

		   // Inflate the popup_layout.xml
		   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
		   LayoutInflater layoutInflater = (LayoutInflater) context
		     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   View layout = layoutInflater.inflate(R.layout.role, viewGroup);

		   // Creating the PopupWindow
		   final PopupWindow popup = new PopupWindow(context);
		   popup.setContentView(layout);
		   popup.setWidth(popupWidth);
		   popup.setHeight(popupHeight);
		   popup.setFocusable(true);

		   // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
		   int OFFSET_X = 50;
		   int OFFSET_Y = 150;

		   // Clear the default translucent background
		   popup.setBackgroundDrawable(new BitmapDrawable());

		   // Displaying the popup at the specified location, + offsets.
		   popup.showAtLocation(layout, Gravity.NO_GRAVITY,  OFFSET_X,OFFSET_Y);

		   Button kg = (Button)layout.findViewById(R.id.kg);
           kg.setOnClickListener(
        		   new Button.OnClickListener()
        		   {

    @Override
    public void onClick(View v) {
   	 role=AppData.ROLE_KG;
   	 chosen=true;
     // TODO Auto-generated method stub
   	popup.dismiss();
   
    }
    }
         
        		   );
              
Button seller = (Button)layout.findViewById(R.id.seller);
           seller.setOnClickListener(new Button.OnClickListener(){

    @Override
    public void onClick(View v) {
     // TODO Auto-generated method stub
   	 role=AppData.ROLE_SELLER;
   	 chosen=true;
   	popup.dismiss();
    }});
           return chosen;
           }
}

