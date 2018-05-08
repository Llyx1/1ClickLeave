package com.example.boulocalix.a1click1leave;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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
//import com.google.android.gms.plus.PlusShare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
onFragmentToMainCallbacks,GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    FragmentManager fragmentManager;
    Dialog myDialog;
    private static final int RC_SIGN_IN = 123;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
    String email ;
    FirebaseUser currentUser ;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        myDialog = new Dialog(this) ;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fragmentManager = getSupportFragmentManager() ;
        if (currentUser != null) {
            SubmitALeaveFragment submitALeaveFragment = SubmitALeaveFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragment_holder, submitALeaveFragment).commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header:
                if (currentUser == null) {
                    signIn();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.nav_header), "Disconnect", Snackbar.LENGTH_LONG)
                            .setAction("Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    signOut();
                                }
                            });

                    snackbar.show();
                }

                break;
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("connection", "connection failed") ;
    }



    private void signIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
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
                Toast.makeText(this, "Your account is not allowed to access this database", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void updateUI() {
        TextView userName = findViewById(R.id.user_name_header);
        TextView userAddress = findViewById(R.id.user_address);
        ImageView avatar = findViewById(R.id.imageView);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        LinearLayout navHearder = findViewById(R.id.nav_header) ;
        navHearder.setOnClickListener(this);
        updateUI();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_submit_leave) {
            fragmentClass = SubmitALeaveFragment.class ;
        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_reward) {

        } else if (id == R.id.nav_personnal_information) {
            fragmentClass = SettingsFragment.class ;
        } else if (id == R.id.nav_home_page) {
            fragmentClass = HomePageFragment.class ;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment!=null) {
        fragmentManager = getSupportFragmentManager() ;
        fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentToMainCallbacks(String sender, ArrayList<String> info) {
        if (sender.equals("SubmitALeaveFragment")) {
            double dayOff = Double.parseDouble(info.get(0)) ; // update the database with that information
            String[] answer = getResources().getStringArray(R.array.answer_to_leave) ;
            int[] pictureId = {
                    R.mipmap.hollidays,
                    R.mipmap.wedding,
                    R.mipmap.wedding,
                    R.mipmap.hollidays,
                    R.mipmap.sickness,
                    R.mipmap.baby,
                    R.mipmap.baby,
                    R.mipmap.funeral,
                    R.mipmap.funeral,
                    R.mipmap.working_from_home,
            } ;
            myDialog.setContentView(R.layout.pop_up_layout);
            TextView txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
            TextView leavingDate = myDialog.findViewById(R.id.day_leave_pop) ;
            TextView returnDate = myDialog . findViewById(R.id.day_return_pop) ;
            ImageView image = myDialog.findViewById(R.id.image_pop) ;
            final Button returnHomePage = myDialog.findViewById(R.id.return_hp) ;
            Button shareGGPlus = myDialog.findViewById(R.id.share_gg_plus) ;
            TextView HRAnswer = myDialog.findViewById(R.id.hr_answer_pop) ;
            leavingDate.setText(info.get(1));
            returnDate.setText(info.get(2));
            int spinnerPosition = Integer.parseInt(info.get(3)) ;
            image.setImageResource(pictureId[spinnerPosition]);
            HRAnswer.setText(answer[spinnerPosition]);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnHomePage() ;
                }
            });
            returnHomePage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   returnHomePage() ;
                }
            });
            shareGGPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent shareIntent = new PlusShare.Builder(getApplicationContext())
//                            .setType("text/plain")
//                            .setText("Welcome to the Google+ platform.")
//                            .setContentUrl(Uri.parse("https://developers.google.com/+/"))
//                            .getIntent();
//
//                    startActivityForResult(shareIntent, 0);
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        }
    }

    public void returnHomePage(){
        myDialog.dismiss();
        HomePageFragment homePageFragment = HomePageFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.fragment_holder, homePageFragment).commit();
    }
}
