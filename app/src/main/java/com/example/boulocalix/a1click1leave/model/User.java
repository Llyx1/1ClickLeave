package com.example.boulocalix.a1click1leave.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    public int id ;
    @SerializedName("name")
    public String fullName ;
    @SerializedName("photo")
    public String photo ;
    @SerializedName("cluster_id")
    public int cluster ;
    @SerializedName("email")
    public String email ;
    @SerializedName("phone")
    public String phone ;
    @SerializedName("backup_buddy")
    public String backupBuddy ;
}
