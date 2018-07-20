package com.example.boulocalix.a1click1leave.repository;

import android.content.Context;

import com.example.boulocalix.a1click1leave.callbacks.LeaveAPICallbacks;
import com.example.boulocalix.a1click1leave.callbacks.RestCallback;
import com.example.boulocalix.a1click1leave.model.Backup;
import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.model.LeaveTicket;
import com.example.boulocalix.a1click1leave.model.LeaveTypeDto;
import com.example.boulocalix.a1click1leave.model.Phone;
import com.example.boulocalix.a1click1leave.model.SubmitTicket;
import com.example.boulocalix.a1click1leave.model.Ticket;
import com.example.boulocalix.a1click1leave.model.User;
import com.example.boulocalix.a1click1leave.util.ApiClient;
import com.example.boulocalix.a1click1leave.util.ApiInterface;
import com.example.boulocalix.a1click1leave.util.SharePrefer;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
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

    public void leaveRequest(SubmitTicket ticket, final LeaveAPICallbacks callbacks) {
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

    public void getUsers(final LeaveAPICallbacks callbacks) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<List<User>> call = apiService.getAllEmployees(accessToken);
        call.enqueue(new RestCallback<List<User>>() {
            @Override
            public void success(List<User> res) {
                callbacks.onContactDatabaseSuccess(res);
            }

            @Override
            public void failure(RestError error, String mess) {
                callbacks.onCreateDatabaseError(mess);
            }
        });
    }

    public void deleteLeaveRequest(final LeaveAPICallbacks callbacks, int leaveId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.deleteLeave(accessToken, leaveId);
        call.enqueue(new RestCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody res) {
                callbacks.onContactDatabaseSuccess(res);
            }

            @Override
            public void failure(RestError error, String mess) {
                callbacks.onCreateDatabaseError(mess);
            }
        });
    }
    public void getBackup(final LeaveAPICallbacks callbacks) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<List<Backup>> call = apiService.getBackups(accessToken);
        call.enqueue(new RestCallback<List<Backup>>() {
            @Override
            public void success(List<Backup> res) {
                callbacks.onContactDatabaseSuccess(res);
            }

            @Override
            public void failure(RestError error, String mess) {
                callbacks.onCreateDatabaseError(mess);
            }
        });
    }

    public void submitOneBackup(final LeaveAPICallbacks callbacks, int backupId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.submitOneBackup(accessToken, new Backup(backupId));
        call.enqueue(new RestCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody res) {
                callbacks.onContactDatabaseSuccess(res);
            }

            @Override
            public void failure(RestError error, String mess) {
                callbacks.onCreateDatabaseError(mess);
            }
        });
    }

    public void deleteOneBackup(final LeaveAPICallbacks callbacks, int backupId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.deleteBackups(accessToken,backupId);
        call.enqueue(new RestCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody res) {
                callbacks.onContactDatabaseSuccess(res);
            }

            @Override
            public void failure(RestError error, String mess) {
                callbacks.onCreateDatabaseError(mess);
            }
        });
    }

    public void submitPhone(final LeaveAPICallbacks callbacks, String phone) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<User> call = apiService.submitPhone(accessToken, new Phone(phone));
        call.enqueue(new RestCallback<User>() {
            @Override
            public void success(User res) {
                callbacks.onContactDatabaseSuccess(res);
            }

            @Override
            public void failure(RestError error, String mess) {
                callbacks.onCreateDatabaseError(mess);
            }
        });
    }

    public void getLeaveHistory(final LeaveAPICallbacks callbacks) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call <List<Ticket>> call = apiService.getUserHistoric(accessToken);
        call.enqueue(new RestCallback<List<Ticket>>() {
            @Override
            public void success(List<Ticket> res) {
                callbacks.onContactDatabaseSuccess(res);
            }

            @Override
            public void failure(RestError error, String mess) {
                callbacks.onCreateDatabaseError(mess);
            }
        });
    }

}
