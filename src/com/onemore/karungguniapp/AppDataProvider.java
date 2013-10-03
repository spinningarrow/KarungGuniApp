package com.onemore.karungguniapp;

import android.content.*;
import android.database.Cursor;
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

    // URIMatcher
    private static final UriMatcher sUriMatcher;

    // Incoming URI matches
    private static final int USERS = 1; // all users
    private static final int USER_ID = 2; // specific user

    // Static objects block
    static {
        // Create URI Matcher
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(AppData.AUTHORITY, "users", USERS);
        sUriMatcher.addURI(AppData.AUTHORITY, "users/#", USER_ID);

        // Create projection map that returns all columns
        sUsersProjectionMap = new HashMap<String, String>();
        sUsersProjectionMap.put(AppData.Users._ID, AppData.Users._ID);
        sUsersProjectionMap.put(AppData.Users.COLUMN_NAME_EMAIL, AppData.Users.COLUMN_NAME_EMAIL);
        sUsersProjectionMap.put(AppData.Users.COLUMN_NAME_CREATED, AppData.Users.COLUMN_NAME_CREATED);
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
        switch (sUriMatcher.match(uri)) {

            // If the pattern is for users, returns the general content type.
            case USERS:
                return AppData.Users.CONTENT_TYPE;

            // If the pattern is for note IDs, returns the note ID content type.
            case USER_ID:
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
        qb.setTables(AppData.Users.TABLE_NAME);

        // Pattern matching
        switch (sUriMatcher.match(uri)) {
            case USERS:
                qb.setProjectionMap(sUsersProjectionMap);
                break;

            case USER_ID:
                qb.setProjectionMap(sUsersProjectionMap);
                qb.appendWhere(AppData.Users._ID + "=" + uri.getPathSegments().get(AppData.Users.USER_ID_PATH_POSITION));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Opens the database object in "read" mode, since no writes need to be done.
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        // Performs the query
        // Cursor is null if error occurs while reading the database
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, AppData.Users.DEFAULT_SORT_ORDER);

        // Tells the Cursor what URI to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;  //To change body of implemented methods use File | Settings | File Templates.
    }

    // Insert data
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {

        // Validate the incoming URI

        // A map to hold the new record's values.
        ContentValues newValues;

        // If the incoming values map is not null, use it for the new values.
        if (initialValues != null) {
            newValues = new ContentValues(initialValues);

        } else {
            // Otherwise, create a new value map
            newValues = new ContentValues();
        }

        // Gets the current system time in milliseconds
        Long now = System.currentTimeMillis();

        // If the values map doesn't contain the creation date, sets the value to the current time.
        if (!newValues.containsKey(AppData.Users.COLUMN_NAME_CREATED)) {
            newValues.put(AppData.Users.COLUMN_NAME_CREATED, now);
        }

        if (!newValues.containsKey(AppData.Users.COLUMN_NAME_EMAIL)) {
            newValues.put(AppData.Users.COLUMN_NAME_EMAIL, "sahil29@gmail.com");
        }

        if (!newValues.containsKey(AppData.Users.COLUMN_NAME_PASSWORD)) {
            newValues.put(AppData.Users.COLUMN_NAME_PASSWORD, "123456");
        }

        // Gets a writable database (will create if it doesn't exist)
        db = mOpenHelper.getWritableDatabase();

        // Insert and return ID
        long rowId = db.insert(AppData.Users.TABLE_NAME, AppData.Users.COLUMN_NAME_EMAIL, newValues);

        // If the insert succeeded, the row ID exists.
        if (rowId > 0) {
            // Creates a URI with the note ID pattern and the new row ID appended to it.
            Uri userUri = ContentUris.withAppendedId(AppData.Users.CONTENT_ID_URI_BASE, rowId);

            // Notifies observers registered against this provider that the data changed.
            getContext().getContentResolver().notifyChange(userUri, null);
            return userUri;
        }

//        try {
//            throw new SQLException("Failed to insert row into " + uri);
//        } catch (SQLException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
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
            // Create the main table
//            db.execSQL(SQL_CREATE_MAIN_TABLE);

            db.execSQL("CREATE TABLE " + AppData.Users.TABLE_NAME + " ("
                    + AppData.Users._ID + " INTEGER PRIMARY KEY,"
                    + AppData.Users.COLUMN_NAME_EMAIL + " TEXT,"
                    + AppData.Users.COLUMN_NAME_PASSWORD + " TEXT,"
                    + AppData.Users.COLUMN_NAME_CREATED + " INTEGER"
                    + ");");

            /* db.execSQL("CREATE TABLE " + NotePad.Notes.TABLE_NAME + " ("
                   + NotePad.Notes._ID + " INTEGER PRIMARY KEY,"
                   + NotePad.Notes.COLUMN_NAME_TITLE + " TEXT,"
                   + NotePad.Notes.COLUMN_NAME_NOTE + " TEXT,"
                   + NotePad.Notes.COLUMN_NAME_CREATE_DATE + " INTEGER,"
                   + NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE + " INTEGER"
                   + ");"); */
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //To change body of implemented methods use File | Settings | File Templates.

            // Logs that the database is being upgraded
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            // Kills the table and existing data
            db.execSQL("DROP TABLE IF EXISTS users");

            // Recreates the database with a new version
            onCreate(db);
        }
    }
}
