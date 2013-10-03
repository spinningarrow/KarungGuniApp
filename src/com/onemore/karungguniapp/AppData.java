package com.onemore.karungguniapp;

import android.net.Uri;
import android.provider.BaseColumns;

public class AppData {
    public static final String AUTHORITY = "com.onemore.karungguniapp.provider";

    private AppData() {}
    private static final String SCHEME = "content://";

    // Contract for the Users table
    // Schema:
    // _ID
    // EMAIL
    // PASSWORD
    // CREATED
    public static final class Users implements BaseColumns {
        private Users() {}

        public static final String TABLE_NAME = "users";

        // URI
        private static final String PATH_USERS = "/users";
        private static final String PATH_USER_ID = "/users/";
        public static final int USER_ID_PATH_POSITION = 1;

        // Parse URL (with content:// etc)
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_USERS);

        // URI for single user. Callers must append user id to retrieve
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_USER_ID);

        // Pattern for single user matching
        public static final Uri CONTENT_ID_URI_PATTERN  = Uri.parse(SCHEME + AUTHORITY + PATH_USER_ID + "/#");

        // Content types
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.onemore.user";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.onemore.user";

        // Column names (_ID is inherited from BaseColumns)
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_CREATED = "created";

        // Default sort order
        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_CREATED + " DESC";
    }


    // Contract for the Advertisements table
    // TODO Schema:
    // _ID
    // CREATED
    public static final class Advertisements implements BaseColumns {
        private Advertisements() {}

        public static final String TABLE_NAME = "advertisements";

        // URI
        private static final String PATH_ADVERTISEMENTS = "/advertisements";
        private static final String PATH_ADVERTISEMENT_ID = "/advertisements/";
        public static final int ADVERTISEMENT_ID_PATH_POSITION = 1;

        // Parse URL (with content:// etc)
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_ADVERTISEMENTS);

        // URI for single user. Callers must append user id to retrieve
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_ADVERTISEMENT_ID);

        // Pattern for single user matching
        public static final Uri CONTENT_ID_URI_PATTERN  = Uri.parse(SCHEME + AUTHORITY + PATH_ADVERTISEMENT_ID + "/#");

        // Content types
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.onemore.advertisement";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.onemore.advertisement";

        // Column names (_ID is inherited from BaseColumns)
        public static final String COLUMN_NAME_CREATED = "created";

        // Default sort order
        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_CREATED + " DESC";
    }
}
