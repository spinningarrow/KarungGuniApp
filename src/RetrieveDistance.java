import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.onemore.karungguniapp.LBS.GeoUtil;
import com.onemore.karungguniapp.LBS.GetLocationWithGPS;

	public class RetrieveDistance extends AsyncTask<String, Void, Double> {
		private TextView textView;

		public RetrieveDistance(TextView textView) {
			this.textView = textView;
		}


		@Override
		protected Double doInBackground(String... args) {
			Double dist = 0.0;
			String testAddr = "======================";
			testAddr = "block271a Jurong west avenue 5 Singapore";
			String parsed_addr = testAddr.replace(" ","+");
			double[] seller_location =  GeoUtil.getLatLongFromAddress(parsed_addr);
			testAddr =String.valueOf(seller_location[0] )+"  "+ String.valueOf(seller_location[1]);
			while(GetLocationWithGPS.gotMyLoc == null);
			if(GetLocationWithGPS.gotMyLoc==GetLocationWithGPS.GET_MY_LOC_SUCCESS)
			{
				dist = Double.valueOf(GeoUtil.calculateDistance(GetLocationWithGPS.myLoc,seller_location));
			}
			return dist;
		}
//
//		@Override
//		protected void onPostExecute(Double dist) {
//			if (dist != null && dist.doubleValue()!=0.0) {
//				String formatted_dist = String.format("%.2f",dist.doubleValue());
//				textView.setText(formatted_dist+"m");
//				textView.setVisibility(View.VISIBLE);
//			}
//			else{
//				textView.setText(GetLocationWithGPS.gotMyLoc);
//				textView.setVisibility(View.VISIBLE);
//			}
//		}



	}
