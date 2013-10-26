package com.onemore.karungguniapp.LBS;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import com.onemore.karungguniapp.R;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/2/13
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProviderDetail extends Activity {

    private LocationManager locationMgr;

    private TextView title;
    private TextView detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_detail);

        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        title = (TextView) findViewById(R.id.title);
        detail = (TextView) findViewById(R.id.detail);
    }
}
