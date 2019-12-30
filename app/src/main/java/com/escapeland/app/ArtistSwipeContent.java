package com.escapeland.app;

import android.graphics.Bitmap;

public class ArtistSwipeContent {
    int artist_blur_image;
    String name;
    String artist_info;
    String facebook_url;
    String google_url;
    String instagram_url;
    String followHisOrHer;

    public int getArtist_blur_image(){
        return artist_blur_image;
    }

    public String getFollowHisOrHer() {
        return followHisOrHer;
    }

    public void setFollowHisOrHer(String followHisOrHer) {
        this.followHisOrHer = followHisOrHer;
    }

    public void setArtist_blur_image(int artist_blur_image){
        this.artist_blur_image = artist_blur_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArtistSwipeContent(String followHisOrHer, int     artist_blur_image,String name, String artist_info, String facebook_url, String google_url, String instagram_url) {
        this.artist_blur_image = artist_blur_image;
        this.followHisOrHer = followHisOrHer;
        this.name = name;
        this.artist_info = artist_info;
        this.facebook_url = facebook_url;
        this.google_url = google_url;
        this.instagram_url = instagram_url;
    }

    public String getArtist_info() {
        return artist_info;
    }

    public void setArtist_info(String artist_info) {
        this.artist_info = artist_info;
    }

    public String getFacebook_url() {
        return facebook_url;
    }

    public void setFacebook_url(String facebook_url) {
        this.facebook_url = facebook_url;
    }

    public String getGoogle_url() {
        return google_url;
    }

    public void setGoogle_url(String google_url) {
        this.google_url = google_url;
    }

    public String getInstagram_url() {
        return instagram_url;
    }

    public void setInstagram_url(String instagram_url) {
        this.instagram_url = instagram_url;
    }
}


