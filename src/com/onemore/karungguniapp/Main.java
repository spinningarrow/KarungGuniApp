package com.onemore.karungguniapp;

import android.accounts.Account;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public class Main extends Activity implements OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener {

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;

    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;


    private ProgressDialog signingIn;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;


    public static String APP_ID = "521174844642024";
    // Instance of Facebook Class
    private Facebook facebook = new Facebook(APP_ID);

    private Bundle currentUser;

    private GraphUser user;

    Button login;

    Button signup;


    String role;
    Account mAccount;
    Bundle syncSettingsBundle;

    public static final String ACCOUNT = "dummyaccount";
    public static final String ACCOUNT_TYPE = "com.onemore.karungguniapp";

    


	ImageButton google;
	ImageButton btnfacebook;

	
	static boolean chosen=false;

	


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up dummy account for SyncAdapter
        mAccount = CreateSyncAccount(this);

        // Set the SyncAdapter to sync periodically
        syncSettingsBundle = new Bundle();
        ContentResolver.setSyncAutomatically(mAccount, AppData.AUTHORITY, true);
        ContentResolver.addPeriodicSync(mAccount, AppData.AUTHORITY, syncSettingsBundle, 60);

        // Log the user in if login was performed earlier (the current user is stored in the shared preferences)
        // Get the role from the user the account exists but it hasn't been set (e.g. if a new account was created
        // using Facebook or Google login
        currentUser = AccountManager.getCurrentUser(getApplicationContext());

        if (currentUser != null) {
            if (currentUser.get("role").equals(AppData.ROLE_KG)) {
                Intent i = new Intent(Main.this, KarungGuniActivity.class);
                startActivity(i);
            } else if (currentUser.get("role").equals(AppData.ROLE_SELLER)) {
                Intent i = new Intent(Main.this, SellerActivity.class);
                startActivity(i);
            } else if (currentUser.get("role") == null) {


                ///////////method add role to the user in db
                ///////
                ///////


                showPopup(this);
                AccountManager.setCurrentUser(getApplicationContext(), currentUser.get("email").toString(), role);
            }
        }

        mPlusClient = new PlusClient.Builder(this, this, this)
                .setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .build();

        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");

        setContentView(R.layout.main);

        login = (Button) findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);
//        Button syncdata =(Button) findViewById(R.id.syncdata);
        google = (ImageButton) findViewById(R.id.google);
        google.setOnClickListener(this);
        // Setup the 'Login with Facebook' button
        btnfacebook = (ImageButton) findViewById(R.id.fbbtn);


        btnfacebook.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                FacebookUtil.login(Main.this, new Session.StatusCallback() {

                    @Override
                    public void call(Session _session, SessionState _state, Exception _exception) {
                        if (_session.isOpened()) {
                            FacebookUtil.askMe(new Request.GraphUserCallback() {
                                public void onCompleted(GraphUser user, Response response) {
                                    if (user != null) {

                                        String displayName = user.getUsername();
                                        String email = displayName + "@facebook.com";

//                                        Handler.Callback callback = new Handler.Callback() {
//                                            intent = new Intent(getBaseContext(), KarungGuniActivity.class);
//                                        };

                                        // Create a new user with the supplied details
                                        AccountManager.createWithFacebook(getApplicationContext(), email, null);
                                        AccountManager.setCurrentUser(getApplicationContext(), email, null);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        findViewById(R.id.google).setOnClickListener(this);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent i = new Intent(Main.this, LoginActivity.class);
                startActivity(i);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent i = new Intent(Main.this, SignupActivity.class);
                startActivity(i);
            }
        });

        /*syncdata.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Log.w("MAIN", "Sync clicked");
//                mAccount = CreateSyncAccount(getApplicationContext());
                ContentResolver contentResolver = getContentResolver();
                Bundle settingsBundle = new Bundle();
                settingsBundle.putBoolean(contentResolver.SYNC_EXTRAS_MANUAL, true);
                settingsBundle.putBoolean(contentResolver.SYNC_EXTRAS_EXPEDITED, true);
                contentResolver.requestSync(mAccount, AppData.AUTHORITY, settingsBundle);
            }
        });*/
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

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
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
                        } else if (role.equals(AppData.ROLE_SELLER)) {
                            intent = new Intent(getBaseContext(), SellerActivity.class);
                        }

                        // Dismiss the progress dialog and start the new activity
                        signingIn.dismiss();
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    } catch (IOException e) {
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
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, OFFSET_X, OFFSET_Y);

        Button kg = (Button) layout.findViewById(R.id.kg);
        kg.setOnClickListener(
                new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        role = AppData.ROLE_KG;
                        chosen = true;
                        // TODO Auto-generated method stub
                        popup.dismiss();

                    }
                }

        );

        Button seller = (Button) layout.findViewById(R.id.seller);
        seller.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                role = AppData.ROLE_SELLER;
                chosen = true;
                popup.dismiss();
            }
        });
        return chosen;
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        android.accounts.AccountManager accountManager =
                (android.accounts.AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }

        return newAccount; // TODO return the right thing
    }
}

