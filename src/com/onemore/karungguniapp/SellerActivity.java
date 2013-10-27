package com.onemore.karungguniapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SellerActivity extends Activity {
    private Button lbl_seller ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seller);

        lbl_seller= (Button)findViewById(R.id.btn_new);

        addListenerToButtons();

	}

    public void addListenerToButtons()
    {
        lbl_seller.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(SellerActivity.this, NewAdActivity.class);
                startActivity(i);


            }

        });

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
