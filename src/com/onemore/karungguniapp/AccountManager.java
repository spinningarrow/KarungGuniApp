package com.onemore.karungguniapp;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountManager {
    public static String hashPassword(String password) {
        // TODO return hashed password
        return password;
    }

    public static boolean loginWithEmail(String email, String password, Handler.Callback callback) throws JSONException {
        Log.w("ACCOUNT_MANAGER", "entered loginWithEmail");
        boolean success;
        String hash;

        // Query the RestClient for user with specified email
        Log.w("ACCOUNT_MANAGER", "before query");

        RestClient.query(
                Uri.parse(AppData.Users.CONTENT_URI + "/" + email),
                null,
                null,
                null,
                null,
                callback);

        Log.w("ACCOUNT_MANAGER", "after query");


//        Log.w("ACCOUNT_MANAGER", user.toString());
//
//        if (user != null) {
//            Log.w("ACCOUNT_MANAGER", "user is not null");
//            hash = user.getString("password");
//            Log.w("ACCOUNT_MANAGER", "hash = " + hash);
//
//            // Check if the password hashes match
//            if (AccountManager.hashPassword(password) == hash) {
//                // Logged in successfully
//                // TODO Save the user to the session
//                success = true;
//            }
//
//            else {
//                success = false;
//            }
//        }
//
//        else {
//            success = false;
//        }

        return true;
    }
}
