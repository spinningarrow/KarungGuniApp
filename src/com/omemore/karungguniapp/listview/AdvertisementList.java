package com.omemore.karungguniapp.listview;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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
import com.onemore.karungguniapp.AppData;
import com.onemore.karungguniapp.KGApp;
import com.onemore.karungguniapp.R;
import com.onemore.karungguniapp.model.Advertisement;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);

		// Give some text to display if there is no data. 
		setEmptyText(getResources().getString(R.string.noData));
				app = (KGApp)getActivity().getApplication();
		//		mAdapter = new MongoAdapter(getActivity(), R.layout.advertisement);
		// We have a menu item to show in action bar.
		setHasOptionsMenu(true);
		mListView = getListView();

		LayoutInflater inflator = getActivity().getLayoutInflater();
		View header = inflator.inflate(R.layout.list_header, null);
		ads = new ArrayList<Advertisement>();
		//		header.setOnClickListener(new OnClickListener()
		//		{
		//			   @Override
		//			   public void onClick(View v)
		//			   {
		//				   mAdapter.updateAdvertisements();
		//			   }
		//			});
		//		
		//		mListView.addHeaderView(header);
		//		View footer = inflator.inflate(R.layout.list_footer, null);
		//		footer.setOnClickListener(new OnClickListener()
		//		{
		//			   @Override
		//			   public void onClick(View v)
		//			   {
		//				   mAdapter.loadMore();
		//			   }
		//			});
		//		mListView.addFooterView(footer);
		// Create an empty adapter we will use to display the loaded data.
		//		ImageView iv = (ImageView) getActivity().findViewById(R.id.icon);
		//		String url = "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash1/1075913_67400590475_1168305305_q.jpg";		
		//		iv.setImageDrawable(LoadImageFromWebOperations(url));
        mContext = getActivity();
		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.advertisement, null,
				new String[] { AppData.Advertisements.COLUMN_NAME_TITLE, AppData.Advertisements.COLUMN_NAME_DESCRIPTION, AppData.Advertisements.COLUMN_NAME_PHOTO},
				new int[] { R.id.title, R.id.distance, R.id.list_image }, 0);
		imageLoader=new ImageLoader(getActivity().getApplicationContext());
		mAdapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (view.getId() == R.id.list_image) {
					ImageView image = (ImageView) view;
					String url;
					try {
						url = new String(cursor.getBlob(columnIndex), "UTF-8");
						imageLoader.DisplayImage(url, image);
						return true;
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

	public static Drawable LoadImageFromWebOperations(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		ViewGroup parent = (ViewGroup) inflater.inflate(R.layout.ad_list, container, false);
		parent.addView(v, 0);
		return parent;
	}
//    @Override
//    public void onListItemClick(ListView listView, View view, int position, long id) {
//        view.setBackgroundColor(android.R.color.background_light);
//        app.setCurrentItem(app.getItems().get(position));
//        Intent dealDetails = new Intent(mContext, AdDetailActivity.class);
//        startActivity(dealDetails);
//    }

	//	public boolean onQueryTextChange(String newText) {
	//		// Called when the action bar search text has changed.  Update
	//		// the search filter, and restart the loader to do a new query
	//		// with this filter.
	//		String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
	//		// Don't do anything if the filter hasn't actually changed.
	//		// Prevents restarting the loader when restoring state.
	//		if (mCurFilter == null && newFilter == null) {
	//			return true;
	//		}
	//		if (mCurFilter != null && mCurFilter.equals(newFilter)) {
	//			return true;
	//		}
	//		mCurFilter = newFilter;
	//		getLoaderManager().restartLoader(0, null, this);
	//		return true;
	//	}
	////
	//
	//	@Override 
	//	public void onListItemClick(ListView l, View v, int position, long id)
	//	{
	//		// Insert desired behavior here.
	//		Log.i("FragmentComplexList", "Item clicked: " + id);
	//	}
	//

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created.  This
		// sample only has one Loader, so we don't care about the ID.
		// First, pick the base URI to use depending on whether we are
		// currently filtering.
		Uri baseUri = AppData.Advertisements.CONTENT_URI;
		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		return new CursorLoader(getActivity(), baseUri,
				null, null, null,
				null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in.  (The framework will take care of closing the
		// old cursor once we return.)
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

	//    private void resetListItems(List<Advertisement> newItems) {
	//        .clear();
	//        items.addAll(newItems);
	//        mAdapter.notifyDataSetChanged();
	//    }
}