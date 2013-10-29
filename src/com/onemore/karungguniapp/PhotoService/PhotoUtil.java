package com.onemore.karungguniapp.PhotoService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.onemore.karungguniapp.Constants;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/29/13
 * Time: 4:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class PhotoUtil {
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

}
