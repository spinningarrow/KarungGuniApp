package com.onemore.karungguniapp;

import android.app.*;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.cloudinary.Cloudinary;
import com.onemore.karungguniapp.PhotoService.AlbumStorageDirFactory;
import com.onemore.karungguniapp.PhotoService.BaseAlbumDirFactory;
import com.onemore.karungguniapp.PhotoService.FroyoAlbumDirFactory;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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
public class NewAdActivity extends Activity implements OnClickListener{
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
    private Spinner type;
    private final Activity current = this;
    private ProgressDialog dialog = null;
    private KGApp app;
    private static File file;

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
    private static final int CHOOSE_PHOTO_DIALOG  = 899;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_ad);

        app = (KGApp) getApplication();

        btn_setDate_from = (Button) findViewById(R.id.set_date_from);
        //pickDate.setOnClickListener();
        btn_setTime_from = (Button)findViewById(R.id.set_time_from);
        btn_setDate_to = (Button) findViewById(R.id.set_date_to);
        //pickDate.setOnClickListener();
        btn_setTime_to = (Button)findViewById(R.id.set_time_to);
        btn_post =(Button)findViewById(R.id.ad_post);
        btn_post.setOnClickListener(this);
        imageview = (ImageView)findViewById(R.id.new_ad_img_view);

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
        btn_uploadPhoto = (Button)findViewById(R.id.new_ad_photo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }


        setCurrentDateOnView();
        setCurrentTimeOnView();

        addListenerOnButton();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    if ( resultCode == RESULT_OK) {
//                        Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
//                        imageview.setImageBitmap(photo);
                          handleCameraPhoto();
                    }
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageview.setImageURI(selectedImage);
                }
                break;
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
            case CHOOSE_PHOTO_DIALOG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.choose_method_to_upload_photo).setItems(R.array.cam_choose_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if(which==0)   {

                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//                            File testfile = new File("/storage/emulated/0/DCIM/Camera/IMG_20131029_221240.jpg");
                            file = dispatchTakePictureIntent(0, takePicture);
                            //startActivityForResult(takePicture, 0);

                            File testUpload = new File("/mnt/sdcard/DCIM/100MEDIA/IMAG0001.jpg");
                            new UploadPhotoTask().execute(testUpload);

//                            cloudinaryTest(new File("/mnt/sdcard/DCIM/100MEDIA/IMAG0001.jpg"));
                        }
                        if(which==1){

                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, 1);
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
            String desc = edit_desc.getText().toString();
            String time_from = tvDisplayDate_from.getText().toString() + tvDisplayTime_from.getText().toString();
            String time_to = tvDisplayDate_to.getText().toString() +      tvDisplayTime_to.getText().toString();
            String timing = time_from+time_to;
            final String category = types.get(type.getSelectedItem());
            String img_url = "drawable/kg_launcher";
            //final String role = roles.get(mRole.getSelectedItem());

            boolean invalid = false;

            if (title.equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Please enter the title for the new post", Toast.LENGTH_SHORT).show();

            }

            if (invalid == false) {

                // Create a new advertisement and post it
                createAdvertisement(title, desc, img_url, category, timing);
            }

        }


    }

    public void cloudinaryTest(File file) {
        Map config = new HashMap();
        config.put("cloud_name", "hsl8yvyi0");
        config.put("api_key", "638233174111431");
        config.put("api_secret", "19YLRLY0ZkMunO7oOJDfmkCNDB0");
        Cloudinary cloudinary = new Cloudinary(config);

        Log.w("NEW AD CLOUDINARY", cloudinary.url().generate("sample.jpg"));
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            cloudinary.uploader().upload(fileInputStream, Cloudinary.emptyMap());

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void createAdvertisement(String title, String description, String photoUrl, String category, String timing) {
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
        values.put(AppData.Advertisements.COLUMN_NAME_TIMING, timing);

        getContentResolver().insert(AppData.Advertisements.CONTENT_ID_URI_BASE, values);
    }

    private void handleCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
            mCurrentPhotoPath = null;
        }

    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

    }
    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = imageview.getWidth();
        int targetH = imageview.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        imageview.setImageBitmap(bitmap);

        imageview.setVisibility(View.VISIBLE);

    }
    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }
    private String getAlbumName() {
        return getString(R.string.album_name);
    }
    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }
    private File dispatchTakePictureIntent(int actionCode, Intent takePictureIntent) {

        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;
        switch(actionCode) {
            case 0:



                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }
                break;

            default:
                break;
        } // switch

         startActivityForResult(takePictureIntent, actionCode);
        return f;
    }

    private class UploadPhotoTask extends AsyncTask<File, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(File... files) {

            // Initialize Cloudinary
            Map config = new HashMap();
            config.put("cloud_name", "hsl8yvyi0");
            config.put("api_key", "638233174111431");
            config.put("api_secret", "19YLRLY0ZkMunO7oOJDfmkCNDB0");
            Cloudinary cloudinary = new Cloudinary(config);

            Log.w("NEW AD CLOUDINARY", cloudinary.url().generate("sample.jpg"));
            FileInputStream fileInputStream;
            JSONObject uploadedImage = null;

            try {
                fileInputStream = new FileInputStream(files[0]);
                uploadedImage = cloudinary.uploader().upload(fileInputStream, Cloudinary.emptyMap());

            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

//            int count = urls.length;
//            long totalSize = 0;
//            for (int i = 0; i < count; i++) {
//                totalSize += Downloader.downloadFile(urls[i]);
//                publishProgress((int) ((i / (float) count) * 100));
//                // Escape early if cancel() is called
//                if (isCancelled()) break;
//            }
            return uploadedImage;
        }
    }
}
