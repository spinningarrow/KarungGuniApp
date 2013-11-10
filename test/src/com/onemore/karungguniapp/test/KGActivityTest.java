package com.onemore.karungguniapp.test;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.jayway.android.robotium.solo.Solo;
import com.onemore.karungguniapp.AccountManager;
import com.onemore.karungguniapp.AppData;
import com.onemore.karungguniapp.AdDetailActivity;
import com.onemore.karungguniapp.KarungGuniActivity;
import com.onemore.karungguniapp.R;

public class KGActivityTest extends  ActivityInstrumentationTestCase2<KarungGuniActivity>  {
	private Solo solo;
	
	public KGActivityTest() {
		super(KarungGuniActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		this.setActivityInitialTouchMode(true);
		AccountManager.clearCurrentUser(this.getInstrumentation().getTargetContext());
	}
	
	/*
public void testOrderby(){
		
		AccountManager.setCurrentUser(this.getInstrumentation().getTargetContext(),"kgtest@domain.com",AppData.ROLE_KG);
		solo = new Solo(getInstrumentation(), getActivity());
		solo.assertCurrentActivity("Not KarungGuniActivity", KarungGuniActivity.class);

		solo.clickOnText("Nearby");
		assertTrue("Orderby",true);

	}
*/

public void testADdetail(){
	
	AccountManager.setCurrentUser(this.getInstrumentation().getTargetContext(),"kgtest@domain.com",AppData.ROLE_KG);
	solo = new Solo(getInstrumentation(), getActivity());
	solo.assertCurrentActivity("Not KarungGuniActivity", KarungGuniActivity.class);

	 ArrayList<ListView> list = solo.getCurrentViews(ListView.class);
	 
	      assertTrue("There are no listviews in this activity.", list.size() > 0);
	    int  chartPosition = 0;  // just to be safe, point at the first item in the list.
	    
	         solo.clickInList(chartPosition, 0);  // Note that "i" identifies the ListView

	   //      assertFalse("AdDetailActivity launched", solo.waitForActivity(AdDetailActivity.class, 10000));

	      
	         solo.waitForActivity("AdDetailActivity");
	
}

	protected void tearDown() throws Exception {
		AccountManager.clearCurrentUser(this.getInstrumentation().getTargetContext());
		solo.finishOpenedActivities();
		super.tearDown();
	}

}
