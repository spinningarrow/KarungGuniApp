package com.onemore.karungguniapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/26/13
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewAdActivity extends Activity {
    private Button btn_setDate_from;
    private Button btn_setTime_from;
    private Button btn_setDate_to;
    private Button btn_setTime_to;
    private EditText edit_title;
    private EditText edit_desc;
    private TextView tvDisplayDate_from;
    private TextView tvDisplayTime_from;
    private TextView tvDisplayDate_to;
    private TextView tvDisplayTime_to;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;

    static final int DATE_DIALOG_ID1 = 999;
    static final int DATE_DIALOG_ID2 = 989;
    static final int TIME_DIALOG_ID1 = 998;
    static final int TIME_DIALOG_ID2 = 988;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_ad);

        btn_setDate_from = (Button) findViewById(R.id.set_date_from);
        //pickDate.setOnClickListener();
        btn_setTime_from = (Button)findViewById(R.id.set_time_from);
        btn_setDate_to = (Button) findViewById(R.id.set_date_to);
        //pickDate.setOnClickListener();
        btn_setTime_to = (Button)findViewById(R.id.set_time_to);

        edit_title = (EditText) findViewById(R.id.new_ad_title);
        edit_desc = (EditText) findViewById(R.id.new_add_description);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        tvDisplayDate_from = (TextView) findViewById(R.id.tvDate_from);

        tvDisplayTime_from = (TextView) findViewById(R.id.tvTime_from);
        tvDisplayDate_to = (TextView) findViewById(R.id.tvDate_to);

        tvDisplayTime_to = (TextView) findViewById(R.id.tvTime_to);

        setCurrentDateOnView();
        setCurrentTimeOnView();
        addListenerOnButton();

    }
    public void setCurrentDateOnView() {


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate_from.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));
        tvDisplayDate_to.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));

        // set current date into datepicker
        datePicker.init(year, month, day, null);

    }
    public void setCurrentTimeOnView(){


        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        // set current time into textview
        tvDisplayTime_from.setText(
                new StringBuilder().append(pad(hour))
                        .append(":").append(pad(min)));
        tvDisplayTime_to.setText(
                new StringBuilder().append(pad(hour))
                        .append(":").append(pad(min)));

        // set current time into timepicker
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);
    }
    public void addListenerOnButton() {

        btn_setDate_from.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID1);

            }

        });
        btn_setDate_to.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID2);

            }

        });

        btn_setTime_from.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID1);

            }

        });
        btn_setTime_to.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID2);

            }

        });

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID1:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener_from,
                        year, month,day);
            case DATE_DIALOG_ID2:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener_to,
                        year, month,day);


            case TIME_DIALOG_ID1:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener_from, hour, min,false);
            case TIME_DIALOG_ID2:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener_to, hour, min,false);


        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener datePickerListener_from
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            tvDisplayDate_from.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));
            tvDisplayDate_to.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

            // set selected date into datepicker also
            datePicker.init(year, month, day, null);

        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener_to
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            tvDisplayDate_from.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));
            tvDisplayDate_to.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

            // set selected date into datepicker also
            datePicker.init(year, month, day, null);

        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener_from =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    min = selectedMinute;

                    // set current time into textview
                    tvDisplayTime_from.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(min)));

                    // set current time into timepicker
                    timePicker.setCurrentHour(hour);
                    timePicker.setCurrentMinute(min);

                }
            };
    private TimePickerDialog.OnTimeSetListener timePickerListener_to =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    min = selectedMinute;

                    // set current time into textview
                    tvDisplayTime_to.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(min)));

                    // set current time into timepicker
                    timePicker.setCurrentHour(hour);
                    timePicker.setCurrentMinute(min);

                }
            };
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

//    public void onClick(View view){
//        if(view.getId() == R.id.set_date){
//            String t_title = edit_title.getText().toString();
//            String t_des = edit_desc.getText().toString();
//
//        }
//
//    }

//    public void onClick(View v) {
//
//        if (v.getId() == R.id.set_date) {
//
//            String displayName = mDisplayName.getText().toString();
//            String password = mPassword.getText().toString();
//            String email = mEmail.getText().toString();
//            final String role = roles.get(mRole.getSelectedItem());
//
//            boolean invalid = false;
//
//            if (displayName.equals("")) {
//                invalid = true;
//                Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
//            } else if (password.equals("")) {
//                invalid = true;
//                Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
//            } else if (email.equals("")) {
//                invalid = true;
//                Toast.makeText(getApplicationContext(), "Please enter your Email ID", Toast.LENGTH_SHORT).show();
//            }
//
//            if (invalid == false) {
//
//                // Callback for  when a new user is inserted to karung_gunis or sellers table
//                // If the insertion is successful, show the appropriate activity
//                // Also add the user state to the SharedPrefs
//                Handler.Callback createWithEmailCallback = new Handler.Callback() {
//                    Bundle result;
//                    JSONObject user;
////                    Uri uri;
//
//                    @Override
//                    public boolean handleMessage(Message message) {
//                        result = message.getData();
//
//                        if (result.getInt("success") != 1 || result.getInt("status") != 201) {
//                            // Dismiss the progress dialog
//                            signingIn.dismiss();
//                            Log.w("ACCOUNT_MANAGER", "Insert role table error occurred");
//
//                            // Show an error to the user if a user with that email address already exists
//                            if (result.getInt("status") == 409) {
//                                Toast toast = Toast.makeText(getApplicationContext(), R.string.signup_user_exists, Toast.LENGTH_LONG);
//                                toast.show();
//                            }
//                            return false;
//                        }
//
//                        try {
//                            user = RestClient.parseJsonObject(new ByteArrayInputStream(result.getString("response").getBytes("UTF-8")));
//
//                            // Set the current user in the Shared Preferences so it can be used by other activities
//                            AccountManager.setCurrentUser(getApplicationContext(), user.getString("email"), role);
//                            Intent intent = null;
//
//                            if (role.equals(AppData.ROLE_KG)) {
//                                intent = new Intent(getBaseContext(), KarungGuniActivity.class);
//                            }
//
//                            else if (role.equals(AppData.ROLE_SELLER)) {
//                                intent = new Intent(getBaseContext(), SellerActivity.class);
//                            }
//
////                            Intent intent = new Intent(getBaseContext(), Main.class);
//
//                            // Dismiss the progress dialog and start the new activity
//                            signingIn.dismiss();
//                            setResult(RESULT_OK, intent);
//                            finish();
//
//                        }
//
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                            return false;
//                        }
//
//                        catch (IOException e) {
//                            e.printStackTrace();
//                            return false;
//                        }
//
//                        return true;
//                    }
//                };
//
//                // Show a progress dialog to the user
//                signingIn = ProgressDialog.show(this, getString(R.string.signup_progress_title), getString(R.string.signup_progress_message), true);
//
//                // Create a new user with the supplied details
//                AccountManager.createWithEmail(email, password, role, displayName, createWithEmailCallback);
//            }
//        }
//    }
}
