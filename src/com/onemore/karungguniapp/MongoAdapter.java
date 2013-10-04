package com.onemore.karungguniapp;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MongoAdapter extends ArrayAdapter<Advertisement> {

	private Context mContext;
	
	private LayoutInflater mInflater;
	Advertisement[] AdvertisementData;
	public MongoAdapter(Context context, int resource) {
		super(context, resource);
		AdvertisementData = new Advertisement[25];
		for(int i = 0; i < AdvertisementData.length; i++)
		{
			AdvertisementData[i] = new Advertisement();
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

	private static class ViewHolder {
		public TextView title;
		public TextView distance;
		public ImageView icon;
	}
}
