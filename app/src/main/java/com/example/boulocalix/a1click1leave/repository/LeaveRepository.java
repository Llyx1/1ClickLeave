package com.example.boulocalix.a1click1leave.repository;

import android.content.Context;

import com.example.boulocalix.a1click1leave.callbacks.LeaveAPICallbacks;
import com.example.boulocalix.a1click1leave.callbacks.RestCallback;
import com.example.boulocalix.a1click1leave.model.LeaveTicket;
import com.example.boulocalix.a1click1leave.model.LeaveTypeDto;
import com.example.boulocalix.a1click1leave.model.Ticket;
import com.example.boulocalix.a1click1leave.util.ApiClient;
import com.example.boulocalix.a1click1leave.util.ApiInterface;
import com.example.boulocalix.a1click1leave.util.SharePrefer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveRepository {

    Context context;
    SharePrefer sharePrefer;
    private String accessToken = null;
    private static LeaveRepository leaveRepository;

    public LeaveRepository(Context context){
        this.context = context;
        sharePrefer = new SharePrefer(context);
        accessToken = sharePrefer.getAccessToken();
    }

    public static synchronized LeaveRepository getInstance(Context context){
        if(leaveRepository == null){
            leaveRepository = new LeaveRepository(context);
        }
        return leaveRepository;
    }

    public void leaveRequest(Ticket ticket, final LeaveAPICallbacks callbacks) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<LeaveTicket> call = apiService.submitLeaveRequest(accessToken, ticket);
        call.enqueue(new RestCallback<LeaveTicket>() {
            @Override
            public void success(LeaveTicket res) {
                callbacks.onContactDatabaseSuccess(res);
            }

            @Override
            public void failure(RestError error, String mess) {
                callbacks.onCreateDatabaseError(mess);
            }
        });
    }

    public void leaveTypeParams(final LeaveAPICallbacks callbacks) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<LeaveTypeDto> call = apiService.getLeaveTypeParams(accessToken);
        call.enqueue(new RestCallback<LeaveTypeDto>() {
            @Override
            public void success(LeaveTypeDto res) {
                callbacks.onContactDatabaseSuccess(res);
            }

            @Override
            public void failure(RestError error, String mess) {
                callbacks.onCreateDatabaseError(mess);
            }
        });
    }

}
