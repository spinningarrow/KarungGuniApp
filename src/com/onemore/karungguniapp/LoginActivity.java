package com.onemore.karungguniapp;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private TextView mForgotView;
	
	// SHA1 variables
	private static String SHAHash;
	public static int NO_OPTIONS=0;

	CheckBox remember;
	SharedPreferences preferences ;
	String PREFS_NAME = "com.example.sp.LoginPrefs";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		mEmailView = (EditText) findViewById(R.id.email);
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin(0);
							return true;
						}
						return false;
					}
				});
		mForgotView = (TextView) findViewById(R.id.forgot_password);
		mForgotView.setOnClickListener(new View.OnClickListener(){
			@Override
            public void onClick(View view) {
            	Intent forgotPassword = new Intent(LoginActivity.this,ResetPassword.class);
                startActivity(forgotPassword);
            }
		});


		
          remember = (CheckBox)findViewById(R.id.checkBox1);
		
		
		preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view){
						attemptLogin(0);
					}
				});
		findViewById(R.id.facebook_login_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin(1);
					}
				});
		findViewById(R.id.google_login_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin(2);
					}
				});
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin(int loginType){
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		switch(loginType){
		
			case 0 : // Login using email & password
				// Store values at the time of the login attempt.
				mEmail = mEmailView.getText().toString();
				mPassword = mPasswordView.getText().toString();
		
				boolean cancel = false;
				View focusView = null;
		
				// Check for a valid password.
				if (TextUtils.isEmpty(mPassword)) {
					mPasswordView.setError(getString(R.string.error_field_required));
					focusView = mPasswordView;
					cancel = true;
				} else if (mPassword.length() < 4) {
					mPasswordView.setError(getString(R.string.error_invalid_password));
					focusView = mPasswordView;
					cancel = true;
				}
		
				// Check for a valid email address.
				if (TextUtils.isEmpty(mEmail)) {
					mEmailView.setError(getString(R.string.error_field_required));
					focusView = mEmailView;
					cancel = true;
				} else if (!mEmail.contains("@")) {
					mEmailView.setError(getString(R.string.error_invalid_email));
					focusView = mEmailView;
					cancel = true;
				}
		
				if (cancel) {
					// There was an error; don't attempt login and focus the first
					// form field with an error.
					focusView.requestFocus();
				} else {
					// Show a progress spinner, and kick off a background task to
					// perform the user login attempt.
					mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
					showProgress(true);

                    AccountManager.loginWithEmail(mEmail, mPassword);

//					mAuthTask = new UserLoginTask();
//					mAuthTask.execute((Void) null);
//					if (preferences.getString("logged", "").toString().equals("logged")) 
//					{
//						Intent i = new Intent(LoginActivity.this,AfterLogin.class);
//						i.putExtra("EMAIL",preferences.getString("mEmail", "").toString());
//						i.putExtra("PASSWORD",preferences.getString("mPassword", "").toString());
//						i.putExtra("CHECK", true);
//						startActivity(i);
//						
//					}
//					Intent i = new Intent(LoginActivity.this,AfterLogin.class);
//					i.putExtra("USERNAME", mEmail);
//					i.putExtra("PASSWORD", mPassword);
//					i.putExtra("CHECK", remember.isChecked());
//					startActivity(i);	
				}

                break;

            case 1	:	// Login using Facebook API
                break;

			case 2	:	// Login using Google+ API
                break;

			default	:	Toast.makeText(getApplicationContext(),
							R.string.error_invalid_login_mode,
							Toast.LENGTH_LONG)
							.show();
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			
			// TODO: attempt authentication against a network service.
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(getString(R.string.http_post));
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			HttpResponse response;
			
			postParameters.add(new BasicNameValuePair("email",mEmail));
			postParameters.add(new BasicNameValuePair("password",computeSHAHash(mPassword)));
			
			try {
				httppost.setEntity(new UrlEncodedFormEntity(postParameters));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				response = httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// If Account doesn't exist
//			return false;

			// If Account exists
			// Insert SharedPreferences stuff
//			 If Account type is Seller
//			Intent i = new Intent(LoginActivity.this,<Seller>.class);
//			i.putExtra("USERNAME", mEmail);
//			i.putExtra("PASSWORD", mPassword);
//			i.putExtra("ROLE", "Seller");
//			i.putExtra("CHECK", remember.isChecked());
//			If Account type is KG
//			Intent i = new Intent(LoginActivity.this,<KG>.class);
//			i.putExtra("USERNAME", mEmail);
//			i.putExtra("PASSWORD", mPassword);
//			i.putExtra("ROLE", "Seller");
//			i.putExtra("CHECK", remember.isChecked());
//			startActivity(i);
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				mEmailView
						.setError(getString(R.string.error_invalid_credentials));
				mEmailView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

		
	/**
	* Used by computeSHAHash
	*/	
	private String convertToHex(byte[] data) throws java.io.IOException 
	{         
		StringBuffer sb = new StringBuffer();
		String hex=null;
		 
		hex=Base64.encodeToString(data, 0, data.length, NO_OPTIONS);
		 
		sb.append(hex);
		             
		return sb.toString();
	}
	 
	/**
	* Used to generate a hashed string
	*/	 
	private String computeSHAHash(String password)
	{
		MessageDigest mdSha1 = null;
		try {
		  mdSha1 = MessageDigest.getInstance("SHA-1");
	}
	catch (NoSuchAlgorithmException e1) {
		Log.e("myapp", "Error initializing SHA1 message digest");
	}
	try {
		mdSha1.update(password.getBytes("ASCII"));
		}
		catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		byte[] data = mdSha1.digest();
		try {
			SHAHash=convertToHex(data);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return SHAHash;
	}
}