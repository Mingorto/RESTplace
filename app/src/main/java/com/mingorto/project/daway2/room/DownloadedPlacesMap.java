package com.mingorto.project.daway2.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class DownloadedPlacesMap {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "placeId")
    private String placeId;

    @ColumnInfo (name = "name")
    private String name;

    @ColumnInfo (name = "latitude")
    private double latitude;

    @ColumnInfo (name = "longitude")
    private double longitude;

    @ColumnInfo (name = "type")
    private int type;

    @ColumnInfo (name = "address")
    private String address;

    @ColumnInfo (name = "rating")
    private double rating;

    @ColumnInfo (name = "ratingCount")
    private int ratingCount;

    public DownloadedPlacesMap(String placeId, String name, double latitude, double longitude, int type, String address, double rating, int ratingCount) {
        this.placeId = placeId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.address = address;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }
}
