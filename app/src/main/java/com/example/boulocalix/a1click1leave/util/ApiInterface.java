package com.example.boulocalix.a1click1leave.util;

import com.example.boulocalix.a1click1leave.model.Backup;
import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.model.LeaveTicket;
import com.example.boulocalix.a1click1leave.model.LeaveTypeDto;
import com.example.boulocalix.a1click1leave.model.Phone;
import com.example.boulocalix.a1click1leave.model.SubmitTicket;
import com.example.boulocalix.a1click1leave.model.Ticket;
import com.example.boulocalix.a1click1leave.model.User;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @Headers("Accept: application/json")
    @GET("offyusers")
    Call<List<User>> getAllEmployees(@Header("Authorization") String token);

    @Headers("Accept: application/json")
    @GET("offyusers/{id}")
    Call<Employee> getRequestedEmployee(@Header("Authorization") String token, @Path("id") int id);

    @Headers("Accept: application/json")
    @GET("loginGoogle/{gmail}")
    Call<Employee> getSignInEmployee( @Header("Authorization") String token, @Path("gmail") String user);

//    @FormUrlEncoded
//    @Headers("Content-type: application/json")
    @POST("googleLogin")
    Call<Employee> signInemployee(@Body JsonObject id) ;

    @Headers("Content-type: application/json")
    @POST("submitPhone")
    Call<User> submitPhone(@Header("Authorization") String token, @Body Phone phone) ;

    @Headers("Content-type: application/json")
    @POST("submitLeaveTicket")
    Call<LeaveTicket> submitLeaveRequest(@Header("Authorization") String token, @Body SubmitTicket leaveRequest) ;

    @Headers("Accept: application/json")
    @GET("leaveHistoryOfUser")
    Call<List<Ticket>> getUserHistoric(@Header("Authorization") String token) ;

    @Headers("Accept: application/json")
    @GET("getLeaveTypeParams")
    Call<LeaveTypeDto> getLeaveTypeParams(@Header("Authorization") String token) ;

    @Headers("Accept: application/json")
    @GET("deleteBackupBuddy/{backup_buddy_id_delete}")
    Call<ResponseBody> deleteBackups(@Header("Authorization") String token, @Path("backup_buddy_id_delete") int id) ;

    @Headers("Accept: application/json")
    @GET("cancelLeave/{id}")
    Call<ResponseBody> deleteLeave(@Header("Authorization") String token, @Path("id") int id) ;

    @Headers("Accept: application/json")
    @GET("getBackupBuddyById")
    Call<List<Backup>> getBackups(@Header("Authorization") String token) ;

    @Headers("Accept: application/json")
    @POST("submitBackupBuddyById")
    Call<ResponseBody> submitOneBackup(@Header("Authorization") String token, @Body Backup backup) ;

}
