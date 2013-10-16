package com.onemore.karungguniapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.onemore.karungguniapp.model.Item;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/14/13
 * Time: 2:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdDetailActivity extends Activity {
    private KGApp app = new KGApp();
    private ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_detail);

        app = (KGApp) getApplication();

        progressBar = (ProgressBar) findViewById(R.id.progress);
//        progressBar.setIndeterminate(true);

        Item item = app.getCurrentItem();

        if (item != null) {
            ImageView icon = (ImageView) findViewById(R.id.item_img);
            new RetrieveImageTask(icon).execute(item.getPicUrl());

            TextView category = (TextView) findViewById(R.id.category);
            //category.setText(item.getCategory());
            category.setText("Newspaper");


            TextView addr_short = (TextView) findViewById(R.id.addr_short);
            //addr_short.setText(item.getLocation());
            addr_short.setText("addr");

        } else {
            Toast.makeText(this, "Error, no current item selected, nothing to see here", Toast.LENGTH_LONG).show();
        }
    }



    private String createDealMessage() {
        Item item = app.getCurrentItem();
        StringBuffer sb = new StringBuffer();
        sb.append("Check out this deal:\n");
        sb.append("\nTitle:" + item.getCategory());
        sb.append("\nLocation:" + item.getLocation());
        return sb.toString();
    }

    private class RetrieveImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public RetrieveImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            Bitmap bitmap = app.retrieveBitmap(args[0]);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressBar.setVisibility(View.GONE);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            }
        }

}
}
