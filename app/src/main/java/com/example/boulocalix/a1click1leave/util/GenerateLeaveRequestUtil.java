package com.example.boulocalix.a1click1leave.util;

import android.content.Context;
import android.widget.Toast;

import com.example.boulocalix.a1click1leave.MainActivity;
import com.example.boulocalix.a1click1leave.callbacks.LeaveAPICallbacks;
import com.example.boulocalix.a1click1leave.model.LeaveTicket;
import com.example.boulocalix.a1click1leave.model.SubmitTicket;
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
    MainActivity main ;
    String gift ;

    public GenerateLeaveRequestUtil(Context context, MainActivity main) {
        sharePrefer = new SharePrefer(context) ;
        this.sharePrefer = sharePrefer ;
        this.context = context ;
        this.main = main ;
    }

    public void createAppropriateLeaveRequest(ArrayList<String> info) {
        int leaveType = Integer.parseInt(info.get(3)) + 1  ;
        Double numberOfDay = Double.parseDouble(info.get(0)) ;

            SubmitTicket ticket = new SubmitTicket(sharePrefer.getId(), info.get(1), info.get(2), numberOfDay, leaveType) ;
            LeaveRepository.getInstance(context).leaveRequest(ticket, this);
    }





    @Override
    public void onContactDatabaseSuccess(Object data) {
        if (data instanceof LeaveTicket) {
            if (((LeaveTicket) data).getTicket().getLeavetypeId() == 1 ) {
                sharePrefer.setBalance(sharePrefer.getBalance()-((LeaveTicket) data).getTicket().getNumberOfDay());
                sharePrefer.setTotalAnnualDayUsed(((LeaveTicket) data).getTicket().getNumberOfDay()+sharePrefer.getTotalAnnualDayUsed());

            }
            gift = ((LeaveTicket) data).getGift();


            sharePrefer.setTotalDayOff(((LeaveTicket) data).getTicket().getNumberOfDay()+sharePrefer.getTotalDayOff());
            main.updateBalance();
            main.thankYouGiftDialog(((LeaveTicket) data).getGift(), ((LeaveTicket) data).getThanks(), ((LeaveTicket) data).getTips());
        }
    }

    @Override
    public void onCreateDatabaseError(String mess) {
        Toast.makeText(context, "Failed to submit because of" + mess, Toast.LENGTH_LONG).show();
        main.failSubmitleave();
    }
}
