package com.example.boulocalix.a1click1leave.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    public int id ;
    @SerializedName("name")
    public String fullName ;
    @SerializedName("photo")
    public String photo ;
    @SerializedName("cluster")
    public String cluster ;
    @SerializedName("email")
    public String email ;
    @SerializedName("phone")
    public String phone ;

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }
}
