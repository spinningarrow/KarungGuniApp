package com.onemore.karungguniapp;

import android.accounts.Account;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.omemore.karungguniapp.listview.AdvertisementList;
import com.onemore.karungguniapp.LBS.GetLocationWithGPS;

public class KarungGuniActivity extends Activity {

	public static final String LOG_TAG = "LocationInfo";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_karung_guni);

    	ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.setDisplayShowTitleEnabled(false);

	    Bundle cArgs = new Bundle();
	    cArgs.putString("selection", null);
	    cArgs.putString("orderby", AppData.Advertisements.COLUMN_NAME_TIMING_END);
	    cArgs.putString("column", AppData.Advertisements.COLUMN_NAME_TIMING_END);	    
	    Tab tab = actionBar.newTab()
	                       .setText(R.string.current)
	                       .setTabListener(new TabListener<AdvertisementList>(
	                               this, "current", AdvertisementList.class, cArgs));
	    actionBar.addTab(tab);

	    Bundle nArgs = new Bundle();
	    nArgs.putString("selection", null);
	    nArgs.putString("orderby", AppData.Advertisements.COLUMN_NAME_DISTANCE);
	    nArgs.putString("column", AppData.Advertisements.COLUMN_NAME_DISTANCE);	  	    
	    tab = actionBar.newTab()
	                   .setText(R.string.nearby)
	                   .setTabListener(new TabListener<AdvertisementList>(
	                           this, "nearby", AdvertisementList.class, nArgs));

	    actionBar.addTab(tab);

        startService(new Intent(KarungGuniActivity.this, GetLocationWithGPS.class));


        // Explicitly request a sync if there are no advertisements
        ContentResolver contentResolver = getContentResolver();
        Cursor mCursor = contentResolver.query(AppData.Advertisements.CONTENT_URI, null, null, null, null);

        if (mCursor == null || mCursor.getCount() < 1) {
            Account mAccount = Main.CreateSyncAccount(this);
            Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(contentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(contentResolver.SYNC_EXTRAS_EXPEDITED, true);
            contentResolver.requestSync(mAccount, AppData.AUTHORITY, settingsBundle);
        }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.karung_guni, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    int itemId = item.getItemId();
		if (itemId == R.id.edit_profile) {
            startActivity(new Intent(getBaseContext(), EditProfile.class));
			return true;
		} else if (itemId == R.id.logout) {
			AccountManager.clearCurrentUser(getApplicationContext());
            startActivity(new Intent(getBaseContext(), Main.class));
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
    @Override
    protected void onResume() {
        super.onResume();

    }


}
