package com.onemore.karungguniapp.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jayway.android.robotium.solo.Solo;
import com.onemore.karungguniapp.AccountManager;
import com.onemore.karungguniapp.KarungGuniActivity;
import com.onemore.karungguniapp.SignupActivity;
import com.onemore.karungguniapp.Main;
import com.onemore.karungguniapp.SellerActivity;

// TODO Not working
// Test cases for successful logins
public class SignupActivityTest extends ActivityInstrumentationTestCase2<Main>{
	
	Solo solo;
	Context context;
	
	
     EditText username;
     EditText email;
     EditText password ;
     Spinner role;
	View submit;
	
	String[] TestName={"NEW","NEW","NEW"}	;
	
	String[] testEmail = {"kgtest@domain.com",
							"NEW@domain.com",
							"newKGtest@domain.com"};

	
	public SignupActivityTest() {
		super(Main.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		AccountManager.clearCurrentUser(getInstrumentation().getTargetContext());
		solo = new Solo(getInstrumentation(),getActivity());
	}
	
	public void testSignu_Invalid1(){

		solo.assertCurrentActivity("Current activity not Main", Main.class);
		solo.clickOnView(solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.signup));
		assertTrue("SignupActivity not launched", solo.waitForActivity(SignupActivity.class, 10000));
		
		solo.assertCurrentActivity("Current activity not SignupActivity", SignupActivity.class);
		username = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.reuname);
		email = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.eemail);
		password = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.repass);
		role = (Spinner)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.spinner1);		
		submit = solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.submit);
		assertNotNull(username);
		assertNotNull(email);		
		assertNotNull(password);
		assertNotNull(role);
		assertNotNull(submit);
		
		int i = 0;
		solo.enterText(username, TestName[i]);
		solo.enterText(email, testEmail[i]);
		solo.enterText(password, "password");
		
		solo.clickOnView(submit);
		
		assertFalse("KarungGuniActivity launched", solo.waitForActivity(KarungGuniActivity.class, 2000));
		assertFalse("SellerActivity launched", solo.waitForActivity(SellerActivity.class, 2000));
		solo.assertCurrentActivity("Current activity SignupActivity", SignupActivity.class);
	}
	
	public void testSignupInvalid2(){

		solo.assertCurrentActivity("Current activity not Main", Main.class);
		solo.clickOnView(solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.signup));
		assertTrue("SignupActivity not launched", solo.waitForActivity(SignupActivity.class, 10000));
		
		solo.assertCurrentActivity("Current activity not SignupActivity", SignupActivity.class);
		username = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.reuname);
		email = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.eemail);
		password = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.repass);
		role = (Spinner)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.spinner1);		
		submit = solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.submit);
		assertNotNull(username);
		assertNotNull(email);		
		assertNotNull(password);
		assertNotNull(role);
		assertNotNull(submit);
		
		int i = 1;
		
		solo.enterText(username, TestName[i]);
		solo.enterText(password, "password");
		solo.clickOnView(submit);
		
		assertFalse("KarungGuniActivity launched", solo.waitForActivity(KarungGuniActivity.class, 2000));
		assertFalse("SellerActivity launched", solo.waitForActivity(SellerActivity.class, 2000));
		solo.assertCurrentActivity("Current activity LoginActivity", SignupActivity.class);
	}

	public void testSignup_Kg(){

		solo.assertCurrentActivity("Current activity not Main", Main.class);
		solo.clickOnView(solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.signup));
		assertTrue("SignupActivity not launched", solo.waitForActivity(SignupActivity.class, 10000));
		
		solo.assertCurrentActivity("Current activity not SignupActivity", SignupActivity.class);
		username = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.reuname);
		email = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.eemail);
		password = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.repass);
		role = (Spinner)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.spinner1);		
		submit = solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.submit);
		assertNotNull(username);
		assertNotNull(email);		
		assertNotNull(password);
		assertNotNull(role);
		assertNotNull(submit);
		
		int i = 2;
		solo.enterText(username, TestName[i]);
		solo.enterText(email, testEmail[i]);
		solo.enterText(password, "password");
		solo.clickOnView(submit);
		
		assertTrue("KarungGuniActivity not launched", solo.waitForActivity(KarungGuniActivity.class, 10000));
		solo.assertCurrentActivity("Current activity not KarungGuniActivity", KarungGuniActivity.class);
	}

	protected void tearDown() throws Exception {
		AccountManager.clearCurrentUser(getInstrumentation().getTargetContext());
		solo.finishOpenedActivities();
		super.tearDown();
	}

}
