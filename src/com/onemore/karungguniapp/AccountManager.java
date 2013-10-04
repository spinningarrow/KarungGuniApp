package com.onemore.karungguniapp;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;

public class AccountManager {
    public static String hashPassword(String password) {
        // TODO return hashed password
        return password;
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
            boolean success;

            @Override
            public boolean handleMessage(Message message) {
                result = message.getData();

                // Check that no error occurred
                if (result.getInt("success") == 1 && result.getInt("status") == 200) {
                    try {
                        user = RestClient.parseJsonObject(new ByteArrayInputStream(result.getString("response").getBytes("UTF-8")));

                        // Check if a user was found
                        if (user != null) {

                            // Check if the password hash matches the found user's hash
                            try {
                                if (AccountManager.hashPassword(password).equals(user.getString(AppData.Users.COLUMN_NAME_PASSWORD))) {
                                    // TODO log the user in
                                    Log.w("ACCOUNT_MANAGER", "User logged in!");
                                    success = true;
                                }

                                else {
                                    // TODO ask the user to enter the password or email again
                                    success = false;
                                }
                            }

                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        else {
                            // TODO ask the user to create an account
                            success = false;
                        }
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                else {
                    success = false;
                }

                return success;
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
}
