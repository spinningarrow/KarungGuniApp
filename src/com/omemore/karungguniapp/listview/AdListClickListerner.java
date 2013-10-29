package com.omemore.karungguniapp.listview;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.onemore.karungguniapp.AdDetailActivity;
import com.onemore.karungguniapp.KGApp;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/28/13
 * Time: 6:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdListClickListerner implements AdapterView.OnItemClickListener{
        private Context mContext;
    private KGApp app;
    private ListView mListView;
        public AdListClickListerner(ListView mListView, Context mContext, KGApp app)
        {
            this.mListView = mListView;
               this.mContext = mContext;
            this.app = app;
        }
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//            view.setBackgroundColor(android.R.color.background_light);
//            app.setCurrentItem(app.getSection().getItems().get(position));
            Cursor item = (Cursor) mListView.getItemAtPosition(position);
            try {
                String category = new String(item.getBlob(0), "UTF-8");
                String title = new String(item.getBlob(1), "UTF-8");
                String id = new String(item.getBlob(2), "UTF-8");
                String status = new String(item.getBlob(3),"UTF-8");
                String photo_url = new String(item.getBlob(4), "UTF-8");
                String description = new String(item.getBlob(5), "UTF-8");
                String owner = new String(item.getBlob(6), "UTF-8");
                String timing = new String(item.getBlob(8),"UTF-8");
//                String timeCreated = new String(item.getBlob(7),"UTF-8");




                Intent toDetails = new Intent(mContext, AdDetailActivity.class);
                toDetails.putExtra("ID", id);
                toDetails.putExtra("OWNER", owner);
                toDetails.putExtra("TITLE", title);
                toDetails.putExtra("DESCRIPTION", description);
                toDetails.putExtra("PHOTO_URL", photo_url);
                toDetails.putExtra("CATEGORY", category);
                toDetails.putExtra("STATUS", status);
                //toDetails.putExtra("TIME_CREATED", timeCreated);
                toDetails.putExtra("TIMING", timing);
                mContext.startActivity(toDetails);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


//						Intent intent = new Intent(getActivity(),AdDetailActivity.class);
//						startActivity(intent);
//						mContext = getActivity();
        }
}
