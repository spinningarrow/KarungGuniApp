package com.onemore.karungguniapp.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;


import com.onemore.karungguniapp.AccountManager;
import com.onemore.karungguniapp.LoginActivity;
import com.onemore.karungguniapp.Main;

public class LoginButtonTest extends ActivityUnitTestCase<Main> {


	Button signInButton;
	Intent mLaunchIntent;
	
	public LoginButtonTest() {
		super(Main.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		AccountManager.clearCurrentUser(this.getInstrumentation().getTargetContext());
		mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), Main.class);
		

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	// Tests that LoginActivity is launched when Sign in button on Main is clicked
	public void testLoginButton(){
		
		startActivity(mLaunchIntent, null, null);
		signInButton = (Button)getActivity().findViewById(com.onemore.karungguniapp.R.id.signin);
		signInButton.performClick();
		
		final Intent launchedIntent = getStartedActivityIntent();
	    assertNotNull("Intent was null", launchedIntent);
	    assertEquals(launchedIntent.getComponent().getClassName(),LoginActivity.class.getName());
	    System.out.println(launchedIntent.getComponent().getClassName());
	    System.out.println(LoginActivity.class.getName());
		
	}

}
