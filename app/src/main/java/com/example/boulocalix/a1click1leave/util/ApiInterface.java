package com.example.boulocalix.a1click1leave.util;

import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.model.LeaveTicket;
import com.example.boulocalix.a1click1leave.model.Ticket;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("offyusers/")
    Call<List<Employee>> getAllEmployees();


    @Headers("Accept: application/json")
    @GET("loginGoogle/{gmail}")
    Call<Employee> getSignInEmployee( @Header("Authorization") String token, @Path("gmail") String user);

//    @FormUrlEncoded
//    @Headers("Content-type: application/json")
    @POST("googleLogin")
    Call<Employee> signInemployee(@Body JsonObject id) ;

    @Headers("Content-type: application/json")
    @POST("submitPhone")
    Call<Employee> submitPhone(@Header("Authorization") String token, @Body JsonObject phone) ;

    @Headers("Content-type: application/json")
    @POST("submitBackup")
    Call<Employee> submitBackup(@Header("Authorization") String token, @Body JsonObject backup) ;

    @Headers("Content-type: application/json")
    @POST("submitLeaveTicket")
    Call<LeaveTicket> submitLeaveRequest(@Header("Authorization") String token, @Body Ticket leaveRequest) ;

    @Headers("Accept: application/json")
    @GET("leaveHistoryOfUser/{id}")
    Call<List<Ticket>> getUserHistoric(@Header("Authorization") String token, @Path("id") int id) ;


}
