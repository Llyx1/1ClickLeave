package com.example.boulocalix.a1click1leave.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.boulocalix.a1click1leave.MainActivity;
import com.example.boulocalix.a1click1leave.R;
import com.example.boulocalix.a1click1leave.SplashActivity;
import com.example.boulocalix.a1click1leave.callbacks.onMainToFragmentCallbacks;
import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.model.GoogleClient;
import com.example.boulocalix.a1click1leave.model.User;
import com.example.boulocalix.a1click1leave.util.ApiClient;
import com.example.boulocalix.a1click1leave.util.ApiInterface;
import com.example.boulocalix.a1click1leave.util.SharePrefer;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FirstSignInFragment extends Fragment implements onMainToFragmentCallbacks {


        MainActivity main ;
        Context context ;
        GoogleSignInClient mGoogleSignInClient ;
        GoogleSignInAccount mGoogleSignInAccount ;
        private static final int RC_SIGN_IN = 123;
        SharePrefer sharePrefer ;

    public FirstSignInFragment() {
        // Required empty public constructor
    }

    public static FirstSignInFragment newInstance() {
        FirstSignInFragment fragment = new FirstSignInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            main = (MainActivity) getActivity() ;
            context = getActivity() ;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }

        sharePrefer = new SharePrefer(context) ;
        }

    @Override
    public void onResume() {
        super.onResume();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("827349712490-u8rt9aemhfguugbnv897a1d6utn2dgai.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        GoogleClient.getInstance(mGoogleSignInClient) ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout signInView = (FrameLayout) inflater.inflate(R.layout.fragment_first_sign_in, container, false);
        SignInButton signInButton = signInView.findViewById(R.id.sign_in_button) ;
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        return  signInView ;
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
                    Employee employee = response.body();

                    if (employee == null) {
                        Toast.makeText(context, "Your address is not part of our database, are you using your offy address ?", Toast.LENGTH_LONG).show();
                        signOut();
                    }
                    else {
                        sharePrefer.setEmployee(employee);
                        sharePrefer.setPicture(mGoogleSignInAccount.getPhotoUrl().toString());
                        main.onFragmentToMainCallbacks("SignIn", null);
                    }
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("Json error", t.toString());
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (ApiException e) {
            if (e.getStatusCode() == 4) {
                Toast.makeText(context, e.getStatusCode()+e.getMessage(), Toast.LENGTH_LONG).show();
                if (e instanceof ResolvableApiException) {
                    ((ResolvableApiException) e).getResolution() ;
                    try {
                        ((ResolvableApiException) e).startResolutionForResult(main, RC_SIGN_IN);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
                signIn();
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
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        GoogleClient.destroy() ;
                    }
                });

    }


    @Override
    public void onMainToFragmentCallbacks(User user) {

    }

}
