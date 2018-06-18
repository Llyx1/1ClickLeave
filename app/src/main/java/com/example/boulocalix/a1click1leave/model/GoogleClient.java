package com.example.boulocalix.a1click1leave.model;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class GoogleClient {
    private static GoogleSignInClient client;
    private static GoogleClient googleClient;

    private GoogleClient(GoogleSignInClient client) {
        this.client = client;
    }

    public static synchronized GoogleClient getInstance(GoogleSignInClient googleSignInClient) {
        if (googleClient == null) {
            googleClient = new GoogleClient(googleSignInClient);
            return googleClient;
        }
        return googleClient;
    }

    public static GoogleSignInClient getClient() {
        return client;
    }

    public static void destroy() {
        googleClient = null ;
    }
}