package com.onemore.karungguniapp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import com.onemore.karungguniapp.LBS.ProviderDetail;
import com.onemore.karungguniapp.LBS.GetLocationWithGPS;

import java.util.Locale;

//import com.mongodb.DB;
//import com.mongodb.MongoURI;

public class KarangGuniActivity extends Activity implements AdapterView.OnItemClickListener {

    private int karang_guni;
    private Button getLoc;
//    private Button openGMap;
    //private LocationManager locationMgr;

    public static final String LOG_TAG = "LocationInfo";
    public static final String PROVIDER_NAME = "PROVIDER_NAME";

    //	private static TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		setContentView(R.layout.activity_karang_guni);
		ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.setDisplayShowTitleEnabled(false);

	    Tab tab = actionBar.newTab()
	                       .setText(R.string.current)
	                       .setTabListener(new TabListener<AdvertisementList>(
	                               this, "current", AdvertisementList.class));
	    actionBar.addTab(tab);

	    tab = actionBar.newTab()
	                   .setText(R.string.nearby)
	                   .setTabListener(new TabListener<AdvertisementList>(
	                           this, "nearby", AdvertisementList.class));
	    actionBar.addTab(tab);

        getLoc = (Button) findViewById(R.id.getloc_button);
        getLoc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(KarangGuniActivity.this, GetLocationWithGPS.class));
            }
        });
        //TODO: Gm, delete this function in this file
//        openGMap = (Button) findViewById(R.id.openGMap_button);
//        openGMap.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
////                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
////                startActivity(new Intent(KarangGuniActivity.this, Uri.parse(uri) ));
////                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
////                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
////                context.startActivity(intent);
//            }
//        });

//	    
//		// Create the text view
//	    textView = new TextView(this);
//	    textView.setTextSize(40);		
//	    new MongoDBAPI().execute("mongodb://heroku:e003eadadbf78ed2d2c62537a1bdbd91@paulo.mongohq.com:10048/app18404502");
//	    // Set the text view as the activity layout
//	    setContentView(textView);
	}
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view;
        String providerName = textView.getText().toString();
        Intent intent = new Intent(KarangGuniActivity.this, ProviderDetail.class);
        intent.putExtra(PROVIDER_NAME, providerName);
        startActivity(intent);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        karang_guni = R.menu.karang_guni;
        getMenuInflater().inflate(karang_guni, menu);
		return true;
	}
	
//	private class MongoDBAPI extends AsyncTask<String, Void, String> {
//
//		@Override
//		protected String doInBackground(String... params) {
//			DB db;
//			try {
//				MongoURI mongoURI = new MongoURI(params[0]);
//				db = mongoURI.connectDB();
//				db.authenticate(mongoURI.getUsername(), mongoURI.getPassword()); 
//				//Use the db object to talk to MongoDB
//				Set<String> colls = db.getCollectionNames();
//				return Arrays.toString(colls.toArray());
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		// onPostExecute displays the results of the AsyncTask.
//		@Override
//		protected void onPostExecute(String result) {
//			textView.setText(result);
//		}
//	}
}
