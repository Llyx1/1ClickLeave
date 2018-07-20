package com.example.boulocalix.a1click1leave.model;

import com.google.gson.annotations.SerializedName;

public final class Phone {

        private static Phone phone = new Phone() ;
        @SerializedName("phone")
        String phoneNumber ;

    public Phone(String phoneNumber) {

        this.phoneNumber = phoneNumber ;
    }

    private Phone(){}
    }
