package com.onemore.karungguniapp;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

public class AppDataProvider extends ContentProvider{

    // Tag for debugging and logging
    private static final String TAG = "AppDataProvider";

    // Handle to db helper object
    private MainDatabaseHelper mOpenHelper;

    // Database name
    private static final String DBNAME = "kgdata";

    // Holds the database object
    private SQLiteDatabase db;

    // Projection map for selecting columns
    private static HashMap<String, String> sUsersProjectionMap;
    private static HashMap<String, String> sSellersProjectionMap;
    private static HashMap<String, String> sKarungGunisProjectionMap;
    private static HashMap<String, String> sAdvertisementsProjectionMap;

    // Static objects block
    static {
        // Create projection map that returns all columns
        sUsersProjectionMap = new HashMap<String, String>();
        sUsersProjectionMap.put(AppData.Users._ID, AppData.Users._ID);
        sUsersProjectionMap.put(AppData.Users.COLUMN_NAME_EMAIL, AppData.Users.COLUMN_NAME_EMAIL);
        sUsersProjectionMap.put(AppData.COLUMN_NAME_DATE_CREATED, AppData.COLUMN_NAME_DATE_CREATED);

        // Create projection map that returns all columns for Sellers
        sSellersProjectionMap = new HashMap<String, String>();
        sSellersProjectionMap.put(AppData.Sellers._ID, AppData.Sellers._ID);
        sSellersProjectionMap.put(AppData.Sellers.COLUMN_NAME_EMAIL, AppData.Sellers.COLUMN_NAME_EMAIL);
        sSellersProjectionMap.put(AppData.Sellers.COLUMN_NAME_DISPLAY_NAME, AppData.Sellers.COLUMN_NAME_DISPLAY_NAME);
        sSellersProjectionMap.put(AppData.Sellers.COLUMN_NAME_ADDRESS, AppData.Sellers.COLUMN_NAME_ADDRESS);
        sSellersProjectionMap.put(AppData.Sellers.COLUMN_NAME_ADDRESS_LAT, AppData.Sellers.COLUMN_NAME_ADDRESS_LAT);
        sSellersProjectionMap.put(AppData.Sellers.COLUMN_NAME_ADDRESS_LONG, AppData.Sellers.COLUMN_NAME_ADDRESS_LONG);
        sSellersProjectionMap.put(AppData.COLUMN_NAME_DATE_CREATED, AppData.COLUMN_NAME_DATE_CREATED);

        // Create projection map that returns all columns for Karung Gunis
        sKarungGunisProjectionMap = new HashMap<String, String>();
        sKarungGunisProjectionMap.put(AppData.KarungGunis._ID, AppData.KarungGunis._ID);
        sKarungGunisProjectionMap.put(AppData.KarungGunis.COLUMN_NAME_EMAIL, AppData.KarungGunis.COLUMN_NAME_EMAIL);
        sKarungGunisProjectionMap.put(AppData.KarungGunis.COLUMN_NAME_DISPLAY_NAME, AppData.KarungGunis.COLUMN_NAME_DISPLAY_NAME);
        sKarungGunisProjectionMap.put(AppData.KarungGunis.COLUMN_NAME_RATING, AppData.KarungGunis.COLUMN_NAME_RATING);
        sKarungGunisProjectionMap.put(AppData.COLUMN_NAME_DATE_CREATED, AppData.COLUMN_NAME_DATE_CREATED);

        // Create projection map that returns all columns
        sAdvertisementsProjectionMap = new HashMap<String, String>();
        sAdvertisementsProjectionMap.put(AppData.Advertisements._ID, AppData.Advertisements._ID);
        sAdvertisementsProjectionMap.put(AppData.COLUMN_NAME_DATE_CREATED, AppData.COLUMN_NAME_DATE_CREATED);
        sAdvertisementsProjectionMap.put(AppData.Advertisements.COLUMN_NAME_OWNER, AppData.Advertisements.COLUMN_NAME_OWNER);
        sAdvertisementsProjectionMap.put(AppData.Advertisements.COLUMN_NAME_TITLE, AppData.Advertisements.COLUMN_NAME_TITLE);
        sAdvertisementsProjectionMap.put(AppData.Advertisements.COLUMN_NAME_DESCRIPTION, AppData.Advertisements.COLUMN_NAME_DESCRIPTION);
        sAdvertisementsProjectionMap.put(AppData.Advertisements.COLUMN_NAME_PHOTO, AppData.Advertisements.COLUMN_NAME_PHOTO);
        sAdvertisementsProjectionMap.put(AppData.Advertisements.COLUMN_NAME_CATEGORY, AppData.Advertisements.COLUMN_NAME_CATEGORY);
        sAdvertisementsProjectionMap.put(AppData.Advertisements.COLUMN_NAME_STATUS, AppData.Advertisements.COLUMN_NAME_STATUS);
        sAdvertisementsProjectionMap.put(AppData.Advertisements.COLUMN_NAME_TIMING_START, AppData.Advertisements.COLUMN_NAME_TIMING_START);
        sAdvertisementsProjectionMap.put(AppData.Advertisements.COLUMN_NAME_TIMING_END, AppData.Advertisements.COLUMN_NAME_TIMING_END);
        sAdvertisementsProjectionMap.put(AppData.Advertisements.COLUMN_NAME_DISTANCE, AppData.Advertisements.COLUMN_NAME_DISTANCE);
    }

