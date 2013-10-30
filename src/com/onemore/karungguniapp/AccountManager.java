package com.onemore.karungguniapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.turbomanage.httpclient.ParameterMap;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AccountManager {

    // Create a new account (with email and password)
    public static void createWithEmail(String email, String password, final String role,
    		final String displayName, final Handler.Callback callback) {

        final ParameterMap params = new ParameterMap();
        params.put(AppData.Users.COLUMN_NAME_EMAIL, email);
        params.put(AppData.Users.COLUMN_NAME_PASSWORD, hashPassword(password));
        params.put(AppData.Sellers.COLUMN_NAME_DISPLAY_NAME, displayName); // could be AppData.KarungGunis; column name is the same

        // Callback for creating a new user in the users table
        // If the insertion is successful, it inserts the data into the karung_gunis or sellers table,
        // depending on the user's role and notifies the callback provided the createWithEmail method
        Handler.Callback insertUserCallback = new Handler.Callback() {
            Bundle result;
            Uri uri;

            @Override
            public boolean handleMessage(Message message) {
                result = message.getData();

                if (result.getInt("success") != 1 || result.getInt("status") != 201) {
                    Log.w("ACCOUNT_MANAGER", "Insert user error occurred");

                    // If insertion returned a 409 Conflict error (i.e., user already exists),
                    // pass this message on to the callback
                    if (result.getInt("status") == 409) {
                        Handler handler = new Handler(callback);
                        handler.sendMessage(Message.obtain(message));
                    }
                    return false;
                }

                // If insert was successful, also insert into the karung_gunis or sellers table
                if (role.equals(AppData.ROLE_KG)) {
                    uri = AppData.KarungGunis.CONTENT_ID_URI_BASE;
                } else {
                    uri = AppData.Sellers.CONTENT_ID_URI_BASE;
                }

                RestClient.insert(uri, params, callback);
                return true;
            }
        };

        RestClient.insert(AppData.Users.CONTENT_ID_URI_BASE, params, insertUserCallback);
    }

    public static void createWithFacebook(final Context context, final String email, Handler.Callback callback) {

        final Handler handler = new Handler(callback);

        final Handler.Callback insertUserCallback = new Handler.Callback() {
            Bundle result;

            @Override
            public boolean handleMessage(Message message) {
                result = message.getData();

                if (result.getInt("status") != 201) {
                    Log.w("ACCOUNT_MANAGER", "Insert user error occurred");

                    return false;
                }

                // If insert was successful, log the user in
                AccountManager.setCurrentUser(context, email, null);
                handler.sendMessage(Message.obtain(message));
                return true;
            }
        };

        // Check if a user by that email already exists
        Handler.Callback requestUser = new Handler.Callback() {
            Bundle result;
            JSONObject user;

            @Override
            public boolean handleMessage(Message message) {
                result = message.getData();

                // No user found, create a new one
                if (result.getInt("status") == 404) {

                    ParameterMap params = new ParameterMap();
                    params.put(AppData.Users.COLUMN_NAME_EMAIL, email);
                    params.put(AppData.Users.COLUMN_NAME_PASSWORD, "");

                    RestClient.insert(AppData.Users.CONTENT_ID_URI_BASE, params, insertUserCallback);
                    return true;
                }

                // Return if an error occurred
                else if (result.getInt("status") != 200) {
                    Log.w("ACCOUNT_MANAGER", "Login error occurred");

                    return false;
                }

                // Try to log the user in
                try {
                    user = RestClient.parseJsonObject(new ByteArrayInputStream(result.getString("response").getBytes("UTF-8")));

                    String role = user.getString(AppData.Users.COLUMN_NAME_ROLE);

                    AccountManager.setCurrentUser(context, email, role);
                    handler.sendMessage(Message.obtain(message));

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

        RestClient.query(
            Uri.parse(AppData.Users.CONTENT_ID_URI_BASE + email),
            null,
            null,
            null,
            null,
            requestUser);
    }

    // Standard login (email with password)
    // The results of the login (success, failure, response) are handled by the callback since the HTTP query takes
    // place in an asynchronous thread
    public static void loginWithEmail(String email, final String password, Handler.Callback callback) {

        // Query the RestClient for user with specified email
        // Pass the callback to query so it can populate it
        RestClient.query(
                Uri.parse(AppData.Users.CONTENT_ID_URI_BASE + email),
                null,
                null,
                null,
                null,
                callback);
    }

    // Retrieve user details for EditProfile activity
    public static void getUserDetails(String email, String role, Handler.Callback callback){
    	
    	Uri table = null;
    	// Query the RestClient for user with specified email
        // Pass the callback to query so it can populate it
    	if (role.equals(AppData.ROLE_KG))
    		table = AppData.KarungGunis.CONTENT_ID_URI_BASE ;
    	else
    		table = AppData.Sellers.CONTENT_ID_URI_BASE;
    	
    	RestClient.query(
                Uri.parse(table + email),
                null,
                null,
                null,
                null,
                callback);
    	
    }
    
    // Retrieve user details for EditProfile activity
    public static void setUserDetails(String email, String role, Handler.Callback callback){
    	
    	Uri table = null;

    	if (role.equals(AppData.ROLE_KG))
    		table = AppData.KarungGunis.CONTENT_ID_URI_BASE ;
    	else
    		table = AppData.Sellers.CONTENT_ID_URI_BASE;
    	
    	//TODO 	Update row
//    	RestClient.update(
//                Uri.parse(table + email),
//                null,
//                null,
//                null,
//                null,
//                callback);
    	
    }
    
    // Get the current user, if any
    public static Bundle getCurrentUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Bundle currentUser = new Bundle();
        currentUser.putString("email", prefs.getString("currentUser.email", null));
        currentUser.putString("role", prefs.getString("currentUser.role", null));
        
        // For facebook session
        currentUser.putString("access_token", prefs.getString("currentUser.access_token", null));
        currentUser.putLong("access_expires", prefs.getLong("currentUser.access_expires", 0));
        
        // Don't return the bundle at all if the current user is null
        if (currentUser.getString("email") == null) {
            return null;
        }

        return currentUser;
    }


    // Set the current user
    public static void setCurrentUser(Context context, String email, String role) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("currentUser.email", email);
        editor.putString("currentUser.role", role);

        editor.commit();
    }
    
    // Set the current user for facebook session
    public static void setCurrentUser(Context context, String access_token, Long access_expires,
    		String email, String role) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("currentUser.access_token", access_token);
        editor.putLong("currentUser.access_expires", access_expires);
        editor.putString("currentUser.email", email);
        editor.putString("currentUser.role", role);

        editor.commit();
    }
    
    public static void clearCurrentUser(Context context){
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        
        editor.clear().commit();
    }

    // Helper method for password hashing
    public static String hashPassword(String password) {
        return SHA1.computeHash(password);
    }
}
