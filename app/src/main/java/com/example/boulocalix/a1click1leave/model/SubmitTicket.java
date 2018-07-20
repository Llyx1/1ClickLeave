package com.example.boulocalix.a1click1leave.model;

import com.google.gson.annotations.SerializedName;

public class SubmitTicket {
    @SerializedName("user_id")
    private  int userId ;
    @SerializedName("start_date")
    private String startDate ;
    @SerializedName("end_date")
    private String endDate ;
    @SerializedName("number_of_days")
    private Double numberOfDays ;
    @SerializedName("leavetype_id")
    private int leavetypeId ;

    public SubmitTicket(int userId, String startDate, String endDate, Double numberOfDays, int leavetypeId) {
        this.leavetypeId=leavetypeId ;
        this.endDate = endDate ;
        this.startDate = startDate ;
        this.userId = userId ;
        this.numberOfDays = numberOfDays ;

    }
}
