package com.onemore.karungguniapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.omemore.karungguniapp.listview.AdvertisementList;

public class SellerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seller);	

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            AdvertisementList ads = new AdvertisementList();
            String email = AccountManager.getCurrentUser(getApplicationContext()).getString("email");
            Bundle args = new Bundle();
            args.putString("selection", "owner = \"" + email + "\"");
            args.putString("orderby", null);
            args.putString("column", AppData.Advertisements.COLUMN_NAME_DESCRIPTION);	  	 
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            ads.setArguments(args);
            
            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, ads).commit();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.seller, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    int itemId = item.getItemId();
		if (itemId == R.id.edit_profile) {
			//TODO
			return true;
		} 
		else if (itemId == R.id.logout) {
			AccountManager.clearCurrentUser(getApplicationContext());
			Intent i = new Intent(getBaseContext(), Main.class);
			startActivity(i);
			finish();
			return true;
		}
		else if (itemId == R.id.new_ad){
            Intent i = new Intent(SellerActivity.this, NewAdActivity.class);
            startActivity(i);		
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

}
