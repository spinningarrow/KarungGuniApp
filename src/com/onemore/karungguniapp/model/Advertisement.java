package com.onemore.karungguniapp.model;

import com.onemore.karungguniapp.AdStatus;
import com.onemore.karungguniapp.AdType;

import java.util.ArrayList;
import java.util.List;

public final class Advertisement {
//    public final AdType type;
//    public final String description, photoPath, title;
//    public final AdStatus status;
//    public final List<Integer> karungunis;
//    public int userID;

    private int adId;
    private int endTime;

    private AdType type;
    private String description;
    private String photoPath;
    private String title;



    private String location;
    private AdStatus status;
    private List<Integer> karungunis;
    private int userID;






    public Advertisement(int adId, int endTime, AdType type, String desc, String path, int user, String title, String location)
    {
        this.adId = adId;
        this.endTime = endTime;
        this.title = title;
        this.type = type;
        this.description = desc;
        this.photoPath = path;
        this.location = location;
        this.status = getStatus();
        this.karungunis = new ArrayList<Integer>();
        this.userID = user;

    }

    public Advertisement() {
        adId = 0;
        endTime = 20130101;
        title = "Selling newspaper";
        type = AdType.NEWSPAPER;
        description = "Selling a lot of newspaper. Buy, can?";
        photoPath = "";
        status = getStatus();
        karungunis = new ArrayList<Integer>();
        userID = 0;
        location =  "1600 Amphitheatre Parkway, Mountain View, CA";
    }

//    public AdStatus getStatus()
//    {
//        return AdStatus.LIVE;
//    }

    public void addKarangGuni(int id)
    {
        karungunis.add(id);
    }

    public static Advertisement getInstance(Advertisement ad){
        Advertisement copy = new Advertisement();
        copy.adId = ad.adId;
        copy.endTime = ad.endTime;
        copy.type = ad.type;
        copy.description = ad.description;
        copy.photoPath = ad.photoPath;
        copy.title = ad.title;
        copy.status = ad.status;
        copy.karungunis = ad.karungunis;
        copy.userID = ad.userID;
        return copy;
    }

    public void setAdId(int id){
        adId = id;
    }
    public int getAdId(){
        return adId;
    }

    public void setEndTime(int endTime){
        this.endTime = endTime;
    }
    public int getEndTime(){
        return endTime;

    }

    public void setType(AdType type){
        this.type = type;
    }
    public AdType getType(){
        return type;
    }

    public void setDescription(String disc){
        description = disc;
    }

    public String getDescription(){
        return description;
    }

    public void setPhotoPath(String path){
        photoPath = path;
    }

    public String getPhotoPath(){
        return photoPath;

    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setStatus(AdStatus status){
        this.status = status;
    }

    public AdStatus getStatus(){
        return status;
    }

    public void setKarungunis(List<Integer> kg){
        karungunis = kg;
    }

    public List<Integer> getKarungunis(){
        return karungunis;
    }

    public void setUserID(int id){
        userID = id;
    }
    public int getUserID(){
        return userID;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
