package com.onemore.karungguniapp;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.util.List;

public class RestClient {

    private static final String API_URL = "http://kgserver.herokuapp.com/";
    private static final String API_PATH = "api/1.0/";

    private static AndroidHttpClient httpClient = new AndroidHttpClient(API_URL);
    static {
        httpClient.setMaxRetries(5);
    }

    // Helper method to parse JSON array and return it
    public static JSONArray parseJsonArray(InputStream json) throws IOException, JSONException {

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
    public static JSONObject parseJsonObject(InputStream json) throws IOException, JSONException {

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

    // Helper method that converts the Uri into a request endpoint
    private static String getRequestEndpoint(Uri uri) {
        String requestEndpoint;
        List<String> pathSegments = uri.getPathSegments();

        switch (AppDataUriMatcher.sUriMatcher.match(uri)) {
            case AppDataUriMatcher.USERS:
            case AppDataUriMatcher.SELLERS:
            case AppDataUriMatcher.KARUNG_GUNIS:
            case AppDataUriMatcher.ADVERTISEMENTS:
            case AppDataUriMatcher.REQUESTS:
                requestEndpoint = pathSegments.get(0);
                break;

            case AppDataUriMatcher.USER_ID:
            case AppDataUriMatcher.SELLER_ID:
            case AppDataUriMatcher.KARUNG_GUNI_ID:
            case AppDataUriMatcher.ADVERTISEMENT_ID:
            case AppDataUriMatcher.REQUEST_ID:
                requestEndpoint = pathSegments.get(0) + "/" + pathSegments.get(1);
                break;

            // If the URI pattern doesn't match any permitted patterns, throw an exception
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        return requestEndpoint;
    }

    // Query the server database for single objects or an array of objects
    // Call the callback when the query is complete
    public static Bundle query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, Handler.Callback callback) {
        Log.w("REST_CLIENT", uri.toString());
        String url = API_PATH + getRequestEndpoint(uri) + (selection != null ? selection : "");

        // If callback is null, GET the response synchronously (used in SyncAdapter)
        if (callback == null) {
            Bundle result = new Bundle();

            HttpResponse httpResponse = httpClient.get(url, null);
            if (httpResponse != null) {
                result.putInt("status", httpResponse.getStatus());
                result.putString("response", httpResponse.getBodyAsString());
            }

            return result;
        }

        // GET the result asynchronously
        final Handler handler = new Handler(callback);
        final Message message = Message.obtain();

        httpClient.get(url, null, new AsyncCallback() {

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

        return null;
    }

    // Insert into the database
    public static Bundle insert(Uri uri, ParameterMap params, Handler.Callback callback) {
        String url = API_PATH + getRequestEndpoint(uri);

        // If callback is null, GET the response synchronously (used in SyncAdapter)
        if (callback == null) {
            Bundle result = new Bundle();

            HttpResponse httpResponse = httpClient.post(url, params);
            result.putInt("status", httpResponse.getStatus());
            result.putString("response", httpResponse.getBodyAsString());

            return result;
        }

        // POST params to the API request endpoint asynchronously
        final Handler handler = new Handler(callback);
        final Message message = Message.obtain();

        httpClient.post(url, params, new AsyncCallback() {

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

        return null;
    }
}
