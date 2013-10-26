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

public class AdManager {

    // Create a new account (with email and password)
    public static void createNewAd(String owner,String title, String desc, final String timing,
                                        String type, String photo_url,final Handler.Callback callback) {

        final ParameterMap params = new ParameterMap();
        params.put("owner",owner);
        params.put("title", title);
        params.put("description", desc);
        params.put("timing", timing);
        params.put("photo_url", photo_url);
        params.put("category",type);


        // Callback for creating a new user in the users table
        // If the insertion is successful, it inserts the data into the karung_gunis or sellers table,
        // depending on the user's role and notifies the callback provided the createWithEmail method
        Handler.Callback insertADCallback = new Handler.Callback() {
            Bundle result;
            Uri uri;

            @Override
            public boolean handleMessage(Message message) {
                result = message.getData();

                if (result.getInt("success") != 1 || result.getInt("status") != 201) {
                    Log.w("AD_MANAGER", "Insert AD error occurred");

                    // If insertion returned a 409 Conflict error (i.e., user already exists),
                    // pass this message on to the callback
                    if (result.getInt("status") == 409) {
                        Handler handler = new Handler(callback);
                        handler.sendMessage(Message.obtain(message));
                    }
                    return false;
                }

                // If insert was successful, also insert into the karung_gunis or sellers table

                    uri = AppData.Advertisements.CONTENT_ID_URI_BASE;


                RestClient.insert(uri, params, callback);
                return true;
            }
        };

        RestClient.insert(AppData.Advertisements.CONTENT_ID_URI_BASE, params, insertADCallback);
    }







    // Standard login (email with password)
    // The results of the login (success, failure, response) are handled by the callback since the HTTP query takes
    // place in an asynchronous thread

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

}
