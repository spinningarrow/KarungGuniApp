package com.onemore.karungguniapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class SellerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seller);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.edit_profile:
	            //TODO
	            return true;
	        case R.id.logout:
	            AccountManager.clearCurrentUser(getApplicationContext());
	            Intent i = new Intent(getBaseContext(), Main.class);
	            startActivity(i);
	            finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
