package com.onemore.karungguniapp;


import java.util.ArrayDeque;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MongoAdapter extends SimpleCursorAdapter {

	private Context mContext;
	
	private LayoutInflater mInflater;
	ArrayDeque<Advertisement> AdvertisementData;
	public MongoAdapter(Context context, int resource) {
		super(context, resource, null, null, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		AdvertisementData = new ArrayDeque<Advertisement>();
		for(int i = 0; i < 20; i++)
		{
			AdvertisementData.add(new Advertisement());
		}
		this.addAll(AdvertisementData);
		this.notifyDataSetChanged();
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mInflater = ((Activity) mContext).getLayoutInflater();
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.advertisement, parent, false);

			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.distance = (TextView) convertView.findViewById(R.id.distance);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText("Selling newspaper" + position);
		holder.distance.setText("Selling a lot of newspaper" + position + ". Buy, can?");
		holder.icon.setImageBitmap(((BitmapDrawable) mContext.getResources().getDrawable(R.drawable.ic_launcher)).getBitmap());

		return convertView;
	}
	
	public void updateAdvertisements()
	{
		Advertisement advertisement = new Advertisement();
		AdvertisementData.addFirst(advertisement);
		insert(advertisement, 0);
		notifyDataSetChanged();
	}
	private static class ViewHolder {
		public TextView title;
		public TextView distance;
		public ImageView icon;
	}
	public void loadMore() {
		Advertisement advertisement = new Advertisement();
		AdvertisementData.add(advertisement);
		add(new Advertisement());
		notifyDataSetChanged();
	}
}
