package com.onemore.karungguniapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import com.turbomanage.httpclient.ParameterMap;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AccountManager {

    // Create a new account (with email and password)
    public static void createWithEmail(String email, String password, final String role, final Handler.Callback callback) {
        final ParameterMap params = new ParameterMap();
        params.put("email", email);
        params.put("password", hashPassword(password));

        // Callback for creating a new user in the users table
        // If the insertion is successful, it inserts the data into the karung_gunis or sellers table,
        // depending on the user's role and notifies the callback provided the createWithEmail method
        Handler.Callback insertUserCallback = new Handler.Callback() {
            Bundle result;
            JSONObject user;
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
                if (role.equals("Karung Guni")) {
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

    // Standard login (email with password)
    // The results of the login (success, failure, response) are handled by the callback since the HTTP query takes
    // place in an asynchronous thread
    public static void loginWithEmail(String email, final String password) {

        // Callback for querying the server
        // If a user was found, Check if the password hash matches the found user's hash
        // If it matches, log the user in (add to SharedPrefs)
        Handler.Callback queryCallback = new Handler.Callback() {
            Bundle result;
            JSONObject user;

            @Override
            public boolean handleMessage(Message message) {
                result = message.getData();

                // Return if an error occurs
                if (result.getInt("success") != 1 || result.getInt("status") != 200) {
                    Log.w("ACCOUNT_MANAGER", "Login error occurred");
                    return false;
                }

                try {
                    user = RestClient.parseJsonObject(new ByteArrayInputStream(result.getString("response").getBytes("UTF-8")));

                    // TODO If no user was found, prompt the user to create an account
                    if (user == null) {
                        Log.w("ACCOUNT_MANAGER", "ERROR: No user found.");
                        return false;
                    }

                    // TODO If the password hash doesn't match, ask the user to enter the password again
                    if (!AccountManager.hashPassword(password).equals(user.getString(AppData.Users.COLUMN_NAME_PASSWORD))) {
                        Log.w("ACCOUNT_MANAGER", "ERROR: Password mismatch.");
                        return false;
                    }

                    // TODO Log the user in
                    Log.w("ACCOUNT_MANAGER", "User logged in.");
                    return true;
                }

                catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }

                catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };

        // Query the RestClient for user with specified email
        // Pass the callback to query so it can populate it
        RestClient.query(
                Uri.parse(AppData.Users.CONTENT_ID_URI_BASE + email),
                null,
                null,
                null,
                null,
                queryCallback);
    }

    // Get the current user, if any
    public static Bundle getCurrentUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Bundle currentUser = new Bundle();
        currentUser.putString("email", prefs.getString("currentUser.email", null));
        currentUser.putString("role", prefs.getString("currentUser.role", null));

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

    // Helper method for password hashing
    public static String hashPassword(String password) {
        return SHA1.computeHash(password);
    }
}
