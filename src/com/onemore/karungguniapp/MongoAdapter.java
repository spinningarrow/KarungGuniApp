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
		AdvertisementData[0] = new Advertisement();
		AdvertisementData[1] = new Advertisement();
		AdvertisementData[2] = new Advertisement();
		AdvertisementData[3] = new Advertisement();
		AdvertisementData[4] = new Advertisement();
		AdvertisementData[5] = new Advertisement();
		AdvertisementData[6] = new Advertisement();
		AdvertisementData[7] = new Advertisement();
		AdvertisementData[8] = new Advertisement();
		AdvertisementData[9] = new Advertisement();
		AdvertisementData[10] = new Advertisement();
		AdvertisementData[11] = new Advertisement();
		AdvertisementData[12] = new Advertisement();
		AdvertisementData[13] = new Advertisement();
		AdvertisementData[14] = new Advertisement();
		AdvertisementData[15] = new Advertisement();
		AdvertisementData[16] = new Advertisement();
		AdvertisementData[17] = new Advertisement();
		AdvertisementData[18] = new Advertisement();
		AdvertisementData[19] = new Advertisement();
		AdvertisementData[20] = new Advertisement();
		AdvertisementData[21] = new Advertisement();
		AdvertisementData[22] = new Advertisement();
		AdvertisementData[23] = new Advertisement();
		AdvertisementData[24] = new Advertisement();
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
			convertView = mInflater.inflate(R.layout.advertisement, null);

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
		holder.icon.setImageBitmap(((BitmapDrawable) mContext.getResources().getDrawable(android.R.drawable.arrow_up_float)).getBitmap());

		return convertView;
	}

	private static class ViewHolder {
		public TextView title;
		public TextView distance;
		public ImageView icon;
	}
}
