package com.onemore.karungguniapp;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import com.onemore.karungguniapp.model.Advertisement;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementList extends ListFragment
implements LoaderManager.LoaderCallbacks<Cursor> 
{

	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;
//	private KGApp app;
	//	MongoAdapter mAdapter;


	// The SearchView for doing filtering.
	SearchView mSearchView;
	ListView mListView;
	// If non-null, this is the current filter the user has provided.
	String mCurFilter;
	Context mContext;
	List<Advertisement> ads;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);

		// Give some text to display if there is no data. 
		setEmptyText(getResources().getString(R.string.noData));
//		app = (KGApp)getActivity().getApplication();
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
		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.advertisement, null,
				new String[] { AppData.Advertisements.COLUMN_NAME_TITLE, AppData.Advertisements.COLUMN_NAME_DESCRIPTION},
				new int[] { R.id.title, R.id.distance }, 0);

		setListAdapter(mAdapter);
		//        if (app.getSectionList().isEmpty()) {
		////            if (app.connectionPresent()) {
		////                new ParseFeedTask().execute();
		////            } else {
		//                Toast.makeText(getActivity(), getString(R.string.deal_list_network_unavailable), Toast.LENGTH_LONG).show();
		//            //}
		//        } else {
		//            //onLoaderReset(app.getSectionList().get(0).getItems());
		//        }

		mListView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

						Intent intent = new Intent(getActivity(),AdDetailActivity.class);
						startActivity(intent);
						mContext = getActivity();
					}
				});

		// Start out with a progress indicator.
		//		setListShown(false);

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
		//		String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
		//				+ Contacts.HAS_PHONE_NUMBER + "=1) AND ("
		//				+ Contacts.DISPLAY_NAME + " != '' ))";
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