package com.onemore.karungguniapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SignupActivity extends Activity implements OnClickListener {
    // Variable Declaration should be in onCreate()
    private Button mSubmit;
    private EditText mDisplayName;
    private EditText mPassword;
    private EditText mEmail;
    private Spinner mRole;
    private ProgressDialog signingIn;

    private static HashMap<String, String> roles = new HashMap<String, String>();
    static {
        roles.put("Karung Guni", AppData.ROLE_KG);
        roles.put("Seller", AppData.ROLE_SELLER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //Assignment of UI fields to the variables
        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);

        mDisplayName = (EditText) findViewById(R.id.reuname);
        mPassword = (EditText) findViewById(R.id.repass);
        mEmail = (EditText) findViewById(R.id.eemail);
        mRole = (Spinner) findViewById(R.id.spinner1);

        // Spinner method to read the on selected value
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            new ArrayList<String>(roles.keySet()));

        mRole.setAdapter(spinnerArrayAdapter);
//        mRole.setOnItemSelectedListener(this);
    }

    public void onClick(View v) {

        if (v.getId() == R.id.submit) {

            String displayName = mDisplayName.getText().toString();
            String password = mPassword.getText().toString();
            String email = mEmail.getText().toString();
            final String role = roles.get(mRole.getSelectedItem());

            boolean invalid = false;

            if (displayName.equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
            } else if (password.equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
            } else if (email.equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Please enter your Email ID", Toast.LENGTH_SHORT).show();
            }

            if (invalid == false) {

                // Callback for  when a new user is inserted to karung_gunis or sellers table
                // If the insertion is successful, show the appropriate activity
                // Also add the user state to the SharedPrefs
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
                AccountManager.createWithEmail(email, password, role, displayName, createWithEmailCallback);
            }
        }
    }

//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        // Get the currently selected State object from the spinner
//        State st = (State) mRole.getSelectedItem();
//
//        // Show it via a toast
//        toastState("onItemSelected", st);
//    }
//
//    public void toastState(String name, State st) {
//        if (st != null) {
//            rol = st.name;
//            //Toast.makeText(getBaseContext(), rol, Toast.LENGTH_SHORT).show();
//
//        }
//    }

//    public void onNothingSelected(AdapterView<?> arg0) {
//        // TODO Auto-generated method stub
//
//    }
}
