package com.onemore.karungguniapp;

import android.net.Uri;
import android.provider.BaseColumns;

public class AppData {
    public static final String AUTHORITY = "com.onemore.karungguniapp.provider";

    private AppData() {}
    private static final String SCHEME = "content://";

    // Contracts for the various tables that are used by the application (including ones that may
    // not be synced to the local SQLite database)

    // All tables consist of the following columns:
    // _id [ObjectId (BSON)]
    // date_created [Integer]
    // date_modified [Integer]
    // NOTE: _id is always unique, but the users table gets individual records
    // by email since that is easier
    public static final String COLUMN_NAME_DATE_CREATED = "date_created";
    public static final String COLUMN_NAME_DATE_MODIFIED = "date_modified";

    // Contract for the Users table:
    // email [String]
    // password [String]
    // UNIQUE KEY: email
    public static final class Users implements BaseColumns {
        private Users() {}

        public static final String TABLE_NAME = "users";

        // URI
        private static final String PATH = "/users";
        private static final String PATH_ID_BASE = PATH + "/";
        public static final int PATH_POSITION = 1;

        // Parse URL (with content:// etc)
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH);

        // URI for single user. Callers must append user email to retrieve
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_ID_BASE);

        // Pattern for single user matching
        public static final Uri CONTENT_ID_URI_PATTERN  = Uri.parse(SCHEME + AUTHORITY + PATH_ID_BASE + "/*");

        // Content types
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.onemore.user";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.onemore.user";

        // Column names
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_ROLE = "role"; // role is returned by the REST API but not actually present in the users table

        // Default sort order
        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_DATE_CREATED + " DESC";
    }

    // Contract for the Sellers table:
    // email [String] [unique]
    // display_name [String]
    // address [String]
    public static final class Sellers implements BaseColumns {
        private Sellers() {}

        public static final String TABLE_NAME = "sellers";

        // URI
        private static final String PATH = "/sellers";
        private static final String PATH_ID_BASE = PATH + "/";
        public static final int PATH_POSITION = 1;

        // Parse URL (with content:// etc)
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH);

        // URI for single user. Callers must append id to retrieve
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_ID_BASE);

        // Pattern for single user matching
        public static final Uri CONTENT_ID_URI_PATTERN  = Uri.parse(SCHEME + AUTHORITY + PATH_ID_BASE + "/*");

        // Content types
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.onemore.user";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.onemore.user";

        // Column names
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_DISPLAY_NAME = "display_name";
        public static final String COLUMN_NAME_ADDRESS = "address";

        // Default sort order
        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_DATE_CREATED + " DESC";

        // Create table SQL
        public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY,"
            + COLUMN_NAME_EMAIL + " TEXT,"
            + COLUMN_NAME_ADDRESS + " TEXT,"
            + COLUMN_NAME_DISPLAY_NAME + " TEXT,"
            + COLUMN_NAME_DATE_CREATED + " INTEGER"
            + ");";
    }

    // Contract for the KarungGunis table:
    // email [String] [unique]
    // display_name [String]
    // rating [Int]
    public static final class KarungGunis implements BaseColumns {
        private KarungGunis() {}

        public static final String TABLE_NAME = "karung_gunis";

        // URI
        private static final String PATH = "/karung_gunis";
        private static final String PATH_ID_BASE = PATH + "/";
        public static final int PATH_POSITION = 1;

        // Parse URL (with content:// etc)
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH);

        // URI for single user. Callers must append id to retrieve
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_ID_BASE);

        // Pattern for single user matching
        public static final Uri CONTENT_ID_URI_PATTERN  = Uri.parse(SCHEME + AUTHORITY + PATH_ID_BASE + "/*");

        // Content types
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.onemore.user";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.onemore.user";

        // Column names
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_DISPLAY_NAME = "display_name";
        public static final String COLUMN_NAME_RATING = "rating";

        // Default sort order
        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_DATE_CREATED + " DESC";

        // Create table SQL
        public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY,"
            + COLUMN_NAME_EMAIL + " TEXT,"
            + COLUMN_NAME_RATING + " INTEGER,"
            + COLUMN_NAME_DISPLAY_NAME + " TEXT,"
            + COLUMN_NAME_DATE_CREATED + " INTEGER"
            + ");";
    }

    // Contract for the Advertisements table:
    // owner [ObjectId (BSON)]
    // title [String]
    // description [String]
    // photo_url [String]
    // category [String]
    // status [String]
    // timing [String] TODO check
    public static final class Advertisements implements BaseColumns {
        private Advertisements() {}

        public static final String TABLE_NAME = "advertisements";

        // URI
        private static final String PATH = "/advertisements";
        private static final String PATH_ID_BASE = PATH + "/";
        public static final int PATH_POSITION = 1;

        // Parse URL (with content:// etc)
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH);

        // URI for single user. Callers must append id to retrieve
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_ID_BASE);

        // Pattern for single user matching
        public static final Uri CONTENT_ID_URI_PATTERN  = Uri.parse(SCHEME + AUTHORITY + PATH_ID_BASE + "/#");

        // Content types
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.onemore.advertisement";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.onemore.advertisement";

        // Column names
        public static final String COLUMN_NAME_OWNER = "owner";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PHOTO = "photo_url";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_TIMING = "timing";

        // Default sort order
        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_DATE_CREATED + " DESC";

        // Create table SQL
        public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY,"
            + COLUMN_NAME_OWNER + " TEXT,"
            + COLUMN_NAME_TITLE + " TEXT,"
            + COLUMN_NAME_DESCRIPTION + " TEXT,"
            + COLUMN_NAME_PHOTO + " TEXT,"
            + COLUMN_NAME_CATEGORY + " TEXT,"
            + COLUMN_NAME_STATUS + " TEXT,"
            + COLUMN_NAME_TIMING + " TEXT,"
            + COLUMN_NAME_DATE_CREATED + " INTEGER"
            + ");";
    }

    // Contract for the Requests table:
    // advertisement [ObjectId (BSON)]
    // karung_guni [ObjectId (BSON)]
    // status [String]
    // UNIQUE KEY: advertisement + karung_guni
    public static final class Requests implements BaseColumns {
        private Requests() {}

        public static final String TABLE_NAME = "requests";

        // URI
        private static final String PATH = "/requests";
        private static final String PATH_ID_BASE = PATH + "/";
        public static final int PATH_POSITION = 1;

        // Parse URL (with content:// etc)
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH);

        // URI for single user. Callers must append id to retrieve
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_ID_BASE);

        // Pattern for single user matching
        public static final Uri CONTENT_ID_URI_PATTERN  = Uri.parse(SCHEME + AUTHORITY + PATH_ID_BASE + "/*");

        // Content types
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.onemore.request";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.onemore.request";

        // Column names
        public static final String COLUMN_NAME_ADVERTISEMENT = "advertisement";
        public static final String COLUMN_NAME_KARUNG_GUNI = "karung_guni";
        public static final String COLUMN_NAME_STATUS = "status";

        // Default sort order
        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_DATE_CREATED + " DESC";

        // Create table SQL
        public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY,"
            + COLUMN_NAME_ADVERTISEMENT + " TEXT,"
            + COLUMN_NAME_KARUNG_GUNI + " TEXT,"
            + COLUMN_NAME_STATUS + " TEXT,"
            + COLUMN_NAME_DATE_CREATED + " INTEGER"
            + ");";
    }
}
