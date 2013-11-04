package com.onemore.karungguniapp.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;
import com.onemore.karungguniapp.AccountManager;
import com.onemore.karungguniapp.KarungGuniActivity;
import com.onemore.karungguniapp.LoginActivity;
import com.onemore.karungguniapp.Main;
import com.onemore.karungguniapp.SellerActivity;

// TODO Not working
// Test cases for successful logins
public class LoginProcessTest extends ActivityInstrumentationTestCase2<Main>{
	
	Solo solo;
	Context context;
	
	EditText email, password;
	View submit;
	
	String[] testEmail = {"kgtest@domain.com",
							"accountdoesnotexist@domain.com",
							"kgtest@domain.com",
							"sellertest@domain.com"};
	String[] testPassword = {"wrongpass",
								"password",
								"password",
								"password"};
	
	public LoginProcessTest() {
		super(Main.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		AccountManager.clearCurrentUser(getInstrumentation().getTargetContext());
		solo = new Solo(getInstrumentation(),getActivity());
	}
	
	public void testLoginProcess_InvalidLogin1(){

		solo.assertCurrentActivity("Current activity not Main", Main.class);
		solo.clickOnView(solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.signin));
		assertTrue("LoginActivity not launched", solo.waitForActivity(LoginActivity.class, 10000));
		
		solo.assertCurrentActivity("Current activity not LoginActivity", LoginActivity.class);
		email = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.Leditemail);
		password = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.Leditpw);
		submit = solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.login);
		assertNotNull(email);
		assertNotNull(password);
		assertNotNull(submit);
		
		int i = 0;
		
		solo.enterText(email, testEmail[i]);
		solo.enterText(password, testPassword[i]);
		solo.clickOnView(submit);
		
		assertFalse("KarungGuniActivity launched", solo.waitForActivity(KarungGuniActivity.class, 2000));
		assertFalse("SellerActivity launched", solo.waitForActivity(SellerActivity.class, 2000));
		solo.assertCurrentActivity("Current activity LoginActivity", LoginActivity.class);
	}
	
	public void testLoginProcess_InvalidLogin2(){

		solo.assertCurrentActivity("Current activity not Main", Main.class);
		solo.clickOnView(solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.signin));
		assertTrue("LoginActivity not launched", solo.waitForActivity(LoginActivity.class, 10000));
		
		solo.assertCurrentActivity("Current activity not LoginActivity", LoginActivity.class);
		email = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.Leditemail);
		password = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.Leditpw);
		submit = solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.login);
		assertNotNull(email);
		assertNotNull(password);
		assertNotNull(submit);
		
		int i = 1;
		
		solo.enterText(email, testEmail[i]);
		solo.enterText(password, testPassword[i]);
		solo.clickOnView(submit);
		
		assertFalse("KarungGuniActivity launched", solo.waitForActivity(KarungGuniActivity.class, 2000));
		assertFalse("SellerActivity launched", solo.waitForActivity(SellerActivity.class, 2000));
		solo.assertCurrentActivity("Current activity LoginActivity", LoginActivity.class);
	}

	public void testLoginProcess_KgLogin(){

		solo.assertCurrentActivity("Current activity not Main", Main.class);
		solo.clickOnView(solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.signin));
		assertTrue("LoginActivity not launched", solo.waitForActivity(LoginActivity.class, 10000));
		
		solo.assertCurrentActivity("Current activity not LoginActivity", LoginActivity.class);
		email = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.Leditemail);
		password = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.Leditpw);
		submit = solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.login);
		assertNotNull(email);
		assertNotNull(password);
		assertNotNull(submit);
		
		int i = 2;
		
		solo.enterText(email, testEmail[i]);
		solo.enterText(password, testPassword[i]);
		solo.clickOnView(submit);
		
		assertTrue("KarungGuniActivity not launched", solo.waitForActivity(KarungGuniActivity.class, 10000));
		solo.assertCurrentActivity("Current activity not KarungGuniActivity", KarungGuniActivity.class);
	}
	
	public void testLoginProcess_SellerLogin(){

		solo.assertCurrentActivity("Current activity not Main", Main.class);
		solo.clickOnView(solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.signin));
		assertTrue("LoginActivity not launched", solo.waitForActivity(LoginActivity.class, 10000));
		
		solo.assertCurrentActivity("Current activity not LoginActivity", LoginActivity.class);
		email = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.Leditemail);
		password = (EditText)solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.Leditpw);
		submit = solo.getCurrentActivity().findViewById(com.onemore.karungguniapp.R.id.login);
		assertNotNull(email);
		assertNotNull(password);
		assertNotNull(submit);
		
		int i = 3;
		
		solo.enterText(email, testEmail[i]);
		solo.enterText(password, testPassword[i]);
		solo.clickOnView(submit);
		
		assertTrue("SellerActivity not launched", solo.waitForActivity(SellerActivity.class, 10000));
		solo.assertCurrentActivity("Current activity not SellerActivity", SellerActivity.class);

	}
	protected void tearDown() throws Exception {
		AccountManager.clearCurrentUser(getInstrumentation().getTargetContext());
		solo.finishOpenedActivities();
		super.tearDown();
	}

}
