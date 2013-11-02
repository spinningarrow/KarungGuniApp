package com.onemore.karungguniapp.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.onemore.karungguniapp.AccountManager;
import com.onemore.karungguniapp.AppData;
import com.onemore.karungguniapp.KarungGuniActivity;
import com.onemore.karungguniapp.Main;
import com.onemore.karungguniapp.SellerActivity;

public class AutoLoginTest extends ActivityUnitTestCase<Main> {

	private Intent mLaunchIntent;
	
	private final String emailKG = "kgtest@domain.com";
	private final String roleKG = AppData.ROLE_KG;
	private final String emailSeller = "sellertest@domain.com";
	private final String roleSeller = AppData.ROLE_SELLER;


	public AutoLoginTest() {
		super(Main.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), Main.class);
		AccountManager.clearCurrentUser(this.getInstrumentation().getTargetContext());
		
	}
	
	// Tests that the SellerActivity is launched when logged in as Seller
	public void testAutoLoginSeller(){

		// set SharedPreferences
		AccountManager.setCurrentUser(this.getInstrumentation().getTargetContext(), emailSeller, roleSeller);

		// start Main
		startActivity(mLaunchIntent,null,null);
		
		final Intent launchedIntent = getStartedActivityIntent();
	    assertNotNull("Intent was null", launchedIntent);
	    assertEquals(launchedIntent.getComponent().getClassName(),SellerActivity.class.getName());
	    System.out.println(launchedIntent.getComponent().getClassName());
	    System.out.println(SellerActivity.class.getName());

	}
	
	// Tests that the KarungGuniActivity is launched when logged in as KG
		public void testAutoLoginKG(){

			// set SharedPreferences
			AccountManager.setCurrentUser(this.getInstrumentation().getTargetContext(), emailKG, roleKG);

			// start Main
			startActivity(mLaunchIntent,null,null);
			
			final Intent launchedIntent = getStartedActivityIntent();
		    assertNotNull("Intent was null", launchedIntent);
		    assertEquals(launchedIntent.getComponent().getClassName(),KarungGuniActivity.class.getName());
		    System.out.println(launchedIntent.getComponent().getClassName());
		    System.out.println(KarungGuniActivity.class.getName());

		}

	protected void tearDown() throws Exception {
		super.tearDown();
		AccountManager.clearCurrentUser(this.getInstrumentation().getTargetContext());	// logout the user (if any)
	}

}
