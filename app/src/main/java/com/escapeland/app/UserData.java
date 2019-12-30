package com.escapeland.app;

import android.net.Uri;

public class UserData {
    String name;
    String password;
    boolean isEmailVerified;
    String date;
    String email;
    String user_type;
    String profileUri;

    public UserData(String profileUri, String name , String email, boolean isEmailVerified, String password, String date, String user_type) {
        this.name = name;
        this.profileUri = profileUri;
        this.email = email;
        this.password = password;
        this.date = date;
        this.isEmailVerified = isEmailVerified;
        this.user_type  = user_type;
    }

    public String getProfileUri() {
        return profileUri;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    public String getEmail(){ return email;}
    public String getDate(){
        return date;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean getIsEmailVerified() {
        return isEmailVerified;
    }

    public String getUser_type(){return user_type;}
}
