package com.example.boulocalix.a1click1leave.model;

import com.google.gson.annotations.SerializedName;

public final class LeaveTicket {

    private static LeaveTicket leaveTicket  = new LeaveTicket() ;
    @SerializedName("leave_ticket")
    private Ticket ticket ;
    @SerializedName("thanks")
    private String thanks ;
    @SerializedName("gift")
    private Gift gift ;
    @SerializedName("useful_tip")
    private String tips;



    public String getGift() {
        if (gift != null) {
        return gift.getName(); }
        else return  null ; 
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getThanks() {
        return thanks;
    }

    public String getTips() {
        return tips;
    }

    private  final class Gift {
        @SerializedName("gift_name")
        String name ;

        public String getName() {
            return name;
        }
    }
}
