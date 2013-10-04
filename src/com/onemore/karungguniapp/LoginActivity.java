package com.onemore.karungguniapp;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity
{
 
 Button mLogin;
  
 EditText mEmailView;
 EditText mPasswordView;
 String mEmail, mPassword;
 
 DBHelper DB = null;
 
 private UserLoginTask mAuthTask = null;
 private View mLoginFormView;
 private View mLoginStatusView;
 private TextView mLoginStatusMessageView;
 
 SharedPreferences preferences;
 Editor editor;
 
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    
        findViewById(R.id.login).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view){
						attemptLogin();
					}
				});
        preferences = getApplicationContext()
        				.getSharedPreferences(getString(R.string.PREFS_NAME), Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void onDestroy()
    {
     super.onDestroy();
     DB.close();
    }

 /**
	 * Attempts to login to the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin(){
		if (mAuthTask != null) {
			return;
		}
		
		mEmailView = (EditText)findViewById(R.id.Leditemail);
		mPasswordView = (EditText)findViewById(R.id.Leditpw);
		
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

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
		} 
		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
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
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
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
			String role = validateLogin(mEmail, mPassword, getBaseContext());
			if(role != null)
			{
				editor.clear();
				editor.putString("email", mEmail);
				editor.putString("role", role);
				editor.commit();
				Intent i;
				if (role.equals("Seller")){
//					i = new Intent(getBaseContext(), <Seller>.class);
				}
				else if (role.equals("KG")){
//					i = new Intent(getBaseContext(), <KG>.class);
				}
//				startActivity(i);
				return true;
			}
			else
				return false;
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
	
	 private String validateLogin(String email, String password, Context baseContext) 
	 {
		  DB = new DBHelper(baseContext);
		  SQLiteDatabase db = DB.getReadableDatabase();
		  CharArrayBuffer role = null;
		  
		  String[] columns = {"_id","role"};
		  
		  String selection = "email=? AND password=?";
		  String[] selectionArgs = {email,SHA1.computeHash(password)};
		  
		  Cursor cursor = null;
		  try{
		  
		  cursor = db.query(DBHelper.DATABASE_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		  startManagingCursor(cursor);
		  }
		  catch(Exception e)
		  
		  {
		   e.printStackTrace();
		  }
		int numberOfRows = cursor.getCount();
		  
		  if(numberOfRows <= 0)
		  {
		  
		   Toast.makeText(getApplicationContext(), "Email and Password mismatch..\nPlease Try Again", Toast.LENGTH_LONG).show();
		  }
		  else{
			  cursor.copyStringToBuffer(1, role);
		  }
		  cursor.close();
		  return role.toString();
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
 
	
}