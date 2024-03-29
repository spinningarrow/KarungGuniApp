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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;
import com.cloudinary.Cloudinary;
import com.onemore.karungguniapp.model.Advertisement;
import com.onemore.karungguniapp.model.Section;

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



    public boolean connectionPresent() {
        NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
        if ((netInfo != null) && (netInfo.getState() != null)) {
            return netInfo.getState().equals(State.CONNECTED);
        }
        return false;
    }
}
