package com.mingorto.project.daway2.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class SavedPlaces {
    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo (name = "innerId")
    private String innerId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "mainPhotoUrl")
    private String mainPhotoUrl;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "rating")
    private double rating;

    @ColumnInfo(name = "position")
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SavedPlaces(String innerId, String name, int type, String mainPhotoUrl, String address, double rating, int position) {
        this.innerId = innerId;
        this.name = name;
        this.type = type;
        this.mainPhotoUrl = mainPhotoUrl;
        this.address = address;
        this.rating = rating;
        this.position = position;

    }

    public String getInnerId() {
        return innerId;
    }

    public void setInnerId(String innerId) {
        this.innerId = innerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMainPhotoUrl() {
        return mainPhotoUrl;
    }

    public void setMainPhotoUrl(String mainPhotoUrl) {
        this.mainPhotoUrl = mainPhotoUrl;
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
}
