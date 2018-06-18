package com.example.boulocalix.a1click1leave.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaveTypeDto {

@SerializedName("leaveType")
public List<Type> type ;

public class Type {
    @SerializedName("id")
    int id ;
    @SerializedName("name")
    String name ;
    @SerializedName("amount")
    public Double amount;
    @SerializedName("tips")
    public String tips ;
}

}
