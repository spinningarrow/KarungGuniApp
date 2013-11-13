package com.onemore.karungguniapp.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Instrumentation;
import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jayway.android.robotium.solo.Solo;
import com.onemore.karungguniapp.AccountManager;
import com.onemore.karungguniapp.AdDetailActivity;
import com.onemore.karungguniapp.AppData;
import com.onemore.karungguniapp.KarungGuniActivity;
import com.onemore.karungguniapp.NewAdActivity;
import com.onemore.karungguniapp.R;
import com.onemore.karungguniapp.SellerActivity;

public class SellerAdManagementTest extends ActivityInstrumentationTestCase2<SellerActivity> {

	Solo solo;
	TextView tv_title, tv_description;
	EditText edit_title, edit_desc;
	DatePicker datePicker;
	TimePicker timePicker;
	Spinner type;
	Button btn_setDate_from, btn_setTime_from, btn_setDate_to, btn_setTime_to, btn_post, ad_post;
	ImageView imageview;
	
	String title, desc, selection;
	String[] selectionArgs = new String[3];
//	String[] projection;
	long startTime, endTime;
	
	public SellerAdManagementTest() {
		super(SellerActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		AccountManager.clearCurrentUser(this.getInstrumentation().getTargetContext());
	}
	
	public void testCreateAd(){
		// TODO: Not Working
		AccountManager.setCurrentUser(this.getInstrumentation().getTargetContext(),"sellertest@domain.com",AppData.ROLE_SELLER);
		solo = new Solo(getInstrumentation(), getActivity());
		solo.assertCurrentActivity("Not SellerActivity", SellerActivity.class);

		solo.clickOnActionBarItem(com.onemore.karungguniapp.R.id.new_ad);
		solo.assertCurrentActivity("Not NewAdActivity", NewAdActivity.class);
		
        btn_setDate_from = (Button) solo.getCurrentActivity().findViewById(R.id.set_date_from);
        btn_setTime_from = (Button) solo.getCurrentActivity().findViewById(R.id.set_time_from);
        btn_setDate_to = (Button) solo.getCurrentActivity().findViewById(R.id.set_date_to);
        btn_setTime_to = (Button) solo.getCurrentActivity().findViewById(R.id.set_time_to);
        btn_post = (Button) solo.getCurrentActivity().findViewById(R.id.ad_post);
        imageview = (ImageView) solo.getCurrentActivity().findViewById(R.id.new_ad_img_view);
        edit_title = (EditText) solo.getCurrentActivity().findViewById(R.id.new_ad_title);
        edit_desc = (EditText) solo.getCurrentActivity().findViewById(R.id.new_add_description);
        datePicker = (DatePicker) solo.getCurrentActivity().findViewById(R.id.datePicker);
        timePicker = (TimePicker) solo.getCurrentActivity().findViewById(R.id.timePicker);
        type = (Spinner) solo.getCurrentActivity().findViewById(R.id.new_ad_type);
		
		solo.enterText(edit_title, "testTitle2");
		solo.enterText(edit_desc, "testDesc2");
		
		// save ad details for later comparison
		title = edit_title.getText().toString();
        desc = edit_desc.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        try {
            startTime = dateFormat.parse(btn_setDate_from.getText().toString() + btn_setTime_from.getText().toString()).getTime() / 1000;
            endTime = dateFormat.parse(btn_setDate_to.getText().toString() + btn_setTime_to.getText().toString()).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
		// Create ad
		
		solo.clickOnView(btn_post);
		
		// Verify ad has been created
//		projection = {AppData.Advertisements.COLUMN_NAME_OWNER,
//								AppData.Advertisements.COLUMN_NAME_TITLE, 
//								AppData.Advertisements.COLUMN_NAME_DESCRIPTION};
		selection = AppData.Advertisements.COLUMN_NAME_OWNER+" = ? AND "
						+	AppData.Advertisements.COLUMN_NAME_TITLE+" = ? AND "
						+	AppData.Advertisements.COLUMN_NAME_DESCRIPTION+" = ?";
		selectionArgs[0] = "sellertest@domain.com";
		selectionArgs[1] = "testTitle";
		selectionArgs[2] = "testDesc";
		
		Cursor result = solo.getCurrentActivity().getContentResolver().query(AppData.Advertisements.CONTENT_ID_URI_BASE, null, selection, selectionArgs, null);
		assertNotNull("Error verifying created advertisement", result);
		assertTrue("Advertisement was not created", result.getCount() > 0);
		assertEquals(title,result.getString(result.getColumnIndex(AppData.Advertisements.COLUMN_NAME_TITLE)));
		assertEquals(desc,result.getString(result.getColumnIndex(AppData.Advertisements.COLUMN_NAME_DESCRIPTION)));
		assertEquals(startTime,result.getLong(result.getColumnIndex(AppData.Advertisements.COLUMN_NAME_TIMING_START)));
		assertEquals(endTime,result.getLong(result.getColumnIndex(AppData.Advertisements.COLUMN_NAME_TIMING_END)));
	}
	public void testAdDetails(){
		
		AccountManager.setCurrentUser(this.getInstrumentation().getTargetContext(),"sellertest@domain.com",AppData.ROLE_SELLER);
		solo = new Solo(getInstrumentation(), getActivity());
		solo.assertCurrentActivity("Not SellerActivity", SellerActivity.class);

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
