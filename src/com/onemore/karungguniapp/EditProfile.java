package com.onemore.karungguniapp;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfile extends Activity {
	
	private TextView view_currentPw, view_newPw, view_confirmNewPw, view_displayName, view_address;
	private ProgressDialog progressDialog;
	
	private Bundle prefs;
	private String email, role;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    prefs = AccountManager.getCurrentUser(getApplicationContext());
	    email = prefs.getString("email");
	    role = prefs.getString("role");
	    
	    setContentView(R.layout.edit_profile);
    	view_currentPw = (TextView)findViewById(R.id.editprofile_currentpw);
    	view_newPw = (TextView)findViewById(R.id.editprofile_newpw); 
    	view_confirmNewPw = (TextView)findViewById(R.id.editprofile_confirmnewpw); 
    	view_displayName = (TextView)findViewById(R.id.editprofile_displayname);
    	view_address = (TextView)findViewById(R.id.editprofile_address);
    	
	    if (role.equals(AppData.ROLE_KG)){
	    	view_address.setVisibility(View.GONE);
	    }
	    
	    
	    // Pre-populate fields with existing data
	    // Callback for querying the server
        Handler.Callback getUserDetailsCallback = new Handler.Callback() {
            Bundle result;
            JSONObject user;

            @Override
            public boolean handleMessage(Message message) {
                result = message.getData();

                // Return if an error occurs
                if (result.getInt("success") != 1 || result.getInt("status") != 200) {
                    Log.w("ACCOUNT_MANAGER", getString(R.string.editprofile_connerror));

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.editprofile_connerror), Toast.LENGTH_LONG).show();
                    return false;
                }

                try {
                    user = RestClient.parseJsonObject(new ByteArrayInputStream(result.getString("response").getBytes("UTF-8")));

                    // If no user was found
                    if (user == null) {
                        Log.w("ACCOUNT_MANAGER", "ERROR: No user found.");

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.editprofile_usernotfound), Toast.LENGTH_LONG).show();
                        return false;
                    }
                    
                    // Populate relevant fields depending on role
                    if (role.equals(AppData.ROLE_KG)){
                    	view_displayName.setText(user.getString(AppData.KarungGunis.COLUMN_NAME_DISPLAY_NAME));
                    }
                    else if (role.equals(AppData.ROLE_SELLER)){
                    	view_displayName.setText(user.getString(AppData.Sellers.COLUMN_NAME_DISPLAY_NAME));
                    	view_address.setText(user.getString(AppData.Sellers.COLUMN_NAME_ADDRESS));
                    }

                    // Dismiss the progress dialog
                    progressDialog.dismiss();

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

        // Show a progress dialog to the user while populating fields with existing data
        progressDialog = ProgressDialog.show(this, getString(R.string.editprofile_loading_title), getString(R.string.editprofile_loading_message), true);
        AccountManager.getUserDetails(email, role, getUserDetailsCallback);
        
        // On submit
        findViewById(R.id.editprofile_submit).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    	
                    	String currentPw = view_currentPw.getEditableText().toString();
            	    	String newPw = view_newPw.getEditableText().toString();
            	    	String confirmNewPw = view_confirmNewPw.getEditableText().toString();
            	    	String displayName = view_displayName.getEditableText().toString();
            	    	String address = view_address.getEditableText().toString();
            	    	
            	    	view_currentPw.setError(null);
                    	view_newPw.setError(null);
                    	view_confirmNewPw.setError(null);
                    	view_displayName.setError(null);
                    	view_address.setError(null);
                    	
                    	// Input validation
                    	boolean invalid = false;

                    	if (TextUtils.isEmpty(displayName)) {
                            invalid = true;
                            view_displayName.setError(getString(R.string.editprofile_compulsory));
                        } else if (TextUtils.isEmpty(currentPw)) {
                            invalid = true;
                            view_currentPw.setError(getString(R.string.editprofile_compulsory));
                        } else if ((!TextUtils.isEmpty(newPw) || !TextUtils.isEmpty(confirmNewPw))
                        		&& !TextUtils.equals(newPw, confirmNewPw)) {
                            invalid = true;
                            view_newPw.setError(getString(R.string.editprofile_passwords_different));
                        } else if (role.equals(AppData.ROLE_SELLER) && TextUtils.isEmpty(address)){
                        	invalid = true;
                        	view_address.setError(getString(R.string.editprofile_address_empty));
                        }
                        

                        if (invalid == false) {
                    	
                        	Handler.Callback setUserDetailsCallback = new Handler.Callback() {
                                Bundle result;
                                JSONObject user;

                                @Override
                                public boolean handleMessage(Message message) {
                                    result = message.getData();

                                    // Return if an error occurs
                                    if (result.getInt("success") != 1 || result.getInt("status") != 200) {
                                        Log.w("ACCOUNT_MANAGER", getString(R.string.editprofile_connerror));

                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), getString(R.string.editprofile_connerror), Toast.LENGTH_LONG).show();
                                        return false;
                                    }

                                    try {
                                        user = RestClient.parseJsonObject(new ByteArrayInputStream(result.getString("response").getBytes("UTF-8")));

                                        // If no user was found
                                        if (user == null) {
                                            Log.w("ACCOUNT_MANAGER", "ERROR: No user found.");

                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), getString(R.string.editprofile_usernotfound), Toast.LENGTH_LONG).show();
                                            return false;
                                        }
                                        
                                        // Display success
                                        Toast.makeText(getApplicationContext(), getString(R.string.editprofile_success), Toast.LENGTH_LONG).show();
                                        
                                        // Dismiss the progress dialog
                                        progressDialog.dismiss();
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
                            
	                        // Show a progress dialog to the user while updating profile asynchronously
	                        progressDialog = ProgressDialog.show(getApplicationContext(), getString(R.string.editprofile_updating_title), getString(R.string.editprofile_updating_message), true);
	                        AccountManager.setUserDetails(email, role, setUserDetailsCallback);
                        }
                    }
                });
	}
}
