package com.example.boulocalix.a1click1leave.callbacks;

public interface LeaveAPICallbacks<T> {
    void onContactDatabaseSuccess(T data);
    void onCreateDatabaseError(String mess);
}
