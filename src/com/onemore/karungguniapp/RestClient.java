package com.onemore.karungguniapp;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.android.AndroidHttpClient;
import org.codeandmagic.deferredobject.Promise;
import org.codeandmagic.deferredobject.android.DeferredAsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

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

    public static JSONObject query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, Handler.Callback callback) {
        Log.w("REST_CLIENT", "Entered query");
        // TODO Convert URI to requestEndpoint
        String requestEndpoint = "users";
        JSONObject obj = null;
//        Promise promise<AndroidHttpClient, AndroidHttpClient, Void> = new DeferredAsyncTask<AndroidHttpClient, AndroidHttpClient, Void>() {
//
//        }

        final Handler handler = new Handler(callback);
        final Message message = new Message();

        AndroidHttpClient httpClient = new AndroidHttpClient(API_URL);
        httpClient.setMaxRetries(5);
        httpClient.get(API_PATH + requestEndpoint, null, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                Log.w("REST_CLIENT", "Entered onComplete");

                // Print response
                Log.d("REST_CLIENT", httpResponse.getBodyAsString());

                handler.sendMessage(message);

                InputStream stream = null;

                try {
                    stream = new ByteArrayInputStream(httpResponse.getBodyAsString().getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    JSONArray result = parseJsonArray(stream);
                    JSONObject obj = result.getJSONObject(0);
                    Log.d("REST API", obj.getString("email"));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Exception e) {
                Log.w("REST_CLIENT", "Entered onError");
                e.printStackTrace();
            }
        });

        return obj;
    }
}
