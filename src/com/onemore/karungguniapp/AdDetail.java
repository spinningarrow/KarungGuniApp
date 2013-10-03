package com.onemore.karungguniapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ProgressBar;
import com.onemore.karungguniapp.model.Item;


/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/3/13
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */


public class AdDetail extends Activity {
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertisement);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setIndeterminate(true);

//        Item item =

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

}