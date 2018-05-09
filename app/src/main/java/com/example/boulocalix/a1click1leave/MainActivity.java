package com.example.boulocalix.a1click1leave;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

import com.example.boulocalix.a1click1leave.fragment.FirstSignInFragment;
import com.example.boulocalix.a1click1leave.fragment.HomePageFragment;
import com.example.boulocalix.a1click1leave.fragment.SettingsFragment;
import com.example.boulocalix.a1click1leave.fragment.SubmitALeaveFragment;
import com.example.boulocalix.a1click1leave.fragmentInterface.onFragmentToMainCallbacks;
import com.example.boulocalix.a1click1leave.util.SignInClass;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.plus.PlusShare;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.android.gms.plus.PlusShare;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        onFragmentToMainCallbacks, View.OnClickListener{
//    GoogleApiClient.OnConnectionFailedListener,
    FragmentManager fragmentManager;
    Dialog myDialog;
//    private static final int RC_SIGN_IN = 123;
//    List<AuthUI.IdpConfig> providers = Arrays.asList(
//            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
//    String email ;
    FirebaseUser currentUser ;
    private FirebaseAuth mAuth;
    SignInClass signIn ;
//    private static final String TAG = "LoginActivity";

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
        else {
            FirstSignInFragment firstSignInFragment = FirstSignInFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragment_holder, firstSignInFragment).commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header:
                currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    signIn.signIn();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.nav_header), "Disconnect", Snackbar.LENGTH_LONG)
                            .setAction("Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    signIn.signOut();
                                }
                            });

                    snackbar.show();
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final int RC_SIGN_IN = 123;
        final String TAG = "LoginActivity";
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.e(TAG, currentUser.getDisplayName());
                Log.e(TAG, currentUser.getEmail());
                Log.e(TAG, currentUser.getPhotoUrl().toString());
                signIn.updateUI();
            } else {
                Toast.makeText(getApplicationContext(), "Your account is not allowed to access this database", Toast.LENGTH_LONG).show();
            }
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
        signIn = new SignInClass(this, this) ;
        signIn.updateUI();
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
                    Intent shareIntent = new PlusShare.Builder(getApplicationContext())
                            .setType("text/plain")
                            .setText("Hello Offies, \n" +
                                    "I'll work from home today for my private reasons. For urgent cases, please call me directly or leave message on Hangout or skype.")
                            .setContentUrl(Uri.parse("https://plus.google.com/u/0/communities/114247689785052683839"))
                            .getIntent();

                    startActivityForResult(shareIntent, 0);
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        }
        if (sender.equals("FirstSignIn")) {
            SubmitALeaveFragment submitALeaveFragment = SubmitALeaveFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragment_holder, submitALeaveFragment).commit();
        }
    }

    public void returnHomePage(){
        myDialog.dismiss();
        HomePageFragment homePageFragment = HomePageFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.fragment_holder, homePageFragment).commit();
    }
}
