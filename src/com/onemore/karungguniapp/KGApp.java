package com.onemore.karungguniapp;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/14/13
 * Time: 2:24 AM
 * To change this template use File | Settings | File Templates.
 */

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.util.Log;
import com.cloudinary.Cloudinary;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.onemore.karungguniapp.PhotoService.L;
import com.onemore.karungguniapp.model.Advertisement;
import com.onemore.karungguniapp.model.Section;
import com.parse.Parse;
import com.parse.ParseACL;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class KGApp extends Application {

    private ConnectivityManager cMgr;
    //private DailyDealsFeedParser parser;

    private Section currentSection;
    private Map<Long, Bitmap> imageCache;
    private Advertisement currentItem;
    private Cloudinary cloudinary;

    //
    // getters/setters
    //
//    public DailyDealsFeedParser getParser() {
//        return this.parser;
//    }
//
    public Section getSection() {
        return this.currentSection;
    }

    public Map<Long, Bitmap> getImageCache() {
        return this.imageCache;
    }

    public Advertisement getCurrentItem() {
        return this.currentItem;
    }

    public void setCurrentItem(Advertisement currentItem) {
        this.currentItem = currentItem;
    }

    //
    // lifecycle
    //
    @Override
    public void onCreate() {
        super.onCreate();
        this.cMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        this.parser = new DailyDealsXmlPullFeedParser();
        this.currentSection = new Section();
        this.imageCache = new HashMap<Long, Bitmap>();
        //L.setTag(TAG);
        initUIL();
        initParse();
        initCloudinary();
    }
    private void initUIL() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        L.i("Universal Image Loader initialized");
    }

    private void initParse() {
        String appId = null;
        String clientKey = null;

        try {
            Bundle bundle = getPackageManager()
                    .getApplicationInfo( getPackageName(), PackageManager.GET_META_DATA)
                    .metaData;
            appId = bundle.getString("PARSE_APPLICATION_ID");
            clientKey = bundle.getString("PARSE_CLIENT_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            // fall-thru
        } catch (NullPointerException e) {
            // fall-thru
        }
        if (appId == null || clientKey == null) {
            throw new RuntimeException("Couldn't load Parse meta-data params from manifest");
        }
        /// Parse - Initializes with appId and clientKey from manifest
        Parse.initialize(this, appId, clientKey);

        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
        L.i("Parse initialized");
    }

    private void initCloudinary() {
        // Cloudinary: creating a cloudinary instance using meta-data from manifest
        cloudinary = new Cloudinary(this);
        L.i("Cloudinary initialized");
    }

    @Override
    public void onTerminate() {
        // not guaranteed to be called
        super.onTerminate();
    }

    //
    // helper methods (used by more than one other activity, so placed here, could be util class too)
    //
    public Bitmap retrieveBitmap(String urlString) {
        Log.d(Constants.LOG_TAG, "making HTTP trip for image:" + urlString);
        Bitmap bitmap = null;
        try {
            URL url = new URL(urlString);
            // NOTE, be careful about just doing "url.openStream()"
            // it's a shortcut for openConnection().getInputStream() and doesn't set timeouts
            // (the defaults are "infinite" so it will wait forever if endpoint server is down)
            // do it properly with a few more lines of code . . .
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(5000);
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (MalformedURLException e) {
            Log.e(Constants.LOG_TAG, "Exception loading image, malformed URL", e);
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Exception loading image, IO error", e);
        }
        return bitmap;
    }
    public static KGApp getInstance(Context context) {
        return (KGApp)context.getApplicationContext();
    }
    public Cloudinary getCloudinary() {
        return cloudinary;
    }


    public boolean connectionPresent() {
        NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
        if ((netInfo != null) && (netInfo.getState() != null)) {
            return netInfo.getState().equals(State.CONNECTED);
        }
        return false;
    }
}
