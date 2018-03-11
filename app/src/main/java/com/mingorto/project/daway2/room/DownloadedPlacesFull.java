package com.mingorto.project.daway2.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.mingorto.project.daway2.CommentsConverter;
import com.mingorto.project.daway2.parsing.Comment;

import java.util.List;

@Entity
public class DownloadedPlacesFull {
    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo (name = "placeId")
    private String placeId;

    @ColumnInfo (name = "name")
    private String name;

    @ColumnInfo (name = "latitude")
    private double latitude;

    @ColumnInfo (name = "longitude")
    private double longitude;

    @ColumnInfo (name = "type")
    private int type;

    @ColumnInfo (name = "description")
    private String description;

    @ColumnInfo (name = "address")
    private String address;

    @ColumnInfo (name = "rating")
    private double rating;

    @ColumnInfo (name = "ratingCount")
    private int ratingCount;

    @ColumnInfo (name = "phone")
    private String phone;

    @ColumnInfo (name = "vkLink")
    private String vkLink;

    @ColumnInfo (name = "website")
    private String website;

    @ColumnInfo (name = "buyLink")
    private String buyLink;

    @TypeConverters(CommentsConverter.class)
    @ColumnInfo (name = "comments")
    private List<Comment> comments;

    @TypeConverters(Converters.class)
    @ColumnInfo (name = "imageLinks")
    private List<String> imageLinks;

    public String getPlaceId() {
        return placeId;
    }

    public DownloadedPlacesFull(String placeId, String name, double latitude, double longitude, int type, String description, String address, double rating, int ratingCount, String phone, String vkLink, String website, String buyLink, List<Comment> comments, List<String> imageLinks) {
        this.placeId = placeId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.description = description;
        this.address = address;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.phone = phone;
        this.vkLink = vkLink;
        this.website = website;
        this.buyLink = buyLink;
        this.comments = comments;
        this.imageLinks = imageLinks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVkLink() {
        return vkLink;
    }

    public void setVkLink(String vkLink) {
        this.vkLink = vkLink;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBuyLink() {
        return buyLink;
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List<String> imageLinks) {
        this.imageLinks = imageLinks;
    }
}
