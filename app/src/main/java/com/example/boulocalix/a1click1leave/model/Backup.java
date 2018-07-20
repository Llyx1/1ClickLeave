package com.example.boulocalix.a1click1leave.model;

import com.google.gson.annotations.SerializedName;

public final class Backup {

    private static Backup backup = new Backup() ;
    @SerializedName("backup_buddy_id")
    int backupId ;
    String name;
    String email ;

    public Backup() {}

    public Backup(int backupId) {
        this.backupId = backupId ;
    }

    public int getBackupId() {
        return backupId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setBackupId(int backupId) {
        this.backupId = backupId;
    }
}
