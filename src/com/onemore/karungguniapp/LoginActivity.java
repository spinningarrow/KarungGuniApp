package com.onemore.karungguniapp;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    Button mLogin;

    EditText mEmailView;
    EditText mPasswordView;
    String mEmail, mPassword;

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

                        if (role.equals("KARUNG_GUNI")) {
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

    
}