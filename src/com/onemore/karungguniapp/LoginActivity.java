package com.onemore.karungguniapp;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;

public class LoginActivity extends Activity {

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
        Log.w("LOGIN_ACTIVITY", "Trying to log in now...");
        AccountManager.loginWithEmail("sahil29@gmail.com", "1234");

        // Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
}
