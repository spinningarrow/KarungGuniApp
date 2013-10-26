//package com.onemore.karungguniapp;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Advertisement {
//	public final AdType type;
//	public final String description, photoPath, title;
//	public final AdStatus status;
//	public final List<Integer> karungunis;
//	public int userID;
//
//	public Advertisement(AdType type, String desc, String path, int user, String title)
//	{
//		this.title = title;
//		this.type = type;
//		this.description = desc;
//		this.photoPath = path;
//		this.status = getStatus();
//		this.karungunis = new ArrayList<Integer>();
//		this.userID = user;
//	}
//
//	public Advertisement() {
//		this.title = "Selling newspaper";
//		this.type = AdType.NEWSPAPER;
//		this.description = "Selling a lot of newspaper. Buy, can?";
//		this.photoPath = "";
//		this.status = getStatus();
//		this.karungunis = new ArrayList<Integer>();
//		this.userID = 0;
//	}
//
//	public AdStatus getStatus()
//	{
//		return AdStatus.LIVE;
//	}
//
//	public void addKarangGuni(int id)
//	{
//		karungunis.add(id);
//	}
//}
