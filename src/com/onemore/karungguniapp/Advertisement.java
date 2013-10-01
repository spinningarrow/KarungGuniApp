package com.onemore.karungguniapp;

import java.util.ArrayList;
import java.util.List;

public class Advertisement {
	public final AdType type;
	public final String description, photoPath;
	public final AdStatus status;
	public final List<Integer> karungunis;
	public int userID;
	
	public Advertisement(AdType type, String desc, String path, int user)
	{
		this.type = type;
		this.description = desc;
		this.photoPath = path;
		this.status = getStatus();
		this.karungunis = new ArrayList<Integer>();
		this.userID = user;
	}
	
	public AdStatus getStatus()
	{
		return AdStatus.LIVE;
	}
	
	public void addKarangGuni(int id)
	{
		karungunis.add(id);
	}
}
