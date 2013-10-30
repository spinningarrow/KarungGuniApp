package com.onemore.karungguniapp;

import android.app.*;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.cloudinary.Cloudinary;
import com.onemore.karungguniapp.PhotoService.AlbumStorageDirFactory;
import com.onemore.karungguniapp.PhotoService.BaseAlbumDirFactory;
import com.onemore.karungguniapp.PhotoService.FroyoAlbumDirFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.onemore.karungguniapp.PhotoService.PhotoUtil;


/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/26/13
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewAdActivity extends Activity implements OnClickListener {
    private Button btn_setDate_from;
    private Button btn_setTime_from;
    private Button btn_setDate_to;
    private Button btn_setTime_to;
    private Button btn_post;
    private Button btn_uploadPhoto;
    private ImageView imageview;
    private EditText edit_title;
    private EditText edit_desc;
    private TextView tvDisplayDate_from;
    private TextView tvDisplayTime_from;
    private TextView tvDisplayDate_to;
    private TextView tvDisplayTime_to;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Long startTime;
    private Long endTime;
    private Spinner type;
    private final Activity current = this;
    private ProgressDialog dialog = null;
    private KGApp app;
    private static File file;

    // Constants for photo upload request
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_GALLERY = 1;

    // Stream to hold the photo if the user wants to upload one
    InputStream photoInputStream;

    Cloudinary cloudinary;
    Activity self;

    private static HashMap<String, String> types = new HashMap<String, String>();

    static {
        types.put("Newspaper", AdType.NEWSPAPER.toString());
        types.put("Books", AdType.BOOKS.toString());
        types.put("Clothes", AdType.CLOTHES.toString());
        types.put("Magazines", AdType.MAGAZINES.toString());
        types.put("Shoes", AdType.SHOES.toString());
        types.put("Others", AdType.OTHER.toString());
    }

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private String mCurrentPhotoPath;
    private ProgressDialog postingAd;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;

    private static final int DATE_DIALOG_ID1 = 999;
    private static final int DATE_DIALOG_ID2 = 989;
    private static final int TIME_DIALOG_ID1 = 998;
    private static final int TIME_DIALOG_ID2 = 988;
    private static final int CHOOSE_PHOTO_DIALOG = 899;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.new_ad);

        app = (KGApp) getApplication();

        btn_setDate_from = (Button) findViewById(R.id.set_date_from);
        //pickDate.setOnClickListener();
        btn_setTime_from = (Button) findViewById(R.id.set_time_from);
        btn_setDate_to = (Button) findViewById(R.id.set_date_to);
        //pickDate.setOnClickListener();
        btn_setTime_to = (Button) findViewById(R.id.set_time_to);
        btn_post = (Button) findViewById(R.id.ad_post);
        btn_post.setOnClickListener(this);
        imageview = (ImageView) findViewById(R.id.new_ad_img_view);

        edit_title = (EditText) findViewById(R.id.new_ad_title);
        edit_desc = (EditText) findViewById(R.id.new_add_description);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        tvDisplayDate_from = (TextView) findViewById(R.id.tvDate_from);

        tvDisplayTime_from = (TextView) findViewById(R.id.tvTime_from);
        tvDisplayDate_to = (TextView) findViewById(R.id.tvDate_to);

        tvDisplayTime_to = (TextView) findViewById(R.id.tvTime_to);
        type = (Spinner) findViewById(R.id.new_ad_type);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<String>(types.keySet()));

        type.setAdapter(spinnerArrayAdapter);
        btn_uploadPhoto = (Button) findViewById(R.id.new_ad_photo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        setCurrentDateOnView();
        setCurrentTimeOnView();

        addListenerOnButton();

        // Initialize Cloudinary (uses the CLOUDINARY_URL set in AndroidManifest)
        cloudinary = new Cloudinary(getApplicationContext());
    }

    // Handle the results from the photo upload dialog (take a new photo or choose from gallery)
    // Calls the photo upload asynchronous task which uploads the photo to Cloudinary
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                case REQUEST_GALLERY:

                    try {
                        photoInputStream = getContentResolver().openInputStream(imageReturnedIntent.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
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

    public void setCurrentTimeOnView() {

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
        }
        );

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
        btn_uploadPhoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(CHOOSE_PHOTO_DIALOG);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID1:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener_from,
                        year, month, day);
            case DATE_DIALOG_ID2:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener_to,
                        year, month, day);


            case TIME_DIALOG_ID1:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener_from, hour, min, false);
            case TIME_DIALOG_ID2:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener_to, hour, min, false);
            case CHOOSE_PHOTO_DIALOG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.choose_method_to_upload_photo).setItems(R.array.cam_choose_array, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        // Choice: Take a photo with the camera
                        if (which == 0) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, REQUEST_CAMERA);
                        }

                        // Choice: Choose from gallery
                        if (which == 1) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, REQUEST_GALLERY);
                        }
                    }
                });
                return builder.create();
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ad_post) {
            String title = edit_title.getText().toString();
            boolean invalid = false;

            if (title.equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Please enter the title for the new post", Toast.LENGTH_SHORT).show();
            }

            if (invalid == false) {
                new UploadPhotoTask(self).execute(photoInputStream);
            }
        }
    }

    private void createAdvertisement(String title, String description, String photoUrl, String category, Long startTime, Long endTime) {
        String id = "unsynced_" + UUID.randomUUID();
        String ownerEmail = AccountManager.getCurrentUser(this).getString("email");
        String status = "OPEN";

        ContentValues values = new ContentValues();
        values.put(AppData.Advertisements._ID, id);
        values.put(AppData.Advertisements.COLUMN_NAME_OWNER, ownerEmail);
        values.put(AppData.Advertisements.COLUMN_NAME_TITLE, title);
        values.put(AppData.Advertisements.COLUMN_NAME_DESCRIPTION, description);
        values.put(AppData.Advertisements.COLUMN_NAME_PHOTO, photoUrl);
        values.put(AppData.Advertisements.COLUMN_NAME_CATEGORY, category);
        values.put(AppData.Advertisements.COLUMN_NAME_STATUS, status);
        values.put(AppData.Advertisements.COLUMN_NAME_TIMING_START, startTime);
        values.put(AppData.Advertisements.COLUMN_NAME_TIMING_END, endTime);

        getContentResolver().insert(AppData.Advertisements.CONTENT_ID_URI_BASE, values);
    }

    // Upload a photo to the Cloudinary service
    // The photo must be provided as a File object from the camera or file storage or whatever
    // Only the first file provided is considered
    // Returns a JSONObject with the details of the uploaded file (the most useful being the 'public_id')
    private class UploadPhotoTask extends AsyncTask<InputStream, Integer, JSONObject> {
        ProgressDialog createAdProgress;
        Activity activity;

        public UploadPhotoTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            createAdProgress = ProgressDialog.show(activity, activity.getString(R.string.new_ad_uploading_title), activity.getString(R.string.new_ad_uploading_message), true);
        }

        @Override
        protected JSONObject doInBackground(InputStream... inputStreams) {

            if (inputStreams[0] == null) {
                return null;
            }

            JSONObject uploadedImage = null;

            try {
                uploadedImage = cloudinary.uploader().upload(inputStreams[0], Cloudinary.emptyMap());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return uploadedImage;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            if (createAdProgress.isShowing()) {
                createAdProgress.dismiss();
            }

            String title = edit_title.getText().toString();
            String desc = edit_desc.getText().toString();
            String time_from = tvDisplayDate_from.getText().toString() + tvDisplayTime_from.getText().toString();
            String time_to = tvDisplayDate_to.getText().toString() + tvDisplayTime_to.getText().toString();

            // Parse dates and convert to timestamps
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");

            try {
                startTime = dateFormat.parse(time_from).getTime() / 1000;
                endTime = dateFormat.parse(time_to).getTime() / 1000;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            final String category = types.get(type.getSelectedItem());
            String photoUrl = null;

            // Get image URL
            if (result != null) {
                try {
                    photoUrl = cloudinary.url().generate(result.getString("public_id") + ".jpg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            createAdvertisement(title, desc, photoUrl, category, startTime, endTime);
            return;
        }
    }
}
