package com.onemore.karungguniapp.LBS;

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
 * Date: 10/28/13
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeoUtil {
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

    public static double calculateDistance(double[] pointA, double[] pointB){
        return Math.sqrt(Math.pow(pointA[0]-pointB[0],2)+Math.pow(pointA[1]-pointB[1],2));

    }
}
