package com.onemore.karungguniapp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class KarangGuniActivity extends Activity {
    public  static  String LOG_TAG="";
//    private KGApp app;
//	private static TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_karang_guni);

        //app=(KGApp)getApplication();
//      since the title is spelled wrong, I'm gonna merge KarangGuniActisity with KarungGuniActivity manually
        //@TODO : gm, refine this
        TextView tv = new TextView(this);
        tv.setTextSize(40);
//        tv.setText(AccountManager.getCurrentUser(this).getString("email"));
        getActionBar();
        setContentView(tv);
        //ending todo

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
	    
//	    
//		// Create the text view
//	    textView = new TextView(this);
//	    textView.setTextSize(40);		
//	    new MongoDBAPI().execute("mongodb://heroku:e003eadadbf78ed2d2c62537a1bdbd91@paulo.mongohq.com:10048/app18404502");
//	    // Set the text view as the activity layout
//	    setContentView(textView);
	}

	private void doStupidStuff() {
	    ContentValues content = new ContentValues();
//	    content.put(AppData.Advertisements.COLUMN_NAME_CATEGORY, "Newspaper");
	    content.put(AppData.Advertisements.COLUMN_NAME_DESCRIPTION, "Newspapersssssssssssssssss");
//	    content.put(AppData.Advertisements.COLUMN_NAME_OWNER, "1");
//	    content.put(AppData.Advertisements.COLUMN_NAME_PHOTO, "bit.ly/i3lkjsdj");
//	    content.put(AppData.Advertisements.COLUMN_NAME_STATUS, "OPEN");
//	    content.put(AppData.Advertisements.COLUMN_NAME_TIMING, "3-6pm");
	    content.put(AppData.Advertisements.COLUMN_NAME_TITLE, "Selling random stuff");
	    getContentResolver().insert(AppData.Advertisements.CONTENT_URI, content);
	    getContentResolver().insert(AppData.Advertisements.CONTENT_URI, content);
//	    getContentResolver().insert(AppData.Advertisements.CONTENT_URI, content);
//	    getContentResolver().insert(AppData.Advertisements.CONTENT_URI, content);
//	    getContentResolver().insert(AppData.Advertisements.CONTENT_URI, content);
//	    getContentResolver().insert(AppData.Advertisements.CONTENT_URI, content);
//	    getContentResolver().insert(AppData.Advertisements.CONTENT_URI, content);
//	    getContentResolver().insert(AppData.Advertisements.CONTENT_URI, content);
	
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.karang_guni, menu);
        getMenuInflater().inflate(R.menu.menu, menu);
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
