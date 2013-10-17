package com.onemore.karungguniapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.onemore.karungguniapp.model.Item;
import com.onemore.karungguniapp.LBS.LocationHelper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/14/13
 * Time: 2:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdDetailActivity extends Activity {
    private KGApp app = new KGApp();
    private LocationManager locationMgr;
    private GpsListener gpsListener;
    private Handler handler;
    private ProgressBar progressBar;
    private TextView detail;
    private double latitude,longitude;
    private String testAddr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_detail);

        app = (KGApp) getApplication();
        latitude = longitude = -1;
        testAddr = "274a jurong west ave 3 #07-61, 641274\n";

        progressBar = (ProgressBar) findViewById(R.id.progress);
//        progressBar.setIndeterminate(true);

        Item item = app.getCurrentItem();

        if (item != null) {
            ImageView icon = (ImageView) findViewById(R.id.item_img);
            new RetrieveImageTask(icon).execute(item.getPicUrl());

            TextView category = (TextView) findViewById(R.id.category);
            //category.setText(item.getCategory());
            category.setText("Newspaper");


            TextView addr_short = (TextView) findViewById(R.id.addr_short);
            //addr_short.setText(item.getLocation());
            addr_short.setText("addr");

        } else {
            Toast.makeText(this, "Error, no current item selected, nothing to see here", Toast.LENGTH_LONG).show();
        }
        handler = new Handler() {
            public void handleMessage(Message m) {
                Log.d(KarangGuniActivity.LOG_TAG, "Handler returned with message: " + m.toString());

                if (m.what == LocationHelper.MESSAGE_CODE_LOCATION_FOUND) {
//                    detail.setText("HANDLER RETURNED\nlat:" + m.arg1 + "\nlon:" + m.arg2);
                    latitude = m.arg1;
                    longitude = m.arg2;
                    double[] KGLatlng = new double[2];
                    KGLatlng[0] = latitude;
                    KGLatlng[1] = longitude;
                    double[] sellLatLng = getLatLongFromAddress(testAddr);
                    double distance = calculateDistance(KGLatlng,sellLatLng);
                    TextView dist = (TextView)findViewById(R.id.distance);
                    dist.setText(new Double(distance).toString()+" viola");

                } else if (m.what == LocationHelper.MESSAGE_CODE_LOCATION_NULL) {
                    detail.setText("HANDLER RETURNED\nunable to get location");
                } else if (m.what == LocationHelper.MESSAGE_CODE_PROVIDER_NOT_PRESENT) {
                    detail.setText("HANDLER RETURNED\nprovider not present");
                }
            }
        };
    }

    public static double[] getLatLongFromAddress(String addrString) {
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                addrString + "&sensor=false";
        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();
        double lat=0,lng = 0;

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            double[] result = new double[2];
            result[0] = lat;
            result[1] = lng;
            return result;
        }

    }

    private double calculateDistance(double[] pointA, double[] pointB){
        return Math.sqrt(Math.pow(pointA[0]-pointB[0],2)+Math.pow(pointA[1]-pointB[1],2));

    }
    private String createDealMessage() {
        Item item = app.getCurrentItem();
        StringBuffer sb = new StringBuffer();
        sb.append("Check out this deal:\n");
        sb.append("\nTitle:" + item.getCategory());
        sb.append("\nLocation:" + item.getLocation());
        return sb.toString();
    }

    private class RetrieveImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public RetrieveImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            Bitmap bitmap = app.retrieveBitmap(args[0]);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressBar.setVisibility(View.GONE);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            }
        }


}
    @Override
    protected void onPause() {
        super.onPause();
        locationMgr.removeGpsStatusListener(gpsListener);
    }

    private class GpsListener implements GpsStatus.Listener {
        public void onGpsStatusChanged(int event) {
            Log.d("GpsListener", "Status changed to " + event);
//            switch (event) {
//                case GpsStatus.GPS_EVENT_STARTED:
//                    //gpsEvents.setText("GPS_EVENT_STARTED");
//                    break;
//                case GpsStatus.GPS_EVENT_STOPPED:
//                    //gpsEvents.setText("GPS_EVENT_STOPPED");
//                    break;
//
//                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
//                    //gpsStatus = locationMgr.getGpsStatus(gpsStatus);
//                    StringBuilder sb = new StringBuilder();
//                    for (GpsSatellite sat : gpsStatus.getSatellites()) {
//                        sb.append("Satellite N:" + sat.getPrn() + " AZ:" + sat.getAzimuth() + " EL:" + sat.getElevation()
//                                + " S/N:" + sat.getSnr() + "\n");
//                    }
//                    satelliteStatus.setText(sb.toString());
//                    break;
//                case GpsStatus.GPS_EVENT_FIRST_FIX:
//                    gpsEvents.setText("GPS_EVENT_FIRST_FIX");
//                    break;
            }
        }
}
