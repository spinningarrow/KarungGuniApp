package com.onemore.karungguniapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class KarungGuniActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_karung_guni);

        TextView tv = new TextView(this);
        tv.setTextSize(40);
        tv.setText(AccountManager.getCurrentUser(this).getString("email"));
        getActionBar();
        setContentView(tv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    int itemId = item.getItemId();
		if (itemId == R.id.edit_profile) {
			//TODO
			return true;
		} else if (itemId == R.id.logout) {
			AccountManager.clearCurrentUser(getApplicationContext());
			Intent i = new Intent(getBaseContext(), Main.class);
			startActivity(i);
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

}
