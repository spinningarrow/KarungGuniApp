package com.onemore.karungguniapp;

import android.content.UriMatcher;

public class AppDataUriMatcher {

    // URIMatcher
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);;

    // Incoming URI matches
    public static final int USERS = 1; // all users
    public static final int USER_ID = 2; // specific user

    public static final int SELLERS = 5; // all sellers
    public static final int SELLER_ID = 6; // specific seller

    public static final int KARUNG_GUNIS = 7;
    public static final int KARUNG_GUNI_ID = 8;

    public static final int ADVERTISEMENTS = 3; // all ads
    public static final int ADVERTISEMENT_ID = 4; // specific ad

    public static final int REQUESTS = 9;
    public static final int REQUEST_ID = 10;

    static {
        // TABLE: Users
        sUriMatcher.addURI(AppData.AUTHORITY, "users", USERS);
        sUriMatcher.addURI(AppData.AUTHORITY, "users/*", USER_ID);

        // TABLE: Sellers
        sUriMatcher.addURI(AppData.AUTHORITY, "sellers", SELLERS);
        sUriMatcher.addURI(AppData.AUTHORITY, "sellers/*", SELLER_ID);

        // TABLE: Karung Gunis
        sUriMatcher.addURI(AppData.AUTHORITY, "karung_gunis", KARUNG_GUNIS);
        sUriMatcher.addURI(AppData.AUTHORITY, "karung_gunis/*", KARUNG_GUNI_ID);

        // TABLE: Advertisements
        sUriMatcher.addURI(AppData.AUTHORITY, "advertisements", ADVERTISEMENTS);
        sUriMatcher.addURI(AppData.AUTHORITY, "advertisements/#", ADVERTISEMENT_ID);

        // TABLE: Requests
        sUriMatcher.addURI(AppData.AUTHORITY, "requests", REQUESTS);
        sUriMatcher.addURI(AppData.AUTHORITY, "requests/#", REQUEST_ID);
    }
}
