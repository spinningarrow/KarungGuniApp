package com.onemore.karungguniapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/3/13
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, KarangGuniActivity.class);
        startActivity(intent);
        setContentView(R.layout.ad_detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

}