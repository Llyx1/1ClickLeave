package com.example.boulocalix.a1click1leave.model;

import com.google.gson.annotations.SerializedName;

public class Balance {

    private static Balance balance = new Balance() ;
    @SerializedName("balanceAnnualLeave")
    Double balanceAnnualLeave ;
}
