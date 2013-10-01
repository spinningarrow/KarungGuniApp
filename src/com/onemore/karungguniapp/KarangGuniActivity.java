package com.onemore.karungguniapp;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.MongoURI;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class KarangGuniActivity extends Activity {
	private static TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the text view
	    textView = new TextView(this);
	    textView.setTextSize(40);		
	    new MongoDBAPI().execute("mongodb://heroku:e003eadadbf78ed2d2c62537a1bdbd91@paulo.mongohq.com:10048/app18404502");
	    // Set the text view as the activity layout
	    setContentView(textView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.karang_guni, menu);
		return true;
	}
	
	private class MongoDBAPI extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			DB db;
			try {
				MongoURI mongoURI = new MongoURI(params[0]);
				db = mongoURI.connectDB();
				db.authenticate(mongoURI.getUsername(), mongoURI.getPassword()); 
				//Use the db object to talk to MongoDB
				Set<String> colls = db.getCollectionNames();
				return Arrays.toString(colls.toArray());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			textView.setText(result);
		}
	}
}
