package com.onemore.karungguniapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.onemore.karungguniapp.LBS.GeoUtil;
import com.onemore.karungguniapp.LBS.GetLocationWithGPS;


/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/14/13
 * Time: 2:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdDetailActivity extends Activity
{
    private KGApp app = new KGApp();
//    private LocationManager locationMgr;
//    private GpsListener gpsListener;
//    private Handler handler;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;

    private TextView tv_category;
    private TextView tv_title        ;
    private TextView tv_dist           ;
    private TextView tv_status       ;
    private TextView tv_photo_url    ;
    private TextView tv_description  ;
    private TextView tv_owner        ;
    private TextView tv_timing       ;
    private ImageView photo;
    private  TextView tv_addr;

    private double latitude,longitude;
    private String testAddr;
    public String parseAddress(String addr)
    {
        return addr.replace(" ","+");

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_detail);

        tv_category = (TextView)findViewById(R.id.dt_category);
        tv_title      =(TextView)findViewById(R.id.dt_title)   ;
        tv_dist = (TextView)findViewById(R.id.dt_dist);
        //tv_id         =(TextView)findViewById(R.id)
        //tv_status     =(TextView)findViewById(R.id.dt_status)
        photo  =(ImageView)findViewById(R.id.dt_img_view)  ;
        tv_description=(TextView)findViewById(R.id.dt_description)   ;
        tv_owner      =(TextView)findViewById(R.id.dt_owner) ;
        tv_timing     =(TextView)findViewById(R.id.dt_timing) ;
        tv_addr =(TextView)findViewById(R.id.dt_addr);

        app = (KGApp) getApplication();
//        if(latitude !=-1 && longitude != -1 &&latitude !=0 && longitude != 0 )
//        {
//           double[] seller_location =  GeoUtil.getLatLongFromAddress();
//           testAddr =String.valueOf(seller_location[0] )+"  "+ String.valueOf(seller_location[1]);
//        }
//        else
//            testAddr = "274a jurong west ave 3 #07-61, 641274\n";

        String  category;
        String  title    ;
        String  id ;
        String  status ;
        String  photo_url;
        String  description ;
        String  owner ;
        String  timing ;



        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {

                category     = null;
                title        = null;
                id           = null;
                status       = null;
                photo_url    = null;
                description  = null;
                owner        = null;
                timing       = null;
            } else {
                category      = extras.getString("CATEGORY");
                title         = extras.getString("TITLE");
                id            = extras.getString("ID");
                status        = extras.getString("STATUS");
                photo_url     = extras.getString("PHOTO_URL");
                description   = extras.getString("DESCRIPTION");
                owner         = extras.getString("OWNER");
                timing        = extras.getString("TIMING");


            }
        } else {
            //id = (String) savedInstanceState.getSerializable("ID");
            category      = (String) savedInstanceState.getSerializable("CATEGORY");
            title         = (String) savedInstanceState.getSerializable("TITLE");
            id            = (String) savedInstanceState.getSerializable("ID");
            status        = (String) savedInstanceState.getSerializable("STATUS");
            photo_url     = (String) savedInstanceState.getSerializable("PHOTO_URL");
            description   = (String) savedInstanceState.getSerializable("DESCRIPTION");
            owner         = (String) savedInstanceState.getSerializable("OWNER");
            timing        = (String) savedInstanceState.getSerializable("TIMING");
        }


        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar2 = (ProgressBar) findViewById(R.id.progress2) ;
        progressBar.setIndeterminate(true);
        tv_category.setText(category);
        tv_description.setText(description);
        tv_owner.setText(owner);
        tv_timing.setText(timing);
        tv_title.setText(title);
        tv_addr.setText(testAddr);
        String img_url = "http://res.cloudinary.com/demo/image/upload/sample.jpg";
        //String img_url  = "http://1.bp.blogspot.com/_NNTkR1HgsXA/S-DHrJxSQYI/AAAAAAAAAAM/Cy4cwy-00Y4/S660/ePs_Newspaper+from+your+date+of+birth.jpg";
        new RetrieveImageTask(photo).execute(img_url);
        new RetrieveSellerLocAndCalculateDistance(tv_dist).execute();




//        Advertisement advertisement = app.getCurrentItem();

        //getLoaderManager().initLoader(0, savedInstanceState, this);

//        if (advertisement != null) {
//            ImageView icon = (ImageView) findViewById(R.id.item_img);
//            new RetrieveImageTask(icon).execute(advertisement.getPhotoPath());
//
//            TextView category = (TextView) findViewById(R.id.category);
//            //category.setText(item.getCategory());
//            category.setText("Newspaper");
//
//
//            TextView addr_short = (TextView) findViewById(R.id.addr_short);
//            //addr_short.setText(item.getLocation());
//            addr_short.setText("addr");
//
//        } else {
//            Toast.makeText(this, "Error, no current item selected, nothing to see here", Toast.LENGTH_LONG).show();
//        }
//
    }




//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        Uri baseUri = AppData.Advertisements.CONTENT_URI;
//        // Now create and return a CursorLoader that will take care of
//        // creating a Cursor for the data being displayed.
//        String id;
//
//        if (bundle == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                id= null;
//            } else {
//                id= extras.getString("ID");
//            }
//        } else {
//            id= (String) bundle.getSerializable("ID");
//        }
//
//
////        return new CursorLoader(this, baseUri,
////                null, null, null,
////                null);
//}

//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
//        int x = 1;
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
public class RetrieveSellerLocAndCalculateDistance extends AsyncTask<String, Void, Double> {
    private TextView textView;

    public RetrieveSellerLocAndCalculateDistance(TextView textView) {
        this.textView = textView;
    }


    @Override
    protected Double doInBackground(String... args) {
        Double dist = new Double(0);
        String testAddr = "======================";
        testAddr = "block274a Jurong west avenue 3 Singapore";
        String parsed_addr = parseAddress(testAddr);
        double[] seller_location =  GeoUtil.getLatLongFromAddress(parsed_addr);
        testAddr =String.valueOf(seller_location[0] )+"  "+ String.valueOf(seller_location[1]);
        while( GetLocationWithGPS.gotMyLoc==null);
        if(GetLocationWithGPS.gotMyLoc==GetLocationWithGPS.GET_MY_LOC_SUCCESS) {
            dist  =new Double(GeoUtil.calculateDistance(GetLocationWithGPS.myLoc,seller_location));
        }
        return dist;
    }

    @Override
    protected void onPostExecute(Double dist) {
        progressBar2.setVisibility(View.GONE);
        if (dist != null && dist.doubleValue()!=0.0) {
           String formatted_dist = String.format("%.2f",dist.doubleValue());
            textView.setText(formatted_dist+"m");
            textView.setVisibility(View.VISIBLE);
        }
        else{
            textView.setText(GetLocationWithGPS.gotMyLoc);
            textView.setVisibility(View.VISIBLE);
        }
    }



}


    private class RetrieveImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public RetrieveImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            //Bitmap bitmap = app.retrieveBitmap(args[0]);
//            try {
//                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
//                Bitmap.createScaledBitmap(bitmap,600,600,false) ;
//                photo.setImageBitmap(bitmap);
//                return bitmap;
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            finally {
//                return null;
//            }
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
//        if(locationMgr!=null)
//        {
//        locationMgr.removeGpsStatusListener(gpsListener);
//        }
    }



}

