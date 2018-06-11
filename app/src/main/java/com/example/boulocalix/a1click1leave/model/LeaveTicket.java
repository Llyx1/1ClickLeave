package com.example.boulocalix.a1click1leave.model;

import com.google.gson.annotations.SerializedName;

public final class LeaveTicket {

    private static LeaveTicket leaveTicket  = new LeaveTicket() ;
    @SerializedName("leave_ticket")
    private Ticket ticket ;
    @SerializedName("thanks")
    private String thanks ;
    @SerializedName("gift")
    private String gift ;



    public String getGift() {
        return gift;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getThanks() {
        return thanks;
    }
}
