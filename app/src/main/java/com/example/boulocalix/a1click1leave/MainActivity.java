package com.example.boulocalix.a1click1leave;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.boulocalix.a1click1leave.callbacks.LeaveAPICallbacks;
import com.example.boulocalix.a1click1leave.fragment.ChooseBackup;
import com.example.boulocalix.a1click1leave.fragment.FirstSignInFragment;
import com.example.boulocalix.a1click1leave.fragment.HistoricFragment;
import com.example.boulocalix.a1click1leave.fragment.ProfileFragment;
import com.example.boulocalix.a1click1leave.fragment.SubmitALeaveFragment;
import com.example.boulocalix.a1click1leave.callbacks.onFragmentToMainCallbacks;
import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.model.GoogleClient;
import com.example.boulocalix.a1click1leave.model.LeaveTypeDto;
import com.example.boulocalix.a1click1leave.model.User;
import com.example.boulocalix.a1click1leave.repository.LeaveRepository;

import com.example.boulocalix.a1click1leave.util.BottomNavigationViewEx;
import com.example.boulocalix.a1click1leave.util.GenerateLeaveRequestUtil;
import com.example.boulocalix.a1click1leave.util.SharePrefer;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class MainActivity extends AppCompatActivity implements onFragmentToMainCallbacks, View.OnClickListener, LeaveAPICallbacks{

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "HttpRequest";
    FragmentManager fragmentManager;
    Dialog myDialogConfirm;
    GoogleSignInAccount mGoogleSignInAccount ;
    Employee employee ;
    Fragment fragment;
    SharePrefer sharePrefer;
    TextView balance ;
    TextView userName ;
    List<String> leaveTips ;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener ;
    private BottomNavigationViewEx bottomNavigationView ;
    RelativeLayout mTopToolbar ;
    String gift ;
    String thanks ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        sharePrefer = new SharePrefer(this);
        myDialogConfirm = new Dialog(this) ;
        fragmentManager = getSupportFragmentManager() ;
        bottomNavigationView = findViewById(R.id.navigation) ;
        balance = findViewById(R.id.balance_toolbar) ;
        userName = findViewById(R.id.user_name_toolbar);
        mOnNavigationItemSelectedListener  = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_submit_a_leave:
                        mTopToolbar.setVisibility(View.VISIBLE);
                        fragment = SubmitALeaveFragment.newInstance() ;
                        ft.replace(R.id.fragment_holder, fragment, "LeaveRequestFragment");
                        ft.addToBackStack("LeaveRequestFragment");
                        ft.commitAllowingStateLoss();
                            return true;

                    case R.id.navigation_historic:
                        mTopToolbar.setVisibility(View.GONE);
                        fragment = HistoricFragment.newInstance() ;
                        ft.replace(R.id.fragment_holder, fragment, "HistoryFragment");
                        ft.addToBackStack("HistoryFragment");
                        ft.commitAllowingStateLoss();
                            return true;

                    case R.id.navigation_profile:
                        mTopToolbar.setVisibility(View.GONE);
                        fragment = ProfileFragment.newInstance() ;
                        ft.replace(R.id.fragment_holder, fragment, "ProfileFragment");
                        ft.addToBackStack("ProfileFragment");
                        ft.commitAllowingStateLoss();
                             return true;
                }
                return false;
            }
        };
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        removeShiftMode(bottomNavigationView);
        bottomNavigationView.enableItemShiftingMode(false);
        bottomNavigationView.setTextVisibility(false);
        mTopToolbar = (RelativeLayout) findViewById(R.id.toolbar);
        Intent intent = getIntent() ;
        if (intent.getBooleanExtra("connexion", false)) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            fragment = SubmitALeaveFragment.newInstance() ;
            ft.replace(R.id.fragment_holder, fragment, "LeaveRequestFragment");
            ft.addToBackStack("LeaveRequestFragment");
            ft.commitAllowingStateLoss();
            mTopToolbar.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
            mTopToolbar.setVisibility(View.GONE);
            fragment = FirstSignInFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();
        }
        LeaveRepository.getInstance(getApplicationContext()).leaveTypeParams(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LeaveRepository.getInstance(this).getBackup(this);
        balance.setText(sharePrefer.getBalance().toString());
        userName.setText(sharePrefer.getFullName());
    }

    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BottomNav", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BottomNav", "Unable to change value of shift mode", e);
        }
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
                        //TODO update ProfileFragment
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.nav_header:
//                if (sharePrefer.getAccessToken() != null) {
//                    fragment = ProfileFragment.newInstance();
//                    fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();
//                }
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                break;
        }
    }


    @Override
    public void onFragmentToMainCallbacks(String sender,final ArrayList<String> info) {
        if (sender.equals("SubmitALeaveFragment")) {
            myDialogConfirm.setContentView(R.layout.pop_up_leave_recap);
            myDialogConfirm.setCancelable(false);
            myDialogConfirm.setCanceledOnTouchOutside(false);
            TextView text = myDialogConfirm.findViewById(R.id.text_pop_up_confirm) ;
            text.setText(setTextPopUp(info));
            if (Build.VERSION.SDK_INT >= 26) {
                text.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }
            final Button returnBtn = myDialogConfirm.findViewById(R.id.correct) ;
            final Button confirm = myDialogConfirm.findViewById(R.id.confirm) ;
            final MainActivity main = this ;
            returnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   returnBTN() ;
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirm.setOnClickListener(null);
                    returnBtn.setOnClickListener(null);
                    GenerateLeaveRequestUtil generateLeaveRequestUtil = new GenerateLeaveRequestUtil(getApplicationContext(), main) ;
                    generateLeaveRequestUtil.createAppropriateLeaveRequest(info);
                    balance.setText(sharePrefer.getBalance().toString()) ;
                }
            });
            myDialogConfirm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialogConfirm.show();
        }else if (sender.equals("SignIn")) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            mTopToolbar.setVisibility(View.VISIBLE);
            SubmitALeaveFragment submitALeaveFragment = SubmitALeaveFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragment_holder, submitALeaveFragment).commit();
            bottomNavigationView.setSelectedItemId(R.id.navigation_submit_a_leave);
            balance.setText(sharePrefer.getBalance().toString());
            userName.setText(sharePrefer.getFullName());


        }else if (sender.equals("settings")) {
            signOut();
            bottomNavigationView.setVisibility(View.GONE);
            fragment = FirstSignInFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();
            mTopToolbar.setVisibility(View.GONE);

        }
    }

    private SpannableStringBuilder setTextPopUp(ArrayList<String> info) {
        ForegroundColorSpan pink = new ForegroundColorSpan(Color.RED) ;
        SpannableStringBuilder SS ;
        String content1, content2, content3, content4;
        if (info.get(1).equals(info.get(2))) {
            content1 = "You have just created a " ;
            content2 = info.get(0) ;
            content3 = "-day leave request on " ;
            content4 = info.get(1) ;
            String content5 = ". Please, confirm to continue." ;
            SS = new SpannableStringBuilder(content1 + content2+content3+content4+content5);
        }else {
            content1 = "You have just created a leave request for ";
            content2 = info.get(0) ;
            content3 = " days from " ;
            content4 = info.get(1) ;
            String content5 = " to " ;
            String content6 = info.get(2) ;
            String content7 = ". Please, confirm to continue." ;
            String content8 ;
            if (Double.parseDouble(info.get(0)) > 12.0) {
                content8 = "\nYou will need to meet HR to have the approval of that leave request." ;
            } else {
                content8 = "" ;
            }
            SS = new SpannableStringBuilder(content1 + content2+content3+content4+content5+content6+content7+content8);
            SS.setSpan(new StyleSpan(Typeface.BOLD),
                    content1.length()+content2.length()+content3.length()+content4.length()+content5.length(),
                    content1.length()+content2.length()+content3.length()+content4.length()+content5.length()+content6.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        }
        SS.setSpan(pink,
                content1.length(),
                content1.length()+content2.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        SS.setSpan(new StyleSpan(Typeface.BOLD),
                content1.length(),
                content1.length()+content2.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        SS.setSpan(new StyleSpan(Typeface.BOLD),
                content1.length()+content2.length()+content3.length(),
                content1.length()+content2.length()+content3.length()+content4.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

        return SS;
    }

    public void returnBTN() {
        myDialogConfirm.dismiss();
    }

    public void returnHistoricLeavePage(){
        //TODO to be change to another fragment
        myDialogConfirm.dismiss();
         HistoricFragment historicFragment = HistoricFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.fragment_holder, historicFragment).commit();
    }

    public void thankYouGiftDialog(String gift, String thanks,String tips) {
        myDialogConfirm.setContentView(R.layout.pop_up_share_gift);
        myDialogConfirm.setCanceledOnTouchOutside(false);
        myDialogConfirm.setCancelable(false);
        TextView thanksTV = myDialogConfirm.findViewById(R.id.thank_you_message) ;
        ImageView giftIV = myDialogConfirm.findViewById(R.id.gift_picture) ;
        TextView giftTV = myDialogConfirm.findViewById(R.id.gift_message) ;
        TextView tipsTV = myDialogConfirm.findViewById(R.id.gift_tips) ;
        thanksTV.setText(thanks);
        if (gift == null) {
            giftIV.setVisibility(View.GONE);
            giftTV.setVisibility(View.GONE);
        }else {
            giftIV.setVisibility(View.VISIBLE);
            giftTV.setVisibility(View.VISIBLE);
            giftTV.setText("Here a " + gift + " for you ! Please contact HR to claim it.");
        }
        tipsTV.setText(tips);
        final Button returnHomePage = myDialogConfirm.findViewById(R.id.return_hp);
        Button share = myDialogConfirm.findViewById(R.id.share_gg_plus);
        String text =  "Hello Offies, \nI'll work from home today for my private reasons. For urgent cases, please call me directly " ;
        if (sharePrefer.getPhone() != null || !sharePrefer.getPhone().equals("")) {
            text = text + "at " + sharePrefer.getPhone() + ", ";
        }
        text = text + "leave a message on Hangout or skype" ;
        if (sharePrefer.getBackup() != null) {
            text = text + " or contact my backup(s)" + sharePrefer.getBackup() ;
        }
        text = text + "." ;
        final String textFinal = text ;
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new PlusShare.Builder(getApplicationContext())
                        .setType("text/plain")
                        .setText(textFinal)
                        .setContentUrl(Uri.parse("https://plus.google.com/u/0/communities/114247689785052683839"))
                        .getIntent();

                startActivityForResult(shareIntent, 0);
            }
        });
        returnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnHistoricLeavePage() ;
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

        if (data instanceof List) {
            if (((List) data).size()>0 && ((List) data).get(0) instanceof User) {
                sharePrefer.setBackups((List<User>) data);
            }
        }
    }

    public void setNewBackup(User employee) {
        ((ProfileFragment) fragment).onMainToFragmentCallbacks(employee) ;
    }

    public void popPreviousFragmentWithoutReload() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

    public void pushChooseBackupFragment(List<User> backups) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ChooseBackup chooseBackup = ChooseBackup.newInstance(backups) ;
        ft.replace(R.id.fragment_holder, chooseBackup, "ChooseBackup");
        ft.addToBackStack("ChooseBackup");
        ft.commitAllowingStateLoss();
    }

    public void setBottomNavigationViewSelectedItem(int id) {
        if (bottomNavigationView.getSelectedItemId() != id) {
            bottomNavigationView.setSelectedItemId(id);
        }
    }

    public void updateBalance() {
        balance.setText(sharePrefer.getBalance().toString());
        if (fragment instanceof HistoricFragment) {
            ((HistoricFragment) fragment).onMainToFragmentCallbacks(null);
        }
    }

    public void failSubmitleave() {
        myDialogConfirm.dismiss();
    }

}
