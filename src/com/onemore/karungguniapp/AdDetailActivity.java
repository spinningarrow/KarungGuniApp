package com.onemore.karungguniapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.omemore.karungguniapp.listview.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;


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
	//private ProgressBar progressBar;
	//private ProgressBar progressBar2;

	private TextView tv_category;
	private TextView tv_title        ;
	private TextView tv_dist           ;
	private TextView tv_status       ;
	private TextView tv_photo_url    ;
	private TextView tv_description  ;
	private TextView tv_owner        ;
	//private TextView tv_timing       ;
    private TextView tv_timing_s;
    private ImageView photo;
	private  TextView tv_addr;
	private  Button btn_gmap;

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
		getActionBar().setDisplayHomeAsUpEnabled(true);
		tv_category = (TextView)findViewById(R.id.dt_category);
		tv_title      =(TextView)findViewById(R.id.dt_title);
		//tv_id         =(TextView)findViewById(R.id)
		//tv_status     =(TextView)findViewById(R.id.dt_status)
		photo  =(ImageView)findViewById(R.id.dt_img_view)  ;
		tv_description=(TextView)findViewById(R.id.dt_description);
		tv_owner      =(TextView)findViewById(R.id.dt_owner);
        tv_timing_s     =(TextView)findViewById(R.id.dt_timing_s) ;

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
        String  addr = null;
        String  displayName = null;
		Long startTime;
		Long endTime;

		Bundle extras = savedInstanceState == null ? getIntent().getExtras() : savedInstanceState;

		if (extras == null) {

			category     = null;
			title        = null;
			id           = null;
			status       = null;
			photo_url    = null;
			description  = null;
			owner        = null;
			startTime    = null;
			endTime      = null;
            addr         = null;
            displayName         = null;
		} else {
			category      = extras.getString(AppData.Advertisements.COLUMN_NAME_CATEGORY);
			title         = extras.getString(AppData.Advertisements.COLUMN_NAME_TITLE);
			id            = extras.getString(AppData.Advertisements._ID);
			status        = extras.getString(AppData.Advertisements.COLUMN_NAME_STATUS);
			photo_url     = extras.getString(AppData.Advertisements.COLUMN_NAME_PHOTO);
			description   = extras.getString(AppData.Advertisements.COLUMN_NAME_DESCRIPTION);
			owner         = extras.getString(AppData.Advertisements.COLUMN_NAME_OWNER);

            Cursor mCursor = getContentResolver().query(AppData.Sellers.CONTENT_ID_URI_BASE, null, "EMAIL = ?", new String[] { owner }, null);
            if (mCursor != null && mCursor.getCount() == 1) {
                mCursor.moveToFirst();
                addr = mCursor.getString(mCursor.getColumnIndex(AppData.Sellers.COLUMN_NAME_ADDRESS));
                displayName = mCursor.getString(mCursor.getColumnIndex(AppData.Sellers.COLUMN_NAME_DISPLAY_NAME));
            }
            startTime     = (long) Float.parseFloat(extras.getString(AppData.Advertisements.COLUMN_NAME_TIMING_START));
			endTime     = (long) Float.parseFloat(extras.getString(AppData.Advertisements.COLUMN_NAME_TIMING_END));
		}


		//progressBar = (ProgressBar) findViewById(R.id.progress);
		//progressBar2 = (ProgressBar) findViewById(R.id.progress2) ;
		//progressBar.setIndeterminate(true);
		tv_category.setText(category);
		tv_description.setText(description);
		tv_owner.setText(displayName);
		//tv_timing.setText(startTime.toString() + endTime.toString()); // TODO parse start and end times and show separately

        SimpleDateFormat date_format_s = new SimpleDateFormat("EEE HH:mm");
        Date date = new Date((long) startTime);
        String time1 = date_format_s.format(date);
        SimpleDateFormat date_format_e = new SimpleDateFormat("EEE HH:mm");
        Date date2 = new Date((long) endTime);
        String time2 = date_format_e.format(date2);
        tv_timing_s.setText(time1 + " - " + time2);

//        tv_timing_e.setText(endTime.toString());
		tv_title.setText(title);
//		tv_addr.setText(testAddr);
        tv_addr.setText(addr, TextView.BufferType.SPANNABLE);
		ImageLoader imageLoader=new ImageLoader(this.getApplicationContext());
		imageLoader.DisplayImage(photo_url, photo);



		tv_addr.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				if (latitude !=-1&& longitude!=-1){
					//                    latitude =  1.351909;
					//                    longitude = 103.703675;
					//                    int zoom=16;
					String uri = "https://maps.google.com/maps?saddr=&daddr="+parseAddress(tv_addr.getText().toString());
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
					startActivity(i);

				}else{
					Toast.makeText(getApplicationContext(), "Location Not Available", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(Intent.ACTION_VIEW);
					startActivity(intent);
				}
			}
		});
	}


}

