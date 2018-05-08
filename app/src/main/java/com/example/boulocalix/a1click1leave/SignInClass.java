package com.example.boulocalix.a1click1leave;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SignInClass extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 123;
    FirebaseUser currentUser ;
    private FirebaseAuth mAuth;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
    Context context ;
    Activity activity ;
    private static final String TAG = "LoginActivity";

    public SignInClass(Context context, Activity activity) {
        this.context = context ;
        this.activity = activity ;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("connection", "connection failed") ;
    }

    public void signIn() {
        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        currentUser = null ;
                        updateUI();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.e(TAG, currentUser.getDisplayName());
                Log.e(TAG, currentUser.getEmail());
                Log.e(TAG, currentUser.getPhotoUrl().toString());
                updateUI();
            } else {
                Toast.makeText(context, "Your account is not allowed to access this database", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateUI() {

        TextView userName = activity.findViewById(R.id.user_name_header);
        TextView userAddress = activity.findViewById(R.id.user_address);
        ImageView avatar = activity.findViewById(R.id.imageView);
        if (currentUser !=null) {
            userName.setText(currentUser .getDisplayName());
            userAddress.setText(currentUser .getEmail());
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.WHITE)
                    .borderWidthDp(2)
                    .oval(true)
                    .build();
            Picasso.get().load(currentUser.getPhotoUrl()).transform(transformation).into(avatar);
        }
        else {
            userAddress.setText("");
            userName.setText("");
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.WHITE)
                    .borderWidthDp(2)
                    .oval(true)
                    .build();
            Picasso.get().load("https://www.rapidcitytransportinc.com/assets/global/img/avatar.png").transform(transformation).into(avatar);
        }
    }
}
