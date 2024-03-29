package com.onemore.karungguniapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;
import com.onemore.karungguniapp.AccountManager;
import com.onemore.karungguniapp.AppData;
import com.onemore.karungguniapp.EditProfile;
import com.onemore.karungguniapp.KarungGuniActivity;

// Test edit profile button on KarungGuniActivity action bar
public class EditProfileUITest extends ActivityInstrumentationTestCase2<KarungGuniActivity> {

	private Solo solo;
	
	public EditProfileUITest() {
		super(KarungGuniActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		this.setActivityInitialTouchMode(true);
		AccountManager.clearCurrentUser(this.getInstrumentation().getTargetContext());
	}
	
	public void testEditProfileButton(){
		
		AccountManager.setCurrentUser(this.getInstrumentation().getTargetContext(),"kgtest@domain.com",AppData.ROLE_KG);
		solo = new Solo(getInstrumentation(), getActivity());
		solo.assertCurrentActivity("Not KarungGuniActivity", KarungGuniActivity.class);

		solo.clickOnActionBarItem(com.onemore.karungguniapp.R.id.edit_profile);
		assertTrue("EditProfile not launched",solo.waitForActivity(EditProfile.class, 10000));
		solo.assertCurrentActivity("Not EditProfile", EditProfile.class);
		solo.waitForDialogToClose();
		assertEquals(View.GONE,solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.editprofile_label_address).getVisibility());
		assertEquals(View.GONE,solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.editprofile_address).getVisibility());
		
		// Test pre-population of fields
		EditText displayName = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.editprofile_displayname);
		assertEquals("Not Sahil",displayName.getText().toString());
		
		// Input validation test
		solo.enterText(displayName, "");
		solo.clickOnView(solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.editprofile_submit));
		assertEquals(solo.getCurrentActivity().getString(com.onemore.karungguniapp.R.string.editprofile_compulsory),
						displayName.getError().toString());
		
		// Test update of profile details
		solo.enterText(displayName, "Display Name2");
		solo.clickOnView(solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.editprofile_submit));
		solo.waitForDialogToClose();
		solo.goBack();
		solo.clickOnActionBarItem(com.onemore.karungguniapp.R.id.edit_profile);
		solo.waitForDialogToClose();
		assertEquals("Display Name2", displayName.getText().toString());
		
		// Undo update of profile details
		solo.enterText(displayName, "Not Sahil");
		solo.clickOnView(solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.editprofile_submit));
		solo.waitForDialogToClose();
	}
	protected void tearDown() throws Exception {
		AccountManager.clearCurrentUser(this.getInstrumentation().getTargetContext());
		solo.finishOpenedActivities();
		super.tearDown();
	}

}
