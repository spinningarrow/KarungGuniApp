package com.onemore.karungguniapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;
import com.onemore.karungguniapp.AccountManager;
import com.onemore.karungguniapp.AppData;
import com.onemore.karungguniapp.EditProfile;
import com.onemore.karungguniapp.SellerActivity;

//Test edit profile button on SellerActivity action bar
public class EditProfileUITest2 extends ActivityInstrumentationTestCase2<SellerActivity> {

	private Solo solo;
	
	public EditProfileUITest2() {
		super(SellerActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		this.setActivityInitialTouchMode(true);
		AccountManager.clearCurrentUser(this.getInstrumentation().getTargetContext());
	}
	
	public void testEditProfileButton(){
		
		AccountManager.setCurrentUser(this.getInstrumentation().getTargetContext(),"sellertest@domain.com",AppData.ROLE_SELLER);
		solo = new Solo(getInstrumentation(), getActivity());
		solo.assertCurrentActivity("Not SellerActivity", SellerActivity.class);

		solo.clickOnActionBarItem(com.onemore.karungguniapp.R.id.edit_profile);
		assertTrue("EditProfile not launched",solo.waitForActivity(EditProfile.class, 10000));
		solo.assertCurrentActivity("Not EditProfile", EditProfile.class);
		solo.waitForDialogToClose();
		assertEquals(View.VISIBLE,solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.editprofile_label_address).getVisibility());
		assertEquals(View.VISIBLE,solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.editprofile_address).getVisibility());
		
		// Test pre-population of fields
		EditText displayName = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.editprofile_displayname);
		EditText address = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.editprofile_address);
		assertEquals("Display Name", displayName.getText().toString());
		assertEquals("32 Nanyang Crescent, Singapore 637658", address.getText().toString());
	}
	protected void tearDown() throws Exception {
		AccountManager.clearCurrentUser(this.getInstrumentation().getTargetContext());
		solo.finishOpenedActivities();
		super.tearDown();
	}

}
