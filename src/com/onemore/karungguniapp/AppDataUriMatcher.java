package com.onemore.karungguniapp;

import android.content.UriMatcher;

public class AppDataUriMatcher {

    // URIMatcher
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);;

    // Incoming URI matches
    public static final int USERS = 1; // all users
    public static final int USER_EMAIL = 2; // specific user

    public static final int ADVERTISEMENTS = 3; // all ads
    public static final int ADVERTISEMENT_ID = 4; // specific ad

    static {
        // TABLE: Users
        sUriMatcher.addURI(AppData.AUTHORITY, "users", USERS);
        sUriMatcher.addURI(AppData.AUTHORITY, "users/*", USER_EMAIL);

        // TABLE: Advertisements
        sUriMatcher.addURI(AppData.AUTHORITY, "advertisements", ADVERTISEMENTS);
        sUriMatcher.addURI(AppData.AUTHORITY, "advertisements/#", ADVERTISEMENT_ID);
    }
}
