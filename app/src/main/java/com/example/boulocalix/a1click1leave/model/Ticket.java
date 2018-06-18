package com.example.boulocalix.a1click1leave.model;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Ticket {
    @SerializedName("user_id")
    private  int userId ;
    @SerializedName("start_date")
    private String startDate ;
    @SerializedName("end_date")
    private String endDate ;
    @SerializedName("number_of_days")
    private Double numberOfDays ;
    @SerializedName("note")
    private String note ;
    @SerializedName("leavetype_id")
    private int leavetypeId ;
     @SerializedName("status_id")
     private int status ;

    public Ticket(int userId, String startDate, String endDate, Double numberOfDays, String note, int leavetypeId) {
        this.leavetypeId=leavetypeId ;
        this.endDate = endDate ;
        this.startDate = startDate ;
        this.userId = userId ;
        this.numberOfDays = numberOfDays ;
        this.note = note ;
    }
    public String getEndDate() {
        return endDate.substring(8,10)+ "-"+ endDate.substring(5,8) + endDate.substring(2,4) ;
    }

    public String getStartDate() {

        return startDate.substring(8,10)+ "-"+ startDate.substring(5,8) + startDate.substring(2,4);
    }

    public Double getNumberOfDay() {
        return numberOfDays;
    }

    public String getReason() {
        String reason = "" ;
        switch (leavetypeId) {
            case 1 :
                reason = "Paid leave" ;
                break;
            case 2:
                reason = "Unpaid leave" ;
                break;
            case 3:
                reason = "Sick leave" ;
                break;
            case 4 :
                reason = "Paternity leave" ;
                break;
            case 5:
                reason = "Maternity leave" ;
                break;
            case 6:
                reason = "Funeral of a close relative" ;
                break;
            case 7:
                reason = "Funeral of a relative" ;
                break;
            case 8 :
                reason="Wedding";
                break;
                default:
        }
        return reason ;
    }
    public int getLeavetypeId() {
        return leavetypeId ;
    }

    public int getStatus() {
        return status;
    }
}
