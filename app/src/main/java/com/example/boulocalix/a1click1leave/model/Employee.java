package com.example.boulocalix.a1click1leave.model;

import android.app.Application;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class Employee{


    private static Employee employee = new Employee() ;
    @SerializedName("accessToken")
    private String idToken ;
    @SerializedName("user")
    private User user ;
    @SerializedName("balanceAnnualLeave")
    private Double balance;


    private Employee() {
    }

    public static Employee getEmployee() {
        if (employee==null) {
            employee = new Employee() ;
        }
        return employee;
    }

    public User getUser() {
        return user;
    }

    public Double getBalance() {
        return balance;
    }

    public String getEmail() {
        return user.email;
    }

    public String getFullName() {
        return user.fullName;
    }

    public int getId() {
        return user.id;
    }

    public String getPhone() {
        return user.phone;
    }

    public String getCluster() {return  user.cluster;}

    public String getPhoto() {return user.photo; }

    public String getIdToken() {
        return idToken;
    }


    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setEmail(String email) {
        this.user.email = email;
    }

    public void setFullName(String fullName) {
        this.user.fullName = fullName;
    }

    public void setId(int id) {
        this.user.id = id;
    }

    public void setPhone(String phone) {
        this.user.phone = phone;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken ;
    }

    public void setCluster(String cluster) {this.user.cluster = cluster ;}


}
