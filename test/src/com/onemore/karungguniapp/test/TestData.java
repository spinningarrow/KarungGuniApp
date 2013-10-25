package com.onemore.karungguniapp.test;

import com.onemore.karungguniapp.AppData;
import com.onemore.karungguniapp.KarungGuniActivity;
import com.onemore.karungguniapp.SellerActivity;

// Contains all data to be referenced during testing
public class TestData {
	
	private TestData(){}
	
	// Test data for AutoLoginTest
	public static final class AutoLoginTest{
		public static final int num = 2;
		public static final String[] email = {"kgtest@domain.com","sellertest@domain.com"};
		public static final String[] role = {AppData.ROLE_KG,AppData.ROLE_SELLER};
		public static final String[] activity = {new KarungGuniActivity().getPackageName()
												, new SellerActivity().getPackageName()};
	}
}
