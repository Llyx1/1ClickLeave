package com.example.boulocalix.a1click1leave.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.boulocalix.a1click1leave.model.Employee;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class SharePrefer {

    Context context;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    public static final String ACCESS_TOKEN = "Access_token";
    public static final String ID = "id" ;
    public static final String FULL_NAME = "Full_name" ;
    public static final String EMAIL = "Email" ;
    public static final String PICTURE = "Picture" ;
    public static final String BALANCE = "Balance" ;
    public static final String PHONE = "Phone" ;
    public static final String BACKUP = "Backup" ;
    public static final String CLUSTER = "Cluster" ;
    public static final String GOOGLE_CLIENT = "Client" ;

    public SharePrefer(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPref.edit();
        mEditor.apply();
    }

    public void setAccessToken(String accessToken) {
        if (accessToken!=null && !accessToken.equals("")){
            mEditor.putString(ACCESS_TOKEN, "Bearer " + accessToken);
        }
        mEditor.commit();
    }

    public void setId(int id) {
        if (id!=0){
            mEditor.putInt(ID, id);
        }
        mEditor.commit();
    }

    public void setFullName(String fullName) {
        if (fullName!=null && !fullName.equals("")){
            mEditor.putString(FULL_NAME, fullName);
        }
        mEditor.commit();
    }

    public void setEmail(String email) {
        if (email!=null && !email.equals("")){
            mEditor.putString(EMAIL, email);
        }
        mEditor.commit();
    }

    public void setPicture(String picture) {
        if (picture!=null && !picture.equals("")) {
            mEditor.putString(PICTURE, picture);
        }
        mEditor.commit();
    }

    public void setBalance(Double balance) {
        if (balance!=null){
            mEditor.putInt(BALANCE, (int) (balance*10));
        }
        mEditor.commit();
    }

    public void setPhone(String phone) {
        if (phone!=null && !phone.equals("")){
            mEditor.putString(PHONE, phone);
        }
        mEditor.commit();
    }

    public void setBackup(String backup) {
        if (backup!=null && !backup.equals("")){
            mEditor.putString(BACKUP, backup);
        }
        mEditor.commit();
    }

    public void setCluster(int cluster) {
        if (cluster!=0){
            mEditor.putInt(CLUSTER, cluster);
        }
        mEditor.commit();
    }

    public void setEmployee(Employee employee) {
        setAccessToken(employee.getIdToken());
        setId(employee.getId());
        setFullName(employee.getFullName());
        setEmail(employee.getEmail());
        setPicture(employee.getPhoto());
        setPhone(employee.getPhone());
        setBackup(employee.getBackupBuddy());
        setBalance(employee.getBalance());
        setCluster(employee.getCluster());

    }

    public String getAccessToken() {return mPref.getString(ACCESS_TOKEN, null);}
    public int getId() {return mPref.getInt(ID, 0);}
    public String getFullName(){return mPref.getString(FULL_NAME, null);}
    public String getEmail(){return mPref.getString(EMAIL, null); }
    public String getPicture() {return mPref.getString(PICTURE, "https://www.rapidcitytransportinc.com/assets/global/img/avatar.png");}
    public  Double getBalance(){return (double) (mPref.getInt(BALANCE, 0))/10;}
    public String getPhone() {return mPref.getString(PHONE, null); }
    public String getBackup() {return mPref.getString(BACKUP, null); }
    public  int getCluster() {return mPref.getInt(CLUSTER,0);}

}
