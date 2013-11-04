package com.onemore.karungguniapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

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
	}
	protected void tearDown() throws Exception {
		AccountManager.clearCurrentUser(this.getInstrumentation().getTargetContext());
		solo.finishOpenedActivities();
		super.tearDown();
	}

}
