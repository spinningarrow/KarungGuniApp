package com.onemore.karungguniapp;

import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.widget.TextView;

public class KarungGuniActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_karung_guni);

        TextView tv = new TextView(this);
        tv.setTextSize(40);
        tv.setText(AccountManager.getCurrentUser(this).getString("email"));
        setContentView(tv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.karung_guni, menu);
		return true;
	}

}
