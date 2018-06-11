package com.example.boulocalix.a1click1leave.util;

import android.content.Context;

import com.example.boulocalix.a1click1leave.model.LeaveTicket;
import com.example.boulocalix.a1click1leave.model.Ticket;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateLeaveRequestUtil {
    SharePrefer sharePrefer ;
    Context context ;

    public GenerateLeaveRequestUtil(Context context) {
        sharePrefer = new SharePrefer(context) ;
        this.sharePrefer = sharePrefer ;
        this.context = context ;
    }

    private void leaveRequest(Ticket ticket) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<LeaveTicket> call = apiService.submitLeaveRequest(sharePrefer.getAccessToken(), ticket);
        call.enqueue(new Callback<LeaveTicket>() {
            @Override
            public void onResponse(Call<LeaveTicket> call, Response<LeaveTicket> response) {
                LeaveTicket leaveTicket = response.body() ;
            }

            @Override
            public void onFailure(Call<LeaveTicket> call, Throwable t) {

            }
        });
    }

    public void createAppropriateLeaveRequest(ArrayList<String> info) {
        int leaveType = Integer.parseInt(info.get(3)) + 1  ;
        Double numberOfDay = Double.parseDouble(info.get(0)) ;
        Double extraDays = 0.0 ;
        Double reportPaidLeave = 0.0 ;
        Double[] returnTable = {numberOfDay, extraDays, reportPaidLeave} ;
        Double remaining = sharePrefer.getBalance() ;
        switch (leaveType) {
            case 1: //Paid Leave
                if (numberOfDay > remaining) {
                    extraDays = numberOfDay - remaining ;
                    numberOfDay = remaining ;
                    sharePrefer.setBalance(0.0);
                }else {sharePrefer.setBalance(remaining-numberOfDay);}
                break;
            case 4 : //Paternity
                returnTable = checkBalance(7.0, numberOfDay) ;
                break;
            case 5 : //Maternity
                returnTable = checkBalance(180.0, numberOfDay) ;
                break;
            case 6 : //Funeral close
                returnTable = checkBalance(3.0, numberOfDay) ;
                break;
            case 7 : //Funeral
                returnTable = checkBalance(1.0, numberOfDay) ;
                break;
            case 8 : //Wedding
                returnTable = checkBalance(3.0, numberOfDay) ;
                break;
            case 9 : //Wedding children
                returnTable = checkBalance(1.0, numberOfDay) ;
                break;
                default:

        }

        if (returnTable[0]!=0) {
//            JsonObject ticket = new JsonObject();
//            ticket.addProperty("user_id", sharePrefer.getId());
//            ticket.addProperty("start_date", info.get(1));
//            ticket.addProperty("end_date", info.get(2));
//            ticket.addProperty("number_of_days", returnTable[0]);
//            ticket.addProperty("note" , "huhu");
//            ticket.addProperty("leavetype_id" , leaveType);
            Ticket ticket = new Ticket(sharePrefer.getId(), info.get(1), info.get(2), returnTable[0], "huhu", leaveType) ;
            leaveRequest(ticket);
        }

        if (returnTable[1]!=0.0) {
//            JsonObject ticket1 = new JsonObject();
//            ticket1.addProperty("user_id", sharePrefer.getId());
//            ticket1.addProperty("start_date", info.get(1));
//            ticket1.addProperty("end_date", info.get(2));
//            ticket1.addProperty("number_of_days", returnTable[1]);
//            ticket1.addProperty("note" , "huhu");
//            ticket1.addProperty("leavetype_id" , 2); //Unpaid
            Ticket ticket1 = new Ticket(sharePrefer.getId(), info.get(1), info.get(2), returnTable[1], "huhu", 2) ;
            leaveRequest(ticket1);
        }
        if (returnTable[2]!=0.0) {
//            JsonObject ticket1 = new JsonObject();
//            ticket1.addProperty("user_id", sharePrefer.getId());
//            ticket1.addProperty("start_date", info.get(1));
//            ticket1.addProperty("end_date", info.get(2));
//            ticket1.addProperty("number_of_days", returnTable[2]);
//            ticket1.addProperty("note" , "huhu");
//            ticket1.addProperty("leavetype_id" , 1); //Paid
            Ticket ticket1 = new Ticket(sharePrefer.getId(), info.get(1), info.get(2), returnTable[2], "huhu", 1) ;
            leaveRequest(ticket1);
        }
    }

    private Double[] checkBalance (Double maxDayOfPerCase, Double numberOfDay) {
        Double extraDays = 0.0 ;
        Double reportPaidLeave = 0.0 ;
        Double remaining = sharePrefer.getBalance() ;
        if (numberOfDay > maxDayOfPerCase) {
            if (numberOfDay -maxDayOfPerCase > remaining) {
                reportPaidLeave = numberOfDay - 7 ;
                extraDays = numberOfDay - maxDayOfPerCase - remaining ;
                numberOfDay = maxDayOfPerCase ;
                sharePrefer.setBalance(0.0);
            }else {
                reportPaidLeave = numberOfDay - maxDayOfPerCase ;
                sharePrefer.setBalance(remaining-numberOfDay + maxDayOfPerCase);
            }
        }
        Double[] returnTable = {numberOfDay, extraDays, reportPaidLeave} ;
        return returnTable ;
    }
}
