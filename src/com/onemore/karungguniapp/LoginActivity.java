package com.onemore.karungguniapp;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import org.codeandmagic.deferredobject.*;
import org.codeandmagic.deferredobject.android.DeferredAsyncTask;
import org.codeandmagic.deferredobject.android.DeferredHttpUrlConnection;
import org.codeandmagic.deferredobject.merge.MergedPromiseReject;
import org.codeandmagic.deferredobject.merge.MergedPromiseResult2;
import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends Activity implements
        ResolveCallback<HttpURLConnection>, RejectCallback<HttpURLConnection> {

    private static final String TAG = "Test";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

        // Test ContentProvider
        // TODO remove

        // Insert data
        Log.w("LOGIN_ACTIVITY_PROVIDER", "Inserting data!");
        getContentResolver().insert(AppData.Users.CONTENT_URI, null);

        // Choose which columns to get
        String[] PROJECTION = new String[] { BaseColumns._ID, AppData.Users.COLUMN_NAME_EMAIL };

        // ContentProviders are accessed through ContentResolver objects
        Cursor mCursor = getContentResolver().query(AppData.Users.CONTENT_URI, PROJECTION, null, null, null);

        if (mCursor == null) {
            Log.e("LOGIN_ACTIVITY_PROVIDER", "Cursor is null!");
        }

        else if (mCursor.getCount() < 1) {
            Log.e("LOGIN_ACTIVITY_PROVIDER", "Cursor is empty!");
        }

        else if (mCursor.moveToFirst()) {
            Log.w("LOGIN_ACTIVITY_PROVIDER", "Cursor has stuff in it.");

            // Loop through the cursor
            do {
                Log.w("LOGIN_ACTIVITY_PROVIDER", mCursor.getString(0) + " " + mCursor.getString(1));
            } while (mCursor.moveToNext());
        }

        // Try login stuff

        // Callbacks?!
        Log.d("LOGIN_ACTIVITY", "Before callbacky stuff");
        Message message = new Message();

        // Create a callback
        // TODO set data to be passed to callback
        Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                Log.d("LOGIN_ACTIVITY", "Inside handle message");
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

//        Handler handler = new Handler(callback);
//        handler.sendMessage(message);
//        try {
//            Promise<HttpURLConnection, HttpURLConnection, Void> p1 = new DeferredHttpUrlConnection(
//                    new URL("http://www.google.com"));
//
//
//            p1.done(this).fail(this);
//
//            DeferredObject
//                    .when(p1, new DeferredHttpUrlConnection(new URL("http://1")))
//                    .done(new ResolveCallback<MergedPromiseResult2<HttpURLConnection, HttpURLConnection>>() {
//                        @Override
//                        public void onResolve(
//                                MergedPromiseResult2<HttpURLConnection, HttpURLConnection> resolved) {
//                            Log.i(TAG, "Done for both!" + resolved._1 + ", " + resolved._2);
//                        }
//                    }).fail(new RejectCallback<MergedPromiseReject>() {
//
//                @Override
//                public void onReject(MergedPromiseReject rejected) {
//                    Log.e(TAG, "One failed: " + rejected.rejected + " index is "
//                            + rejected.index);
//                }
//            });
//        }
//
//        catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }



        try {
            AccountManager.loginWithEmail("sahil29@gmail.com", "1234", callback);
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

//        try {
//            if (AccountManager.loginWithEmail(deferred, "sahil29@gmail.com", "1234")) {
//                Log.w("LOGIN_ACTIVITY_PROVIDER", "Logged in!");
//            } else {
//                Log.w("LOGIN_ACTIVITY_PROVIDER", "WTF!");
//            }
//        } catch (JSONException e) {
//            Log.w("LOGIN_ACTIVITY_PROVIDER", "WHOOPSSSSIEE");
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

        // Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

    @Override
    public void onResolve(HttpURLConnection resolved) {
        Log.d(TAG, "Done: " + resolved);
    }

    @Override
    public void onReject(HttpURLConnection rejected) {
        Log.w(TAG, "Fail: " + rejected);

    }

}
