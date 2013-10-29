package com.omemore.karungguniapp.listview;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.onemore.karungguniapp.AdDetailActivity;
import com.onemore.karungguniapp.AppData;
import com.onemore.karungguniapp.KGApp;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/28/13
 * Time: 6:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdListClickListerner implements AdapterView.OnItemClickListener {
    private Context mContext;
    private KGApp app;
    private ListView mListView;

    public AdListClickListerner(ListView mListView, Context mContext, KGApp app) {
        this.mListView = mListView;
        this.mContext = mContext;
        this.app = app;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        Cursor item = (Cursor) mListView.getItemAtPosition(position);
        Intent toDetails = new Intent(mContext, AdDetailActivity.class);

        toDetails.putExtra(AppData.Advertisements.COLUMN_NAME_CATEGORY, item.getString(item.getColumnIndex(AppData.Advertisements.COLUMN_NAME_CATEGORY)));
        toDetails.putExtra(AppData.Advertisements.COLUMN_NAME_TITLE, item.getString(item.getColumnIndex(AppData.Advertisements.COLUMN_NAME_TITLE)));
        toDetails.putExtra(AppData.Advertisements._ID, item.getString(item.getColumnIndex(AppData.Advertisements._ID)));
        toDetails.putExtra(AppData.Advertisements.COLUMN_NAME_STATUS, item.getString(item.getColumnIndex(AppData.Advertisements.COLUMN_NAME_STATUS)));
        toDetails.putExtra(AppData.Advertisements.COLUMN_NAME_PHOTO, item.getString(item.getColumnIndex(AppData.Advertisements.COLUMN_NAME_PHOTO)));
        toDetails.putExtra(AppData.Advertisements.COLUMN_NAME_DESCRIPTION, item.getString(item.getColumnIndex(AppData.Advertisements.COLUMN_NAME_DESCRIPTION)));
        toDetails.putExtra(AppData.Advertisements.COLUMN_NAME_OWNER, item.getString(item.getColumnIndex(AppData.Advertisements.COLUMN_NAME_OWNER)));
        toDetails.putExtra(AppData.Advertisements.COLUMN_NAME_TIMING, item.getString(item.getColumnIndex(AppData.Advertisements.COLUMN_NAME_TIMING)));

        mContext.startActivity(toDetails);
    }
}
