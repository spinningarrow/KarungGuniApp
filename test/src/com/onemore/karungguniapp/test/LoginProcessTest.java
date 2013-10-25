package com.onemore.karungguniapp.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

import com.onemore.karungguniapp.AccountManager;
import com.onemore.karungguniapp.LoginActivity;

public class LoginProcessTest extends
		ActivityInstrumentationTestCase2<LoginActivity> {
	LoginActivity activity;
	Context context;
	EditText email, password;
	Button submit;
	
	String[] testEmail = {"kgtest@domain.com",
							"accountdoesnotexist@domain.com",
							"kgtest@domain.com",
							"sellertest@domain.com"};
	String[] testPassword = {"wrongpass",
								"password",
								"password",
								"password"};
	
	public LoginProcessTest(String name) {
		super(LoginActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(true);
		activity = getActivity();
		context = activity.getApplicationContext();
		AccountManager.clearCurrentUser(context);
		email = (EditText)activity.findViewById(com.onemore.karungguniapp.R.id.Leditemail);
		password = (EditText)activity.findViewById(com.onemore.karungguniapp.R.id.Leditpw);
		submit = (Button)activity.findViewById(com.onemore.karungguniapp.R.id.login);
	}

	public void testLoginProcess(){
		for (int i = 0; i < testEmail.length; i++){
			email.setText(testEmail[i]);
			password.setText(testPassword[i]);
			
			TouchUtils.clickView(this, submit);
		}
	}
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
