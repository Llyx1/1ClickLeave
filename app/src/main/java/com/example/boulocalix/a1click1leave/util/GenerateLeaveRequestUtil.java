package com.example.boulocalix.a1click1leave.util;

import android.content.Context;
import android.widget.Toast;

import com.example.boulocalix.a1click1leave.callbacks.LeaveAPICallbacks;
import com.example.boulocalix.a1click1leave.model.LeaveTicket;
import com.example.boulocalix.a1click1leave.model.Ticket;
import com.example.boulocalix.a1click1leave.repository.LeaveRepository;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateLeaveRequestUtil implements LeaveAPICallbacks {
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
                    returnTable[2] = numberOfDay - remaining ;
                    returnTable[0] = remaining ;
                }
                break;
                default:
                    returnTable = checkBalance(leaveType, numberOfDay) ;
        }

        if (returnTable[0]!=0.0) {
            Ticket ticket = new Ticket(sharePrefer.getId(), info.get(1), info.get(2), returnTable[0], "huhu", leaveType) ;
            LeaveRepository.getInstance(context).leaveRequest(ticket, this);
        }

        if (returnTable[2]!=0.0) {
            Ticket ticket1 = new Ticket(sharePrefer.getId(), info.get(1), info.get(2), returnTable[2], "huhu", 2) ;
            LeaveRepository.getInstance(context).leaveRequest(ticket1,this);
        }
        if (returnTable[1]!=0.0) {
            Ticket ticket2 = new Ticket(sharePrefer.getId(), info.get(1), info.get(2), returnTable[1], "huhu", 1) ;
            LeaveRepository.getInstance(context).leaveRequest(ticket2, this);
        }
    }

    private Double[] checkBalance (int leaveType, Double numberOfDay) {
        Double maxDayOfPerCase = getMaxDayOffPerCase(leaveType) ;
        Double extraDays = 0.0 ;
        Double reportPaidLeave = 0.0 ;
        Double remaining = sharePrefer.getBalance() ;
        if (numberOfDay > maxDayOfPerCase) { //amount superior of the maximal amount granted by the company
            if (numberOfDay -maxDayOfPerCase > remaining) { //not enough paid leave to cover the difference
                reportPaidLeave = remaining ;
                extraDays = numberOfDay - maxDayOfPerCase - remaining ;
                numberOfDay = maxDayOfPerCase ;
            }else { //enough paid leave to cover the difference
                reportPaidLeave = numberOfDay - maxDayOfPerCase ;
                numberOfDay = maxDayOfPerCase ;
            }
        }
        Double[] returnTable = {numberOfDay, reportPaidLeave, extraDays} ;
        return returnTable ;
    }

    private Double getMaxDayOffPerCase(int leaveType) {
        Double maxDayOff ;
      switch (leaveType) {
          case 4 : //Paternity
              maxDayOff = 7.0 ;
              break;
          case 5 : //Maternity
              maxDayOff = 180.0 ;
              break;
          case 6 : //Funeral close
              maxDayOff = 3.0 ;
              break;
          case 7 : //Funeral
              maxDayOff = 1.0 ;
              break;
          case 8 : //Wedding
              maxDayOff = 3.0 ;
              break;
          case 9 : //Wedding children
              maxDayOff = 1.0 ;
              break;
          default:
              maxDayOff = 0.0 ;
              break;
      }
      return maxDayOff ;
    }

    @Override
    public void onContactDatabaseSuccess(Object data) {
        if (data instanceof LeaveTicket) {
            if (((LeaveTicket) data).getTicket().getLeavetypeId() == 1 ) {
                sharePrefer.setBalance(sharePrefer.getBalance()-((LeaveTicket) data).getTicket().getNumberOfDay());
            }
        }
    }

    @Override
    public void onCreateDatabaseError(String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }
}
