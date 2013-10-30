package com.omemore.karungguniapp.listview;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import com.onemore.karungguniapp.AppData;
import com.onemore.karungguniapp.KGApp;
import com.onemore.karungguniapp.R;
import com.onemore.karungguniapp.model.Advertisement;

public class AdvertisementList extends ListFragment
implements LoaderManager.LoaderCallbacks<Cursor> 
{

	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;
	private KGApp app;
	//	MongoAdapter mAdapter;


	// The SearchView for doing filtering.
	SearchView mSearchView;
	ListView mListView;
	// If non-null, this is the current filter the user has provided.
	String mCurFilter;
	Context mContext;
	List<Advertisement> ads;
	ImageLoader imageLoader;
	String selection = null;
	String sortOrder = null;
	String specialColumn;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		Bundle args = getArguments();
		selection = args.getString("selection");
		sortOrder = args.getString("orderby");
		specialColumn = args.getString("column");
		setEmptyText(getResources().getString(R.string.noData));
		app = (KGApp)getActivity().getApplication();
		setHasOptionsMenu(true);
		mListView = getListView();
		mContext = getActivity();
		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.advertisement, null,
				new String[] { AppData.Advertisements.COLUMN_NAME_TITLE, AppData.Advertisements.COLUMN_NAME_DESCRIPTION, AppData.Advertisements.COLUMN_NAME_PHOTO, specialColumn},
				new int[] { R.id.title, R.id.description, R.id.list_image, R.id.time_posted }, 0);
		imageLoader=new ImageLoader(getActivity().getApplicationContext());
		mAdapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (view.getId() == R.id.time_posted) {
					TextView text = (TextView) view;
					if (specialColumn.equals(AppData.Advertisements.COLUMN_NAME_TIMING_END))
					{
					    SimpleDateFormat date_format = new SimpleDateFormat("EEE HH:mm");
						Date date = new Date((long) Float.parseFloat(cursor.getString(columnIndex)));
						String time = date_format.format(date);
						text.setText(time);
						return true;
					}
					else if(specialColumn.equals(AppData.Advertisements.COLUMN_NAME_DISTANCE))
					{
						text.setText(cursor.getInt(columnIndex) + " m");
						return true;
					}
					else
						return false;
				}
				if (view.getId() == R.id.list_image) {
					ImageView image = (ImageView) view;
					String url = cursor.getString(columnIndex);
					imageLoader.DisplayImage(url, image);
					return true;
				}
				return false;
			}
		});
		setListAdapter(mAdapter);

		mListView.setOnItemClickListener(new AdListClickListerner(mListView, mContext, app));


		// Start out with a progress indicator.
		setListShown(false);

		// Prepare the loader.  Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		ViewGroup parent = (ViewGroup) inflater.inflate(R.layout.ad_list, container, false);
		parent.addView(v, 0);
		return parent;
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created.  This
		// sample only has one Loader, so we don't care about the ID.
		// First, pick the base URI to use depending on whether we are
		// currently filtering.
		Uri baseUri = AppData.Advertisements.CONTENT_URI;
		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		return new CursorLoader(getActivity(), baseUri,
				null, selection, null, sortOrder);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in.  (The framework will take care of closing the
		// old cursor once we return.)
		data.setNotificationUri(getActivity().getContentResolver(), AppData.Advertisements.CONTENT_URI);
		mAdapter.swapCursor(data);

		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed.  We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
	}
}