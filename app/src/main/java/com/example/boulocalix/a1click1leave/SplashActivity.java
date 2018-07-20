package com.example.boulocalix.a1click1leave;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.boulocalix.a1click1leave.fragment.SubmitALeaveFragment;
import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.util.ApiClient;
import com.example.boulocalix.a1click1leave.util.ApiInterface;
import com.example.boulocalix.a1click1leave.util.SharePrefer;
import com.example.boulocalix.a1click1leave.model.GoogleClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    SharePrefer sharePrefer ;
    public static GoogleSignInClient mGoogleSignInClient ;
    GoogleSignInAccount mGoogleSignInAccount ;
    Employee employee ;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharePrefer = new SharePrefer(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView image = findViewById(R.id.image_splash) ;
                GifImageView gif = findViewById(R.id.video) ;
                image.setVisibility(View.GONE);
                gif.setVisibility(View.VISIBLE);
            }
        }, 1000);


    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance() ;
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this) ;
        if (result != ConnectionResult.SUCCESS) {
            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, result) ;
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("827349712490-u8rt9aemhfguugbnv897a1d6utn2dgai.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleClient.getInstance(mGoogleSignInClient) ;
        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                handleSignInResult(task);
            }
        }) ;
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            mGoogleSignInAccount = completedTask.getResult(ApiException.class);
            final String idToken = mGoogleSignInAccount.getIdToken();
            JsonObject idTokenJson = new JsonObject();
            idTokenJson.addProperty("id_token", idToken);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<Employee> call = apiService.signInemployee(idTokenJson);
            call.enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {
                    employee = response.body();
                    if (employee == null) {
                        Toast.makeText(getApplicationContext(), "Your address is not part of our database, are you using your offy address ?", Toast.LENGTH_LONG).show();
                        signOut();
                        callMain(false);
                    } else {
                        sharePrefer.setEmployee(employee);
                        sharePrefer.setPicture(mGoogleSignInAccount.getPhotoUrl().toString());
                        callMain(true);
                    }
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("Json error", t.toString());
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    callMain(false);
                }
            });
        } catch (ApiException e) {
            if (e.getStatusCode() == 4) {
                if (e instanceof ResolvableApiException) {
//                    ((ResolvableApiException) e).getResolution() ;
                    try {
                        ((ResolvableApiException) e).startResolutionForResult(this, 456);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    callMain(false); //authentication failed
                }
            } else {
                Log.w("SignIn", "handleSignInResult:error", e);
            }
        }
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        if (requestCode == 456) {
//            callMain(false);
        }
    }


    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        GoogleClient.destroy();
                    }
                });
    }

    private void callMain(Boolean bool) {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra("connexion", bool) ;
        startActivity(intent);
        finish();
        return;
    }
}
