package com.onemore.karungguniapp;

import android.accounts.Account;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {
        /*
         * Put the data transfer code here.
         */

        Log.w("SYNC_ADAPTER", "Syncing...");
//        android.os.Debug.waitForDebugger();

        // there are a few general tasks your implementation should perform:
        // conencting to a server
        // downloading and uploading data
        JSONArray advertisements;
        Bundle result = RestClient.query(AppData.Advertisements.CONTENT_URI, null, null, null, null, null);

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
                values.put(AppData.Advertisements.COLUMN_NAME_OWNER, advertisement.getString(AppData.Advertisements.COLUMN_NAME_OWNER));
                values.put(AppData.Advertisements.COLUMN_NAME_TITLE, advertisement.getString(AppData.Advertisements.COLUMN_NAME_TITLE));
                values.put(AppData.Advertisements.COLUMN_NAME_DESCRIPTION, advertisement.getString(AppData.Advertisements.COLUMN_NAME_DESCRIPTION));
                values.put(AppData.Advertisements.COLUMN_NAME_PHOTO, advertisement.getString(AppData.Advertisements.COLUMN_NAME_PHOTO));
                values.put(AppData.Advertisements.COLUMN_NAME_CATEGORY, advertisement.getString(AppData.Advertisements.COLUMN_NAME_CATEGORY));
                values.put(AppData.Advertisements.COLUMN_NAME_STATUS, advertisement.getString(AppData.Advertisements.COLUMN_NAME_STATUS));
                values.put(AppData.COLUMN_NAME_DATE_CREATED, advertisement.getString(AppData.COLUMN_NAME_DATE_CREATED));

                // Insert to local DB
                mContentResolver.insert(AppData.Advertisements.CONTENT_ID_URI_BASE, values);
            }

            Log.w("SYNC_ADAPTER", "Inserted.");

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
        // handling data conflicts or determing how current the data is
        // clean up (close connectiongs and clean up temp files and caches)
    }
}
