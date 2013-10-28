/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/2/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */

package com.onemore.karungguniapp.LBS;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import com.onemore.karungguniapp.KarungGuniActivity;

public class GetLocationWithGPS extends Service {

    public static boolean DEBUG =true;

    public static final String LOC_DATA = "LOC_DATA";
    private Context mContext;

    private LocationManager locationMgr;
    private GpsListener gpsListener;
    private GpsStatus gpsStatus;
    private Handler handler;

//    private TextView title;
//    private TextView detail;
//    private TextView gpsEvents;
//    private TextView satelliteStatus;
//    private Button openGMap;

    private double latitude;
    private double longitude;

    @Override
    public void onCreate() {

        super.onCreate();
        mContext = getApplicationContext();
        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsListener = new GpsListener();
        handler = new Handler() {
            public void handleMessage(Message m) {
                Log.d(KarungGuniActivity.LOG_TAG, "Handler returned with message: " + m.toString());
                if (m.what == LocationHelper.MESSAGE_CODE_LOCATION_FOUND) {
                    Toast.makeText(mContext,"HANDLER RETURNED\nlat:" + m.arg1 + "\nlon:" + m.arg2,Toast.LENGTH_LONG);
                    if (DEBUG) Log.v(LOC_DATA, "HANDLER RETURNED\nlat:" + m.arg1 + "\nlon:" + m.arg2);

                    latitude = m.arg1;
                    longitude = m.arg2;
                } else if (m.what == LocationHelper.MESSAGE_CODE_LOCATION_NULL) {
                    //detail.setText("HANDLER RETURNED\nunable to get location");
                    Toast.makeText(mContext,"HANDLER RETURNED\nunable to get location",Toast.LENGTH_LONG);
                    if (DEBUG) Log.v(LOC_DATA, "HANDLER RETURNED\nunable to get location");
                } else if (m.what == LocationHelper.MESSAGE_CODE_PROVIDER_NOT_PRESENT) {
                    //detail.setText("HANDLER RETURNED\nprovider not present");
                    Toast.makeText(mContext,"HANDLER RETURNED\nprovider not present",Toast.LENGTH_LONG);
                    if (DEBUG) Log.v(LOC_DATA, "HANDLER RETURNED\nunable to get location");

                }
            }
        };



        latitude = longitude = -1;
        if (!locationMgr.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS is not enabled")
                    .setMessage("Would you like to go the location settings and enable GPS?").setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    //finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            LocationHelper locationHelper = new LocationHelper(locationMgr, handler, KarungGuniActivity.LOG_TAG);
            locationHelper.getCurrentLocation(30);
        }

        locationMgr.addGpsStatusListener(gpsListener);


}

    @Override
    public IBinder onBind(Intent intent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private class GpsListener implements GpsStatus.Listener {
        public void onGpsStatusChanged(int event) {
            Log.d("GpsListener", "Status changed to " + event);
            switch (event) {
                case GpsStatus.GPS_EVENT_STARTED:
                   // gpsEvents.setText("GPS_EVENT_STARTED");
                   // Toast.makeText(mContext,)
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
//                    gpsEvents.setText("GPS_EVENT_STOPPED");
                    break;

                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
//                    gpsStatus = locationMgr.getGpsStatus(gpsStatus);
//                    StringBuilder sb = new StringBuilder();
//                    for (GpsSatellite sat : gpsStatus.getSatellites()) {
//                        sb.append("Satellite N:" + sat.getPrn() + " AZ:" + sat.getAzimuth() + " EL:" + sat.getElevation()
//                                + " S/N:" + sat.getSnr() + "\n");
//                    }
//                    satelliteStatus.setText(sb.toString());
                    break;
                case GpsStatus.GPS_EVENT_FIRST_FIX:
//                    gpsEvents.setText("GPS_EVENT_FIRST_FIX");
                    break;
            }
        }
    }
}