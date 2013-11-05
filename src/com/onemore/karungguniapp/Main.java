package com.onemore.karungguniapp;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.turbomanage.httpclient.ParameterMap;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;


public class Main extends Activity {

    private ProgressDialog signingIn;
    private ProgressDialog mConnectionProgressDialog;

    public static String APP_ID = "521174844642024";

    Button login;
    Button signup;

    String displayName;
    String role;
    Account mAccount;
    Bundle syncSettingsBundle;

    private Bundle currentUser;

    public static final String ACCOUNT = "dummyaccount";
    public static final String ACCOUNT_TYPE = "com.onemore.karungguniapp";

    ImageButton btnfacebook;

    private static HashMap<String, String> roles = new HashMap<String, String>();

    static {
        roles.put("Karung Guni", AppData.ROLE_KG);
        roles.put("Seller", AppData.ROLE_SELLER);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Activity self = this;

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

            String currentUserRole = currentUser.getString("role");

            if (currentUserRole == null) {

                // Prompt the user to choose a role
                DialogFragment selectRoleDialog = new SelectRoleDialogFragment();
                selectRoleDialog.show(self.getFragmentManager(), "roles");

                AccountManager.setCurrentUser(getApplicationContext(), currentUser.getString("email"), role);
            }

            else if (currentUserRole.equals(AppData.ROLE_KG)) {
                Intent i = new Intent(self, KarungGuniActivity.class);
                startActivity(i);
            }

            else if (currentUserRole.equals(AppData.ROLE_SELLER)) {
                Intent i = new Intent(self, SellerActivity.class);
                startActivity(i);
            }
            finish();
        }

        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");

        setContentView(R.layout.main);
        //setTitleColor(Color.rgb(15,48,78));

        login = (Button) findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);

        btnfacebook = (ImageButton) findViewById(R.id.fbbtn);

        btnfacebook.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View arg0) {
                FacebookUtil.login(Main.this, new Session.StatusCallback() {

                    @Override
                    public void call(Session _session, SessionState _state, Exception _exception) {
                        if (_session.isOpened()) {
                            signingIn = ProgressDialog.show(self, getString(R.string.login_progress_signing_in), getString(R.string.login_progress_message), true);

                            FacebookUtil.askMe(new Request.GraphUserCallback() {
                                public void onCompleted(GraphUser user, Response response) {
                                    if (user != null) {

                                        displayName = user.getName();
                                        String email = user.getUsername() + "@facebook.com";

                                        Handler.Callback callback = new Handler.Callback() {
                                            Bundle result;

                                            @Override
                                            public boolean handleMessage(Message message) {
                                                result = message.getData();
                                                signingIn.dismiss();

                                                // Restart the main activity
                                                Intent intent = getIntent();
                                                finish();
                                                startActivity(intent);

                                                return true;
                                            }
                                        };

                                        // Create a new user with the supplied details
                                        AccountManager.createWithFacebook(getApplicationContext(), email, callback);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent i = new Intent(Main.this, LoginActivity.class);
                startActivityForResult(i,AppData.REQUEST_EXIT);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent i = new Intent(Main.this, SignupActivity.class);
                startActivityForResult(i,AppData.REQUEST_EXIT);
            }
        });
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        
        // If LoginActivity or SignupActivity is successful
        if (_requestCode == AppData.REQUEST_EXIT) {
            if (_resultCode == RESULT_OK) {
            	startActivity(_data);
            	this.finish();
            }
        }
        else
        	FacebookUtil.onActivityResult(this, _requestCode, _resultCode, _data);
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

    // Create an AlertDialog that prompts the user to choose a role
    @SuppressLint("ValidFragment")
	public class SelectRoleDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(getString(R.string.signup_choose_role));

            builder.setItems(roles.keySet().toArray(new String[0]), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Uri uri;

                    final ParameterMap params = new ParameterMap();
                    params.put("email", currentUser.getString("email"));
                    params.put("password", "");
                    params.put("display_name", displayName);

                    // The 'which' argument contains the index position
                    // of the selected item
                    role = roles.get(roles.keySet().toArray(new String[0])[which]);

                    // Set the role for the current user
                    // The current user's email must be set before this is called
                    AccountManager.setCurrentUser(getApplicationContext(), currentUser.getString("email"), role);

                    // Create a Karung Guni or a Seller
                    if (role.equals(AppData.ROLE_KG)) {
                        uri = AppData.KarungGunis.CONTENT_ID_URI_BASE;
                    } else {
                        uri = AppData.Sellers.CONTENT_ID_URI_BASE;
                    }

                    RestClient.insert(uri, params, null);

                    // Restart the main activity
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}

