package com.example.boulocalix.a1click1leave;

import android.app.Dialog;
import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.SubMenu;
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

import com.example.boulocalix.a1click1leave.callbacks.LeaveAPICallbacks;
import com.example.boulocalix.a1click1leave.fragment.FirstSignInFragment;
import com.example.boulocalix.a1click1leave.fragment.HistoricFragment;
import com.example.boulocalix.a1click1leave.fragment.HomePageFragment;
import com.example.boulocalix.a1click1leave.fragment.SettingsFragment;
import com.example.boulocalix.a1click1leave.fragment.SubmitALeaveFragment;
import com.example.boulocalix.a1click1leave.callbacks.onFragmentToMainCallbacks;
import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.model.GoogleClient;
import com.example.boulocalix.a1click1leave.model.LeaveTicket;
import com.example.boulocalix.a1click1leave.model.LeaveTypeDto;
import com.example.boulocalix.a1click1leave.repository.LeaveRepository;
import com.example.boulocalix.a1click1leave.util.ApiClient;
import com.example.boulocalix.a1click1leave.util.ApiInterface;

import com.example.boulocalix.a1click1leave.util.GenerateLeaveRequestUtil;
import com.example.boulocalix.a1click1leave.util.SharePrefer;
import com.example.boulocalix.a1click1leave.util.UpdateNavHeaderUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        onFragmentToMainCallbacks, View.OnClickListener, LeaveAPICallbacks{

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "HttpRequest";
    FragmentManager fragmentManager;
    Dialog myDialogConfirm;
    GoogleSignInAccount mGoogleSignInAccount ;
    Employee employee ;
    Fragment fragment;
    SharePrefer sharePrefer;
    LinearLayout navHeader;
    MenuItem balance ;
    List<String> leaveTips ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        sharePrefer = new SharePrefer(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        myDialogConfirm = new Dialog(this) ;
        fragmentManager = getSupportFragmentManager() ;
//        UpdateNavHeaderUtil.updateUI(navHeader, getApplicationContext());
        Intent intent = getIntent() ;
        if (intent.getBooleanExtra("connexion", false)) {
            fragment = SubmitALeaveFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragment_holder,fragment).commit();
        } else {
            fragment = FirstSignInFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();
        }
        LeaveRepository.getInstance(getApplicationContext()).leaveTypeParams(this);
    }

    public void signOut() {
        GoogleClient googleClient = GoogleClient.getInstance(null) ;
        GoogleSignInClient googleSignInClient = googleClient.getClient() ;
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        GoogleClient.destroy() ;
                        sharePrefer.reset() ;
                        UpdateNavHeaderUtil.updateUI(navHeader, getApplicationContext());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header:
                if (sharePrefer.getAccessToken() != null) {
                    fragment = SettingsFragment.newInstance();
                    fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
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
        navHeader = findViewById(R.id.nav_header);
        navHeader.setOnClickListener(this);
        UpdateNavHeaderUtil.updateUI(navHeader, getApplicationContext());
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 0, Menu.NONE, sharePrefer.getBalance().toString()).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        balance = menu.getItem(0) ;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Class fragmentClass = null;
        if (sharePrefer.getAccessToken() != null) {
            if (id == R.id.nav_submit_leave) {
                fragmentClass = SubmitALeaveFragment.class ;
            } else if (id == R.id.nav_history) {
                fragmentClass = HistoricFragment.class ;
            } else if (id == R.id.nav_reward) {

            } else if (id == R.id.nav_personnal_information) {
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.nav_header), "Disconnect", Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                signOut();
                                fragment = FirstSignInFragment.newInstance();
                                fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();
                            }
                        });

                snackbar.show();
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentToMainCallbacks(String sender,final ArrayList<String> info) {
        if (sender.equals("SubmitALeaveFragment")) {
            final double dayOff = Double.parseDouble(info.get(0)) ; // update the database with that information
            String[] answer = getResources().getStringArray(R.array.answer_to_leave) ;
            int[] pictureId = {
                    R.mipmap.hollidays,
                    R.mipmap.hollidays,
                    R.mipmap.sickness,
                    R.mipmap.baby,
                    R.mipmap.baby,
                    R.mipmap.funeral,
                    R.mipmap.funeral,
                    R.mipmap.wedding,
                    R.mipmap.wedding,
                    R.mipmap.working_from_home,
            } ;
            myDialogConfirm.setContentView(R.layout.pop_up_leave_recap);
            TextView txtclose =(TextView) myDialogConfirm.findViewById(R.id.txtclose);
            TextView leavingDate = myDialogConfirm.findViewById(R.id.day_leave_pop) ;
            TextView returnDate = myDialogConfirm. findViewById(R.id.day_return_pop) ;
            ImageView image = myDialogConfirm.findViewById(R.id.image_pop) ;
            final Button returnBtn = myDialogConfirm.findViewById(R.id.correct) ;
            Button confirm = myDialogConfirm.findViewById(R.id.confirm) ;
            TextView HRAnswer = myDialogConfirm.findViewById(R.id.hr_answer_pop) ;
            if (!info.get(1).equals(info.get(2))) {
                leavingDate.setText(info.get(1));
                returnDate.setText(info.get(2));
            } else {
                myDialogConfirm.findViewById(R.id.optionnal_recap).setVisibility(View.GONE);
            }
            final int spinnerPosition = Integer.parseInt(info.get(3)) ;
            image.setImageResource(pictureId[spinnerPosition]);
            HRAnswer.setText(leaveTips.get(spinnerPosition));
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnBTN() ;
                }
            });
            returnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   returnBTN() ;
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GenerateLeaveRequestUtil generateLeaveRequestUtil = new GenerateLeaveRequestUtil(getApplicationContext()) ;
                    generateLeaveRequestUtil.createAppropriateLeaveRequest(info);
                    balance.setTitle(sharePrefer.getBalance().toString()) ;
                    thankYouGiftDialog();
                }
            });
            myDialogConfirm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialogConfirm.show();
        }else if (sender.equals("SignIn")) {
            SubmitALeaveFragment submitALeaveFragment = SubmitALeaveFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragment_holder, submitALeaveFragment).commit();
            UpdateNavHeaderUtil.updateUI(navHeader, getApplicationContext());

        }else if (sender.equals("settings")) {
            if (info == null) {
                SettingsFragment settingsFragment = (SettingsFragment) fragment ;
                settingsFragment.onMainToFragmentCallbacks(employee) ;
            }
        }
    }

    public void returnBTN() {
        myDialogConfirm.dismiss();
    }

    public void returnHomePage(){
        myDialogConfirm.dismiss();
        HomePageFragment homePageFragment = HomePageFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.fragment_holder, homePageFragment).commit();
    }

    public void thankYouGiftDialog() {
        myDialogConfirm.setContentView(R.layout.pop_up_share_gift);
        final Button returnHomePage = myDialogConfirm.findViewById(R.id.return_hp);
        Button share = myDialogConfirm.findViewById(R.id.share_gg_plus);
        share.setOnClickListener(new View.OnClickListener() {
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
        returnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnHomePage() ;
            }
        });
    }

    @Override
    public void onCreateDatabaseError(String mess) {

    }

    @Override
    public void onContactDatabaseSuccess(Object data) {
        if (data instanceof LeaveTypeDto) {
            leaveTips = new ArrayList<>() ;
            for (int i= 0; i<((LeaveTypeDto) data).type.size(); i++) {
                leaveTips.add(((LeaveTypeDto) data).type.get(0).tips);
            }
        }
    }
}