    public boolean onCreate() {
        // Create a new helper object
        // Database is created/opened later when SQLiteOpenHelper.getWritableDatabase is called
        mOpenHelper = new MainDatabaseHelper(getContext());

        return true;
    }

    @Override
    public String getType(Uri uri) {

        /**
         * Chooses the MIME type based on the incoming URI pattern
         */
        switch (AppDataUriMatcher.sUriMatcher.match(uri)) {

            // If the pattern is for users, returns the general content type.
            case AppDataUriMatcher.USERS:
                return AppData.Users.CONTENT_TYPE;

            // If the pattern is for note IDs, returns the note ID content type.
            case AppDataUriMatcher.USER_ID:
                return AppData.Users.CONTENT_ITEM_TYPE;

            // If the URI pattern doesn't match any permitted patterns, throws an exception.
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Constructs a new query builder and sets its table name
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String defaultSortOrder;

        // Pattern matching
        switch (AppDataUriMatcher.sUriMatcher.match(uri)) {
            case AppDataUriMatcher.USERS:
                qb.setTables(AppData.Users.TABLE_NAME);
                qb.setProjectionMap(sUsersProjectionMap);
                defaultSortOrder = AppData.Users.DEFAULT_SORT_ORDER;
                break;

            case AppDataUriMatcher.USER_ID:
                qb.setTables(AppData.Users.TABLE_NAME);
                qb.setProjectionMap(sUsersProjectionMap);
                qb.appendWhere(AppData.Users.COLUMN_NAME_EMAIL + "=" + uri.getPathSegments().get(AppData.Users.PATH_POSITION));
                defaultSortOrder = AppData.Users.DEFAULT_SORT_ORDER;
                break;

            case AppDataUriMatcher.SELLERS:
                qb.setTables(AppData.Sellers.TABLE_NAME);
                qb.setProjectionMap(sSellersProjectionMap);
                defaultSortOrder = AppData.Sellers.DEFAULT_SORT_ORDER;
                break;

            case AppDataUriMatcher.SELLER_ID:
                qb.setTables(AppData.Sellers.TABLE_NAME);
                qb.setProjectionMap(sSellersProjectionMap);
                qb.appendWhere(AppData.Sellers.COLUMN_NAME_EMAIL + "=" + uri.getPathSegments().get(AppData.Sellers.PATH_POSITION));
                defaultSortOrder = AppData.Sellers.DEFAULT_SORT_ORDER;
                break;

            case AppDataUriMatcher.KARUNG_GUNIS:
                qb.setTables(AppData.KarungGunis.TABLE_NAME);
                qb.setProjectionMap(sKarungGunisProjectionMap);
                defaultSortOrder = AppData.KarungGunis.DEFAULT_SORT_ORDER;
                break;

            case AppDataUriMatcher.KARUNG_GUNI_ID:
                qb.setTables(AppData.KarungGunis.TABLE_NAME);
                qb.setProjectionMap(sKarungGunisProjectionMap);
                qb.appendWhere(AppData.KarungGunis.COLUMN_NAME_EMAIL + "=" + uri.getPathSegments().get(AppData.KarungGunis.PATH_POSITION));
                defaultSortOrder = AppData.KarungGunis.DEFAULT_SORT_ORDER;
                break;

            case AppDataUriMatcher.ADVERTISEMENTS:
                qb.setTables(AppData.Advertisements.TABLE_NAME);
                qb.setProjectionMap(sAdvertisementsProjectionMap);
                defaultSortOrder = AppData.Advertisements.DEFAULT_SORT_ORDER;
                break;

            case AppDataUriMatcher.ADVERTISEMENT_ID:
                qb.setTables(AppData.Advertisements.TABLE_NAME);
                qb.setProjectionMap(sAdvertisementsProjectionMap);
                qb.appendWhere(AppData.Advertisements._ID + "=" + uri.getPathSegments().get(AppData.Advertisements.PATH_POSITION));
                defaultSortOrder = AppData.Advertisements.DEFAULT_SORT_ORDER;
                break;

            case AppDataUriMatcher.REQUESTS:
                qb.setTables(AppData.Requests.TABLE_NAME);
                qb.setProjectionMap(sAdvertisementsProjectionMap);
                defaultSortOrder = AppData.Requests.DEFAULT_SORT_ORDER;
                break;

            case AppDataUriMatcher.REQUEST_ID:
                qb.setTables(AppData.Requests.TABLE_NAME);
                qb.setProjectionMap(sAdvertisementsProjectionMap);
                qb.appendWhere(AppData.Requests._ID + "=" + uri.getPathSegments().get(AppData.Requests.PATH_POSITION));
                defaultSortOrder = AppData.Requests.DEFAULT_SORT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Opens the database object in "read" mode, since no writes need to be done.
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        // Performs the query
        // Cursor is null if error occurs while reading the database
        Cursor c = qb.query(
                db,
                projection,
                selection,
                selectionArgs,
                null, // groupBy
                null, // having
                sortOrder != null ? sortOrder : defaultSortOrder);

        // Tells the Cursor what URI to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;  //To change body of implemented methods use File | Settings | File Templates.
    }

    // Insert data
    // Values must not be null as there are no defaults specified
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // Return if no ContentValues provided
        if (values == null) {
            return null;
        }

        String tableName;
        Uri baseUri, resultUri = null;

        // Match the incoming URI to a table name
        // Also set the uri base which is used in the URI returned by this method
        switch (AppDataUriMatcher.sUriMatcher.match(uri)) {
            case AppDataUriMatcher.USERS:
            case AppDataUriMatcher.USER_ID:
                tableName = AppData.Users.TABLE_NAME;
                baseUri = AppData.Users.CONTENT_ID_URI_BASE;
                break;

            case AppDataUriMatcher.KARUNG_GUNIS:
            case AppDataUriMatcher.KARUNG_GUNI_ID:
                tableName = AppData.KarungGunis.TABLE_NAME;
                baseUri = AppData.KarungGunis.CONTENT_ID_URI_BASE;
                break;

            case AppDataUriMatcher.SELLERS:
            case AppDataUriMatcher.SELLER_ID:
                tableName = AppData.Sellers.TABLE_NAME;
                baseUri = AppData.Sellers.CONTENT_ID_URI_BASE;
                break;

            case AppDataUriMatcher.ADVERTISEMENTS:
            case AppDataUriMatcher.ADVERTISEMENT_ID:
                tableName = AppData.Advertisements.TABLE_NAME;
                baseUri = AppData.Advertisements.CONTENT_ID_URI_BASE;
                break;

            case AppDataUriMatcher.REQUESTS:
            case AppDataUriMatcher.REQUEST_ID:
                tableName = AppData.Requests.TABLE_NAME;
                baseUri = AppData.Requests.CONTENT_ID_URI_BASE;
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If no creation date is specified, use the current time
        if (!values.containsKey(AppData.COLUMN_NAME_DATE_CREATED)) {

            // Get the current system time in milliseconds
            Long now = System.currentTimeMillis() / 1000;
            values.put(AppData.COLUMN_NAME_DATE_CREATED, now);
        }

        // Get a writable database (will create if it doesn't exist)
        db = mOpenHelper.getWritableDatabase();

        try {
            // Insert and return ID
            long rowId = db.insert(tableName, AppData.COLUMN_NAME_DATE_MODIFIED, values);

            // If the insert succeeded, the row ID exists.
            if (rowId > 0) {
                // Creates a URI with the note ID pattern and the new row ID appended to it.
                resultUri = ContentUris.withAppendedId(baseUri, rowId);

                // Notifies observers registered against this provider that the data changed.
                getContext().getContentResolver().notifyChange(resultUri, null);
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return resultUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;

        // Get a writable database (will create if it doesn't exist)
        db = mOpenHelper.getWritableDatabase();

        // Match the incoming URI to a table name
        switch (AppDataUriMatcher.sUriMatcher.match(uri)) {
            case AppDataUriMatcher.ADVERTISEMENTS:
            case AppDataUriMatcher.ADVERTISEMENT_ID:
                try {
                    rowsUpdated = db.update(AppData.Advertisements.TABLE_NAME, contentValues, selection, selectionArgs);

                    // Notify observers registered against this provider that the data changed.
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                catch (SQLException e) {
                    e.printStackTrace();
                }

                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return rowsUpdated;
    }

    // Helper class to connect to the SQLite database
    static class MainDatabaseHelper extends SQLiteOpenHelper {

        // Instantiates an open helper for the provider's SQLite data repository
        // Do not do database creation and upgrade here.
        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
        }

        // Creates the data repository.
        // Called when the provider attempts to open the repository and SQL reports that it doesn't exist
        public void onCreate(SQLiteDatabase db) {

            // Create the required tables
            db.execSQL(AppData.KarungGunis.CREATE_TABLE_SQL);
            db.execSQL(AppData.Sellers.CREATE_TABLE_SQL);
            db.execSQL(AppData.Advertisements.CREATE_TABLE_SQL);
            db.execSQL(AppData.Requests.CREATE_TABLE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //To change body of implemented methods use File | Settings | File Templates.

            // Logs that the database is being upgraded
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            // Kill the tables and existing data
            db.execSQL("DROP TABLE IF EXISTS " + AppData.KarungGunis.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + AppData.Sellers.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + AppData.Advertisements.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + AppData.Requests.TABLE_NAME);

            // Recreates the database with a new version
            onCreate(db);
        }
    }
}
