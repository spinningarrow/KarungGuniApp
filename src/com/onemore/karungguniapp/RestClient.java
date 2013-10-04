package com.onemore.karungguniapp;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.android.AndroidHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

public class RestClient {

    private static final String API_URL = "http://kgserver.herokuapp.com/";
    private static final String API_PATH = "api/1.0/";

    // Helper method to parse JSON array and return it
    public static JSONArray parseJsonArray(InputStream json) throws Exception {

        // The JSON parser needs the data to be in memory entirely
        BufferedReader reader = new BufferedReader(new InputStreamReader(json));
        StringBuilder sb = new StringBuilder();
        JSONArray result;

        try {
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            reader.close();
        }

        // Check if JSON string really is an array
        if (sb.charAt(0) == '[') {
            result = new JSONArray(sb.toString());
        } else {
            result = null;
        }

        return result;
    }

    // Helper method to parse JSON object and return it
    public static JSONObject parseJsonObject(InputStream json) throws Exception {

        // The JSON parser needs the data to be in memory entirely
        BufferedReader reader = new BufferedReader(new InputStreamReader(json));
        StringBuilder sb = new StringBuilder();
        JSONObject result;

        try {
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            reader.close();
        }

        // Check if JSON string really is an array
        if (sb.charAt(0) == '{') {
            result = new JSONObject(sb.toString());
        } else {
            result = null;
        }

        return result;
    }

    // Query the server database for single objects or an array of objects
    // Call the callback when the query is complete
    public static void query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, Handler.Callback callback) {
//        Log.w("REST_CLIENT", "Entered query");
        // TODO Convert URI to requestEndpoint
        // Compute requestEndpoint based on the uri
        String requestEndpoint;

        switch (AppDataUriMatcher.sUriMatcher.match(uri)) {
            case AppDataUriMatcher.USERS:
            case AppDataUriMatcher.ADVERTISEMENTS:
                requestEndpoint = uri.getPathSegments().get(0);
                break;

            case AppDataUriMatcher.USER_EMAIL:
            case AppDataUriMatcher.ADVERTISEMENT_ID:
                List<String> pathSegments = uri.getPathSegments();
                requestEndpoint = pathSegments.get(0) + "/" + pathSegments.get(1);
                break;

            // If the URI pattern doesn't match any permitted patterns, throws an exception.
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        final Handler handler = new Handler(callback);
        final Message message = Message.obtain();

        AndroidHttpClient httpClient = new AndroidHttpClient(API_URL);
        httpClient.setMaxRetries(5);

        // GET the result asynchronously
        httpClient.get(API_PATH + requestEndpoint, null, new AsyncCallback() {

            // Store the result in a bundle which will then be passed as a message to the query callback
            // result schema:
            // int success - whether the query was successful
            // int status - HTTP status code returned
            // String response - raw HTTP response (the callback handler must know what to do with it)
            Bundle result = new Bundle();

            @Override
            public void onComplete(HttpResponse httpResponse) {

                // Query was successful (TODO actually maybe not, does it just mean that no error occurred from this side?)
                result.putInt("success", 1);
                result.putInt("status", httpResponse.getStatus());
                result.putString("response", httpResponse.getBodyAsString());

                message.setData(result);
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {

                result.putInt("success", 0);
                e.printStackTrace();
            }
        });
    }
}
