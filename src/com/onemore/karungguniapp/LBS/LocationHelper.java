/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/2/13
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */

package com.onemore.karungguniapp.LBS;

import android.location.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;



public class LocationHelper {

    public static final int MESSAGE_CODE_LOCATION_FOUND = 1;
    public static final int MESSAGE_CODE_LOCATION_NULL = 2;
    public static final int MESSAGE_CODE_PROVIDER_NOT_PRESENT = 3;

    private static final int FIX_RECENT_BUFFER_TIME = 30000;

    private LocationManager locationMgr;
    private LocationListener locationListener;
    private Handler handler;
    private Runnable handlerCallback;
    private String providerName;
    private String logTag;

    /**
     * Construct with a LocationManager, and a Handler to pass back Messages via.
     *
     * @param locationMgr
     * @param handler
     */
    public LocationHelper(LocationManager locationMgr, Handler handler, String logTag) {
        this.locationMgr = locationMgr;
        this.locationListener = new LocationListenerImpl();
        this.handler = handler;
        this.handlerCallback = new Thread() {
            public void run() {
                endListenForLocation(null);
            }
        };

        Criteria criteria = new Criteria();
        // use Criteria to get provider (and could use COARSE, but doesn't work in emulator)
        // (FINE will use EITHER network/gps, whichever is the best enabled match, except in emulator must be gps)
        // (NOTE: network won't work unless enabled - Settings->Location & Security Settings->Use wireless networks)
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        this.providerName = locationMgr.getBestProvider(criteria, true);

        this.logTag = logTag;
    }

    /**
     * Invoke the process of getting the current Location.
     * Expect Messages to be returned via the Handler passed in at construction with results.
     *
     * @param durationSeconds amount of time to poll for location updates
     */
    public void getCurrentLocation(int durationSeconds) {

        if (this.providerName == null) {
            // return 2/0/0 if provider is not enabled
            Log.d(logTag, "Location provideName null, provider is not enabled or not present.");
            sendLocationToHandler(MESSAGE_CODE_PROVIDER_NOT_PRESENT, 0, 0);
            return;
        }

        // first check last KNOWN location (and if the fix is recent enough, use it)
        // NOTE -- this does NOT WORK in the Emulator
        // (if you send a DDMS "manual" time or geo fix, you get correct DATE,
        // but fix time starts at 00:00 and seems to increment by 1 second each time sent)
        // to test this section (getLastLocation being recent enough), you need to use a real device
        Location lastKnown = locationMgr.getLastKnownLocation(providerName);
        if (lastKnown != null && lastKnown.getTime() >= (System.currentTimeMillis() - FIX_RECENT_BUFFER_TIME)) {
            Log.d(logTag, "Last known location recent, using it: " + lastKnown.toString());
            // return lastKnown lat/long on Message via Handler
            sendLocationToHandler(MESSAGE_CODE_LOCATION_FOUND, (int) (lastKnown.getLatitude() * 1e6),
                    (int) (lastKnown.getLongitude() * 1e6));
        } else {
            // last known is relatively old, or doesn't exist, use a LocationListener
            // and wait for a location update for X seconds
            Log.d(logTag, "Last location NOT recent, setting up location listener to get newer update.");
            listenForLocation(providerName, durationSeconds);
        }
    }

    private void sendLocationToHandler(int msgId, int lat, int lon) {
        Message msg = Message.obtain(handler, msgId, lat, lon);
        handler.sendMessage(msg);
    }

    private void listenForLocation(String providerName, int durationSeconds) {
        locationMgr.requestLocationUpdates(providerName, 0, 0, locationListener);
        handler.postDelayed(handlerCallback, durationSeconds * 1000);
    }

    private void endListenForLocation(Location loc) {
        locationMgr.removeUpdates(locationListener);
        handler.removeCallbacks(handlerCallback);
        if (loc != null) {
            sendLocationToHandler(MESSAGE_CODE_LOCATION_FOUND, (int) (loc.getLatitude() * 1e6), (int) (loc.getLongitude() * 1e6));
        } else {
            sendLocationToHandler(MESSAGE_CODE_LOCATION_NULL, 0, 0);
        }
    }

    private class LocationListenerImpl implements LocationListener {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(logTag, "Location status changed to:" + status);
            switch (status) {
                case LocationProvider.AVAILABLE:
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    endListenForLocation(null);
            }
        }

        @Override
        public void onLocationChanged(Location loc) {
            if (loc == null) {
                return;
            }
            Log.d(logTag, "Location changed to:" + loc.toString());
            endListenForLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            endListenForLocation(null);
        }

        @Override
        public void onProviderEnabled(String provider) {
        }
    }
}
