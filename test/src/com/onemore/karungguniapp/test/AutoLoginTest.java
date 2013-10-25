package com.onemore.karungguniapp.test;

import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.onemore.karungguniapp.AccountManager;
import com.onemore.karungguniapp.AppData;
import com.onemore.karungguniapp.KarungGuniActivity;
import com.onemore.karungguniapp.Main;
import com.onemore.karungguniapp.SellerActivity;

public class AutoLoginTest extends ActivityInstrumentationTestCase2<Main> {
	private Main main;
	private Context context;
	private KarungGuniActivity kgActivity;
	private SellerActivity sellerActivity;
	private ActivityMonitor kgMonitor, sellerMonitor;
	private final String email = "kgtest@domain.com";
	private final String role = AppData.ROLE_KG;
//	private final String[] email = {"kgtest@domain.com","sellertest@domain.com"};
//	private final String[] role = {AppData.ROLE_KG,AppData.ROLE_SELLER};
//	private String[] email = TestData.AutoLoginTest.email;
//	private String[] role = TestData.AutoLoginTest.role;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	public AutoLoginTest() {
		super(Main.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		main = getActivity();
		context = main.getApplicationContext();
		AccountManager.clearCurrentUser(context);	// logout the user (if any)
//		prefs = PreferenceManager.getDefaultSharedPreferences(context);
//    	editor = prefs.edit();
//    	editor.clear().commit();
		main.finish();
	}
	
	// Tests that the correct activity is launched depending on the role set in SharedPreferences
	public void testAutoLogin(){

		for (int i = 0; i < 1; i++){
			
			// init activity monitors
			kgMonitor = getInstrumentation().addMonitor(KarungGuniActivity.class.getName(),
			        null, false);
			sellerMonitor = getInstrumentation().addMonitor(SellerActivity.class.getName(),
			        null, false);
			
			// set SharedPreferences
			main = getActivity();
			context = main.getApplicationContext();
			AccountManager.setCurrentUser(context, email, role);
			Bundle bundle = AccountManager.getCurrentUser(context);
			assertEquals("SharedPreferences email not set",bundle.getString("email"),email);
			assertEquals("SharedPreferences role not set",bundle.getString("role"),role);
			main.finish();	
			
			switch (i){
				case 0	:	kgActivity = (KarungGuniActivity)kgMonitor.waitForActivity();
							break;
				case 1	:	sellerActivity = (SellerActivity)sellerMonitor.waitForActivity();
							break;
				default :	Log.e("AutoLoginTest","Number of test iterations incorrect");
			}
			// Relaunch app
			main = getActivity();
			
			// Check if the correct activity was launched
			switch (i){
				case 0	:	assertNotNull("kgActivity is null", kgActivity);
							assertEquals("KarungGuniActivity not launched", 1, kgMonitor.getHits());
							assertEquals("Activity launched is not KarungGuniActivity", KarungGuniActivity.class, kgActivity.getClass());
							break;
				case 1	:	assertNotNull("sellerActivity is null", sellerActivity);
							assertEquals("SellerActivity not launched", 1, sellerMonitor.getHits());
							assertEquals("Activity launched is not SellerActivity", SellerActivity.class, sellerActivity.getClass());
							break;
				default :	Log.e("AutoLoginTest","Number of test iterations incorrect");
			}
			
			// Remove the ActivityMonitor
			getInstrumentation().removeMonitor(kgMonitor);
			getInstrumentation().removeMonitor(sellerMonitor);
			
			AccountManager.clearCurrentUser(context);	// logout the user (if any)
			main.finish();
			
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		AccountManager.clearCurrentUser(context);	// logout the user (if any)
		main.finish();
	}

}
