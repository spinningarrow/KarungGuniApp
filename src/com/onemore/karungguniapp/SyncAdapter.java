package com.onemore.karungguniapp;

import android.accounts.Account;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.android.gms.internal.w;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.w("SYNC_ADAPTER", "Syncing...");

        // there are a few general tasks your implementation should perform:
        // conencting to a server
        // downloading and uploading data
        // handling data conflicts or determing how current the data is
        // clean up (close connectiongs and clean up temp files and caches)

        String ADVERTISEMENTS_SELECTION = null;
        String[] ADVERTISEMENTS_SELECTION_ARGS = null;
        Bundle lastSync = getLastSync(this.getContext());

        // If lastSync is not empty, set the required selections
        // TODO selection parameters don't actually work, the query method doesn't use them
        if (lastSync.getLong(AppData.Advertisements.TABLE_NAME) != -1) {
            ADVERTISEMENTS_SELECTION = "/latest/" + lastSync.getLong(AppData.Advertisements.TABLE_NAME);
        }

        // Get advertisement data from the server
        JSONArray advertisements;
        Bundle result = RestClient.query(AppData.Advertisements.CONTENT_URI, null, ADVERTISEMENTS_SELECTION, ADVERTISEMENTS_SELECTION_ARGS, null, null);

        if (result.getInt("status") != 200) {
            return;
        }

        try {
            advertisements = RestClient.parseJsonArray(new ByteArrayInputStream(result.getString("response").getBytes("UTF-8")));

            ContentValues values;
            JSONObject advertisement;

            // Loop through the advertisements
            for (int i = 0; i < advertisements.length(); i++) {
                advertisement = advertisements.getJSONObject(i);

                values = new ContentValues();
                values.put(AppData.Advertisements._ID, advertisement.getString(AppData.Advertisements._ID));
                values.put(AppData.Advertisements.COLUMN_NAME_OWNER, advertisement.getString(AppData.Advertisements.COLUMN_NAME_OWNER));
                values.put(AppData.Advertisements.COLUMN_NAME_TITLE, advertisement.getString(AppData.Advertisements.COLUMN_NAME_TITLE));
                values.put(AppData.Advertisements.COLUMN_NAME_DESCRIPTION, advertisement.getString(AppData.Advertisements.COLUMN_NAME_DESCRIPTION));
                values.put(AppData.Advertisements.COLUMN_NAME_PHOTO, advertisement.getString(AppData.Advertisements.COLUMN_NAME_PHOTO));
                values.put(AppData.Advertisements.COLUMN_NAME_CATEGORY, advertisement.getString(AppData.Advertisements.COLUMN_NAME_CATEGORY));
                values.put(AppData.Advertisements.COLUMN_NAME_STATUS, advertisement.getString(AppData.Advertisements.COLUMN_NAME_STATUS));
                values.put(AppData.Advertisements.COLUMN_NAME_TIMING, advertisement.getString(AppData.Advertisements.COLUMN_NAME_TIMING));
                values.put(AppData.COLUMN_NAME_DATE_CREATED, advertisement.getString(AppData.COLUMN_NAME_DATE_CREATED));

                // Insert to local DB
                mContentResolver.insert(AppData.Advertisements.CONTENT_ID_URI_BASE, values);
            }

            Log.w("SYNC_ADAPTER", "Inserted.");
            // Save sync status
            Long now = System.currentTimeMillis() / 1000;
            setLastSync(getContext(), AppData.Advertisements.TABLE_NAME, now);

            Cursor mCursor = mContentResolver.query(AppData.Advertisements.CONTENT_URI, null, null, null, null);
            Log.w("SYNC_ADAPTER", "Count: " + mCursor.getCount());
        }

        catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public static Bundle getLastSync(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Bundle lastSync = new Bundle();
        lastSync.putLong(AppData.Advertisements.TABLE_NAME, prefs.getLong("lastSync." + AppData.Advertisements.TABLE_NAME, -1));

        return lastSync;
    }

    public static void setLastSync(Context context, String tableName, Long time) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("lastSync." + tableName, time);
        editor.commit();
    }
}
