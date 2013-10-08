package com.onemore.karungguniapp;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
                        return false;
                    }

                    // TODO If the password hash doesn't match, ask the user to enter the password again
                    if (!AccountManager.hashPassword(password).equals(user.getString(AppData.Users.COLUMN_NAME_PASSWORD))) {
                        return false;
                    }

                    // TODO Log the user in
                    Log.w("ACCOUNT_MANAGER", "User logged in!");
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
}
