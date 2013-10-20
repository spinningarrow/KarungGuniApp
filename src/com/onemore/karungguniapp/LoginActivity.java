package com.onemore.karungguniapp;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.*;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class LoginActivity extends Activity {

    Button mLogin;

    EditText mEmailView;
    EditText mPasswordView;
    String mEmail, mPassword;

    DBHelper DB = null;

    private UserLoginTask mAuthTask = null;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;
    private ProgressDialog loggingIn;

    SharedPreferences preferences;
    Editor editor;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        findViewById(R.id.login).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptLogin();
                    }
                });
        preferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.PREFS_NAME), Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * Attempts to login to the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        mEmailView = (EditText) findViewById(R.id.Leditemail);
        mPasswordView = (EditText) findViewById(R.id.Leditpw);

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

            // Callback for querying the server
            // If a user was found, Check if the password hash matches the found user's hash
            // If it matches, log the user in (add to SharedPrefs)
            Handler.Callback loginWithEmailCallback = new Handler.Callback() {
                Bundle result;
                JSONObject user;

                @Override
                public boolean handleMessage(Message message) {
                    result = message.getData();

                    // Return if an error occurs
                    if (result.getInt("success") != 1 || result.getInt("status") != 200) {
                        Log.w("ACCOUNT_MANAGER", "Login error occurred");

                        loggingIn.dismiss();
                        Toast.makeText(getApplicationContext(), R.string.login_incorrect, Toast.LENGTH_LONG).show();
                        return false;
                    }

                    try {
                        user = RestClient.parseJsonObject(new ByteArrayInputStream(result.getString("response").getBytes("UTF-8")));

                        // If no user was found, prompt the user to create an account
                        if (user == null) {
                            Log.w("ACCOUNT_MANAGER", "ERROR: No user found.");

                            loggingIn.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.login_incorrect, Toast.LENGTH_LONG).show();
                            return false;
                        }

                        // If the password hash doesn't match, ask the user to enter the password again
                        if (!AccountManager.hashPassword(mPassword).equals(user.getString(AppData.Users.COLUMN_NAME_PASSWORD))) {
                            Log.w("ACCOUNT_MANAGER", "ERROR: Password mismatch.");

                            loggingIn.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.login_incorrect, Toast.LENGTH_LONG).show();
                            return false;
                        }

                        // Get the user's email and role
                        String email = user.getString(AppData.Users.COLUMN_NAME_EMAIL);
                        String role = user.getString(AppData.Users.COLUMN_NAME_ROLE);

                        // Log the user in
                        Log.w("ACCOUNT_MANAGER", "User logged in.");
                        AccountManager.setCurrentUser(getApplicationContext(), email, role);

                        // Show the appropriate activity
                        Intent intent = null;

                        if (role.equals("KARUNGGUNI")) {
                            intent = new Intent(getBaseContext(), KarungGuniActivity.class);
                        } else if (role.equals("SELLER")) {
                            intent = new Intent(getBaseContext(), SellerActivity.class);
                        }

                        // Dismiss the progress dialog and start the new activity
                        loggingIn.dismiss();
                        startActivity(intent);

                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            };

            // Show a progress dialog to the user while logging in stuff happens asynchronously
            loggingIn = ProgressDialog.show(this, getString(R.string.login_progress_title), getString(R.string.login_progress_message), true);
            AccountManager.loginWithEmail(mEmail, mPassword, loginWithEmailCallback);
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
            if (role != null) {
                editor.clear();
                editor.putString("email", mEmail);
                editor.putString("role", role);
                editor.commit();
                Intent i;
                if (role.equals("Seller")) {
//					i = new Intent(getBaseContext(), <Seller>.class);
                } else if (role.equals("KG")) {
//					i = new Intent(getBaseContext(), <KG>.class);
                }
//				startActivity(i);
                return true;
            } else
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

    private String validateLogin(String email, String password, Context baseContext) {
        DB = new DBHelper(baseContext);
        SQLiteDatabase db = DB.getReadableDatabase();
        CharArrayBuffer role = null;

        String[] columns = {"_id", "role"};

        String selection = "email=? AND password=?";
        String[] selectionArgs = {email, SHA1.computeHash(password)};

        Cursor cursor = null;
        try {

            cursor = db.query(DBHelper.DATABASE_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            startManagingCursor(cursor);
        } catch (Exception e)

        {
            e.printStackTrace();
        }
        int numberOfRows = cursor.getCount();

        if (numberOfRows <= 0) {

            Toast.makeText(getApplicationContext(), "Email and Password mismatch..\nPlease Try Again", Toast.LENGTH_LONG).show();
        } else {
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