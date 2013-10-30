package com.onemore.karungguniapp;

import android.accounts.Account;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import com.turbomanage.httpclient.ParameterMap;
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

        // Step 1: Get latest advertisement, seller and kg data from the server
        String ADVERTISEMENTS_SELECTION = null;
        String[] ADVERTISEMENTS_SELECTION_ARGS = null;
        Bundle lastSync = getLastSync(this.getContext());

        // If seller data hasn't been synced, get it now
        if (lastSync.getLong(AppData.Sellers.TABLE_NAME) == -1) {
            JSONArray sellers;
            Bundle sellerResult = RestClient.query(AppData.Sellers.CONTENT_URI, null, null, null, null, null);

            if (sellerResult.getInt("status") == 200) {
                try {
                    sellers = RestClient.parseJsonArray(new ByteArrayInputStream(sellerResult.getString("response").getBytes("UTF-8")));

                    ContentValues values;
                    JSONObject seller;

                    // Loop through the advertisements
                    for (int i = 0; i < sellers.length(); i++) {
                        seller = sellers.getJSONObject(i);

                        values = new ContentValues();
                        values.put(AppData.Sellers._ID, seller.getString(AppData.Sellers._ID));
                        values.put(AppData.Sellers.COLUMN_NAME_EMAIL, seller.getString(AppData.Sellers.COLUMN_NAME_EMAIL));
                        values.put(AppData.Sellers.COLUMN_NAME_DISPLAY_NAME, seller.getString(AppData.Sellers.COLUMN_NAME_DISPLAY_NAME));
                        values.put(AppData.Sellers.COLUMN_NAME_ADDRESS, seller.getString(AppData.Sellers.COLUMN_NAME_ADDRESS));

                        // Insert to local DB
                        mContentResolver.insert(AppData.Sellers.CONTENT_ID_URI_BASE, values);
                    }

                    Log.w("SYNC_ADAPTER", "Inserted into sellers.");
                    // Save sync status
                    Long now = System.currentTimeMillis() / 1000;
                    setLastSync(getContext(), AppData.Sellers.TABLE_NAME, now);

                    Cursor mCursor = mContentResolver.query(AppData.Sellers.CONTENT_URI, null, null, null, null);
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
        }

        // If KG data hasn't been synced, get it now
        if (lastSync.getLong(AppData.Sellers.TABLE_NAME) == -1) {
            JSONArray karunggunis;
            Bundle karungguniResult = RestClient.query(AppData.KarungGunis.CONTENT_URI, null, null, null, null, null);

            if (karungguniResult.getInt("status") == 200) {
                try {
                    karunggunis = RestClient.parseJsonArray(new ByteArrayInputStream(karungguniResult.getString("response").getBytes("UTF-8")));

                    ContentValues values;
                    JSONObject karungguni;

                    // Loop through the advertisements
                    for (int i = 0; i < karunggunis.length(); i++) {
                        karungguni = karunggunis.getJSONObject(i);

                        values = new ContentValues();
                        values.put(AppData.KarungGunis._ID, karungguni.getString(AppData.KarungGunis._ID));
                        values.put(AppData.KarungGunis.COLUMN_NAME_EMAIL, karungguni.getString(AppData.KarungGunis.COLUMN_NAME_EMAIL));
                        values.put(AppData.KarungGunis.COLUMN_NAME_DISPLAY_NAME, karungguni.getString(AppData.KarungGunis.COLUMN_NAME_DISPLAY_NAME));
                        values.put(AppData.KarungGunis.COLUMN_NAME_RATING, karungguni.getString(AppData.KarungGunis.COLUMN_NAME_RATING));

                        // Insert to local DB
                        mContentResolver.insert(AppData.KarungGunis.CONTENT_ID_URI_BASE, values);
                    }

                    Log.w("SYNC_ADAPTER", "Inserted into Karung Gunis.");
                    // Save sync status
                    Long now = System.currentTimeMillis() / 1000;
                    setLastSync(getContext(), AppData.KarungGunis.TABLE_NAME, now);

                    Cursor mCursor = mContentResolver.query(AppData.KarungGunis.CONTENT_URI, null, null, null, null);
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
        }

        // If lastSync is not empty, set the required selections
        if (lastSync.getLong(AppData.Advertisements.TABLE_NAME) != -1) {
            ADVERTISEMENTS_SELECTION = "/latest/" + lastSync.getLong(AppData.Advertisements.TABLE_NAME);
        }

        JSONArray advertisements;
        Bundle result = RestClient.query(AppData.Advertisements.CONTENT_URI, null, ADVERTISEMENTS_SELECTION, ADVERTISEMENTS_SELECTION_ARGS, null, null);

        if (result.getInt("status") == 200) {

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
                    values.put(AppData.Advertisements.COLUMN_NAME_TIMING_START, advertisement.getString(AppData.Advertisements.COLUMN_NAME_TIMING_START));
                    values.put(AppData.Advertisements.COLUMN_NAME_TIMING_END, advertisement.getString(AppData.Advertisements.COLUMN_NAME_TIMING_END));
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

        // Step 2: Publish local advertisement data to the server
        JSONObject advertisement;
        ADVERTISEMENTS_SELECTION = "_ID LIKE 'unsynced_%'";
        Cursor mCursor = mContentResolver.query(AppData.Advertisements.CONTENT_URI, null, ADVERTISEMENTS_SELECTION, null, null);

        if (mCursor != null && mCursor.getCount() > 0 && mCursor.moveToFirst()) {
            Log.w("SYNC_ADAPTER", "Publishing to server");

            // Loop through each row and send the data to the server
            // Update the _ID field to the new value
            ParameterMap params = new ParameterMap();

            do {
                params.put(AppData.Advertisements.COLUMN_NAME_OWNER, mCursor.getString(mCursor.getColumnIndex(AppData.Advertisements.COLUMN_NAME_OWNER)));
                params.put(AppData.Advertisements.COLUMN_NAME_TITLE, mCursor.getString(mCursor.getColumnIndex(AppData.Advertisements.COLUMN_NAME_TITLE)));
                params.put(AppData.Advertisements.COLUMN_NAME_DESCRIPTION, mCursor.getString(mCursor.getColumnIndex(AppData.Advertisements.COLUMN_NAME_DESCRIPTION)));
                params.put(AppData.Advertisements.COLUMN_NAME_PHOTO, mCursor.getString(mCursor.getColumnIndex(AppData.Advertisements.COLUMN_NAME_PHOTO)));
                params.put(AppData.Advertisements.COLUMN_NAME_CATEGORY, mCursor.getString(mCursor.getColumnIndex(AppData.Advertisements.COLUMN_NAME_CATEGORY)));
                params.put(AppData.Advertisements.COLUMN_NAME_STATUS, mCursor.getString(mCursor.getColumnIndex(AppData.Advertisements.COLUMN_NAME_STATUS)));
                params.put(AppData.Advertisements.COLUMN_NAME_TIMING_START, mCursor.getString(mCursor.getColumnIndex(AppData.Advertisements.COLUMN_NAME_TIMING_START)));
                params.put(AppData.Advertisements.COLUMN_NAME_TIMING_END, mCursor.getString(mCursor.getColumnIndex(AppData.Advertisements.COLUMN_NAME_TIMING_END)));
//                params.put(AppData.COLUMN_NAME_DATE_CREATED, mCursor.getString(mCursor.getColumnIndex(AppData.COLUMN_NAME_DATE_CREATED)));

                result = RestClient.insert(AppData.Advertisements.CONTENT_ID_URI_BASE, params, null);

                if (result.getInt("status") == 201) {
                    try {
                        advertisement = RestClient.parseJsonObject(new ByteArrayInputStream(result.getString("response").getBytes("UTF-8")));
                        String _id = advertisement.getString("_id");
                        ContentValues updatedId = new ContentValues();
                        updatedId.put("_id", _id);
                        String[] selectionArgs = new String[] { mCursor.getString(mCursor.getColumnIndex(AppData.Advertisements._ID)) };

                        int rowsUpdated = mContentResolver.update(AppData.Advertisements.CONTENT_ID_URI_BASE, updatedId, "_ID = ?", selectionArgs);

                        if (rowsUpdated == 0) {
                            Log.w("SYNC_ADAPTER", "Updating error occurred ;_;");
                        }
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

            } while (mCursor.moveToNext());
        }
    }

    public static Bundle getLastSync(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Bundle lastSync = new Bundle();
        lastSync.putLong(AppData.Advertisements.TABLE_NAME, prefs.getLong("lastSync." + AppData.Advertisements.TABLE_NAME, -1));
        lastSync.putLong(AppData.Sellers.TABLE_NAME, prefs.getLong("lastSync." + AppData.Sellers.TABLE_NAME, -1));
        lastSync.putLong(AppData.KarungGunis.TABLE_NAME, prefs.getLong("lastSync." + AppData.KarungGunis.TABLE_NAME, -1));
        return lastSync;
    }

    public static void setLastSync(Context context, String tableName, Long time) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("lastSync." + tableName, time);
        editor.commit();
    }
}
