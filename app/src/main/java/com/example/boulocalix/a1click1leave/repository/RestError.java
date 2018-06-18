package com.example.boulocalix.a1click1leave.repository;

import com.google.gson.annotations.SerializedName;

public class RestError {
    public static final int SERVER_ERROR = 404;
    public static final int NO_CONNECTION = 402;
    public static final int REQUEST_TIMEOUT = 902;
    @SerializedName("success")
    public int code;
    @SerializedName("error")
    public String msg;

    public RestError(int status) {
        this.code = status;
    }

    public RestError(int status, String msg) {
        this.code = status;
        this.msg = msg;
    }
}

