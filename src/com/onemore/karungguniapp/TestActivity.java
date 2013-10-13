package com.onemore.karungguniapp;

import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.onemore.karungguniapp.R;

import java.util.Arrays;
import java.util.List;

public class TestActivity extends ListActivity {
    boolean mDualPane;
    int mCurCheckPosition = 0;

    //@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Populate list with our static array of titles.
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, Arrays.asList("Hello", "World!", "How", "Are", "You")));

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        TextView detailsFrame = (TextView)findViewById(R.id.catalog);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            //showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        mCurCheckPosition = position;

        //if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(position, true);

            // Check what fragment is currently shown, replace if needed.
            AdDetailFragment details = (AdDetailFragment)getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != position) {
                // Make new fragment to show this selection.
                details = AdDetailFragment.newInstance(position);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (position == 0) {
                    ft.replace(R.id.details, details);
                }
                else {
                    ft.replace(R.id.details, details);
                }
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();

        }
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
//    void showDetails(int index) {
//        mCurCheckPosition = index;
//
//        //if (mDualPane) {
//            // We can display everything in-place with fragments, so update
//            // the list to highlight the selected item and show the data.
//            getListView().setItemChecked(index, true);
//
//            // Check what fragment is currently shown, replace if needed.
//            AdDetailFragment details;
//            details = (AdDetailFragment)getFragmentManager().findFragmentById(R.id.details);
//            if (details == null || details.getShownIndex() != index) {
//                // Make new fragment to show this selection.
//                details = AdDetailFragment.newInstance(index);
//
//                // Execute a transaction, replacing any existing fragment
//                // with this one inside the frame.
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                if (index == 0) {
//                    ft.replace(R.id.details, details);
//                }
//                else {
//                    ft.replace(R.id.details, details);
//                }
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.commit();
//            //}
//        }
//
////        } else {
////            // Otherwise we need to launch a new activity to display
////            // the dialog fragment with selected text.
////            Intent intent = new Intent();
////            intent.setClass(getActivity(), DetailsActivity.class);
////            intent.putExtra("index", index);
////            startActivity(intent);
////        }
//    }
}