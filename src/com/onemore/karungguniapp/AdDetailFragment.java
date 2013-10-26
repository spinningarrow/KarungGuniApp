//package com.onemore.karungguniapp;
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//
///**
// * Created with IntelliJ IDEA.
// * User: gemengqin
// * Date: 10/3/13
// * Time: 8:12 PM
// * To change this template use File | Settings | File Templates.
// */
//
//
//public class AdDetailFragment extends Fragment {
//    private ProgressBar progressBar;
//    public static AdDetailFragment newInstance(int index) {
//        AdDetailFragment f = new AdDetailFragment();
//
//        // Supply index input as an argument.
//        Bundle args = new Bundle();
//        args.putInt("index", index);
//        f.setArguments(args);
//
//        return f;
//    }
//
//    public int getShownIndex() {
//        return getArguments().getInt("index", 0);
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View dealView = inflater.inflate(R.layout.ad_detail,container,false);
//        progressBar = (ProgressBar) dealView.findViewById(R.id.progress);
//        progressBar.setIndeterminate(true);
//        Advertisement item = new Advertisement(AdType.NEWSPAPER,"abc","ddd",1);
//        if (item != null) {
//
//            populateDealView(dealView, item);
//        }
//        return dealView;
//
//    }
//
//    //    @Override
////    public View onCreateView(LayoutInflater inflater,ViewGroup container,    Bundle savedInstanceState) {
////        View dealView = inflater.inflate(R.layout.ad_detail,container,false);
////        progressBar = (ProgressBar) dealView.findViewById(R.id.progress);
////        progressBar.setIndeterminate(true);
////        Advertisement item = new Advertisement(AdType.NEWSPAPER,"abc","ddd",1);
////        if (item != null) {
////
////            populateDealView(dealView, item);
////        }
////        return dealView;
////
////
////
////
////    }
//    private void populateDealView(View dealView, Advertisement item) {
//        TextView catalog =(TextView) dealView.findViewById(R.id.category);
//        catalog.setText(item.type.toString());
//        TextView seller =(TextView) dealView.findViewById(R.id.seller);
//        seller.setText(item.userID);
//        TextView addr_short =(TextView) dealView.findViewById(R.id.addr_short);
//        addr_short.setText("address of seller");
//        TextView distance =(TextView) dealView.findViewById(R.id.distance);
//
//        TextView time_avail =(TextView) dealView.findViewById(R.id.time_avail);
//
//
//        ImageView photo = (ImageView) dealView.findViewById( R.id.item_img);
//
//
//
////        CharSequence pricePrefix =getText(R.string.deal_details_price_prefix);
////        TextView price = (TextView) dealView.findViewById(R.id.details_price);
////        price.setText(pricePrefix + item.convertedCurrentPrice);
////        TextView msrp = (TextView) dealView.findViewById( R.id.details_msrp);
////        msrp.setText(item.msrp);
////        TextView quantity =(TextView) dealView.findViewById(R.id.details_quantity);
////        quantity.setText(Integer.toString(item.quantity));
////        TextView quantitySold = (TextView) dealView.findViewById( R.id.details_quantity_sold);
////        quantitySold.setText(Integer.toString(item.quantitySold));
////        TextView location = (TextView) dealView.findViewById(R.id.details_location); location.setText(item.location);
//
//
//
//
//
//        /**
//         * Create a new instance of DetailsFragment, initialized to
//         * show the text at 'index'.
//         */
//
//
//    }
//
//}