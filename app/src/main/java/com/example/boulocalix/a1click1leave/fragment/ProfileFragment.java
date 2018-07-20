package com.example.boulocalix.a1click1leave.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.boulocalix.a1click1leave.MainActivity;
import com.example.boulocalix.a1click1leave.R;
import com.example.boulocalix.a1click1leave.callbacks.LeaveAPICallbacks;
import com.example.boulocalix.a1click1leave.callbacks.onMainToFragmentCallbacks;
import com.example.boulocalix.a1click1leave.model.Backup;
import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.model.User;
import com.example.boulocalix.a1click1leave.repository.LeaveRepository;
import com.example.boulocalix.a1click1leave.util.ApiClient;
import com.example.boulocalix.a1click1leave.util.ApiInterface;
import com.example.boulocalix.a1click1leave.util.SharePrefer;
import com.google.gson.JsonObject;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements onMainToFragmentCallbacks, View.OnClickListener , LeaveAPICallbacks{

    MainActivity main ;
    Context context = null ;
    TextView fullNameTV;
    TextView phoneTV ;
    TextView layoutProvider ;
    static EditText phoneEdit ;
    LinearLayout backupLL ;
    LinearLayout backupEditLL ;
    ImageView plusBackup ;
    Button editBtn ;
    Button saveBtn ;
    Button logoutBtn ;
    SharePrefer sharePrefer ;
    ImageView profilePicture ;
    List<User> users ;
    List<User> backups ;
    static Boolean edit = false;




    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        LeaveRepository.getInstance(context).getUsers(this);
        backups = new ArrayList<>() ;
        }

    @Override
    public void onResume() {
        super.onResume();
        main.setBottomNavigationViewSelectedItem(R.id.navigation_profile);
        sharePrefer = new SharePrefer(context) ;
        LeaveRepository.getInstance(context).getUsers(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        final FrameLayout settings = (FrameLayout) inflater.inflate(R.layout.fragment_settings, container, false);
        TextView dayLeftInTotal =settings.findViewById(R.id.profile_total_day_off) ;
        TextView dayRemaining =settings.findViewById(R.id.profile_remaining_day) ;
        TextView dayAnnual =settings.findViewById(R.id.profile_total_annual_leave) ;
        dayLeftInTotal.setText(sharePrefer.getTotalDayOff().toString());
        dayAnnual.setText(sharePrefer.getTotalAnnualDayUsed().toString());
        dayRemaining.setText(sharePrefer.getBalance().toString());
        fullNameTV = settings.findViewById(R.id.user_fullname) ;
        phoneTV = settings.findViewById(R.id.user_phone);
        phoneEdit = settings.findViewById(R.id.user_phone_edit) ;
        backupLL = settings.findViewById(R.id.backupList) ;
        backupEditLL = settings.findViewById(R.id.backupListEdit) ;
        TextView cluster = settings.findViewById(R.id.user_cluster) ;
        editBtn = settings.findViewById(R.id.edit) ;
        saveBtn = settings.findViewById(R.id.save_edit) ;
        plusBackup = settings.findViewById(R.id.plus_one_backup) ;
        logoutBtn = settings.findViewById(R.id.logout);
        layoutProvider = settings.findViewById(R.id.backup_title) ;
        editBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        plusBackup.setOnClickListener(this);
        fullNameTV.setText(sharePrefer.getFullName());
        profilePicture = settings.findViewById(R.id.profile_picture) ;
        phoneTV.setText(sharePrefer.getPhone());
        phoneEdit.setText(sharePrefer.getPhone());
        cluster.setText(sharePrefer.getCluster());
        Picasso.with(context).load(sharePrefer.getPicture())
                .resize(100, 100)
                .into(profilePicture, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        profilePicture.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {

                    }
                });
        if (edit) {
            editBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.VISIBLE);
            backupLL.setVisibility(View.GONE);
            backupEditLL.setVisibility(View.VISIBLE);
            phoneEdit.setVisibility(View.VISIBLE);
            phoneTV.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.GONE);
        }
        return settings ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                editBtn.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);
                backupLL.setVisibility(View.GONE);
                backupEditLL.setVisibility(View.VISIBLE);
                phoneEdit.setVisibility(View.VISIBLE);
                phoneTV.setVisibility(View.GONE);
                logoutBtn.setVisibility(View.GONE);
                edit = true ;

                break;
            case R.id.save_edit:
                editBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.GONE);
                backupLL.setVisibility(View.VISIBLE);
                backupEditLL.setVisibility(View.GONE);
                phoneTV.setVisibility(View.VISIBLE);
                phoneEdit.setVisibility(View.GONE);
                logoutBtn.setVisibility(View.VISIBLE);
                edit = false ;
                LeaveRepository.getInstance(context).submitPhone(this,phoneEdit.getText().toString());
                updateProfile() ;
                sharePrefer.setPhone(phoneEdit.getText().toString());
                sharePrefer.setBackups(backups) ;
                break;
            case R.id.plus_one_backup:
                main.pushChooseBackupFragment(backups);
                break;
            case R.id.logout:
                main.onFragmentToMainCallbacks("settings", null);
                break;
                default:
                    for (int i=0; i<backups.size(); i++) {
                        if (v.getId() == backups.get(i).getId()) {
                            backups.remove(i);
                            break;
                        }
                    }
                    backupEditLL.removeView(v);
                   LeaveRepository.getInstance(context).deleteOneBackup(this, v.getId());



        }
    }


    public void updateProfile() {
        phoneTV.setText(phoneEdit.getText());
        backupLL.removeAllViews();
        for (int i= 0; i<backupEditLL.getChildCount()-1 ; i++) {
            if (backupEditLL.getChildAt(i).getVisibility()!=View.GONE) {
                TextView textView = new TextView(context);
                textView.setText(((TextView) backupEditLL.getChildAt(i)).getText().toString());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                textView.setTextColor(context.getResources().getColor(R.color.midtone));
                textView.setTypeface(layoutProvider.getTypeface());
                backupLL.addView(textView);
            }
        }
    }

    @Override
    public void onMainToFragmentCallbacks(User user) {
        TextView button = new TextView(context) ;
        button.setText(user.getFullName());
        LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 50) ;
        layoutParams.setMargins(0,10,0,10);
        button.setLayoutParams(layoutParams);
        button.setTypeface(layoutProvider.getTypeface());
        button.setTextColor(getResources().getColor(R.color.midtone));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        button.setAllCaps(false);
        button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        button.setBackground(getResources().getDrawable(R.drawable.backup_button));
        button.setOnClickListener(this);
        button.setId(user.getId()); //to be able to delete the good backup
        backupEditLL.addView(button, backupEditLL.getChildCount()-1);
        LeaveRepository.getInstance(context).submitOneBackup(this, user.getId());
        backups.add(user) ; 
    }

    @Override
    public void onContactDatabaseSuccess(Object data) {
        if (data instanceof List) {
            if (((List) data).size()>0) {
                if (((List) data).get(0) instanceof Backup) {
                    backupLL.removeAllViews();
                    backups.clear();
                    backupEditLL.removeAllViews();
                    backupEditLL.addView(plusBackup);
                    for (int i = 0; i < ((List) data).size(); i++) {
                        TextView button = new TextView(context);
                        User user = searchBackup(((Backup) ((List) data).get(i)).getBackupId());
                        if (user != null) {
                            button.setText(user.getFullName());
                            LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 60 );
                            layoutParams.setMargins(0,10,0,10);
                            button.setPadding( 30,5,5,0);
                            button.setLayoutParams(layoutParams);
                            button.setTypeface(layoutProvider.getTypeface());
                            button.setTextColor(getResources().getColor(R.color.dark));
                            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            button.setAllCaps(false);
                            button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                            button.setId(user.getId()) ;
                            button.setBackground(getResources().getDrawable(R.drawable.backup_button));
                            button.setOnClickListener(this);
//                    button.setBackground(getResources().getDrawable(R.mipmap.plus_plein, null));
                            backupEditLL.addView(button, backupEditLL.getChildCount() - 1);
                            TextView textView = new TextView(context);
                            textView.setText(user.getFullName());
                            textView.setTypeface(layoutProvider.getTypeface());
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            textView.setTextColor(context.getResources().getColor(R.color.midtone));
                            backupLL.addView(textView);
                            backups.add(user) ;
                        }
                    }
                }
                if (((List) data).get(0) instanceof User) {
                    users = (List<User>) data;
                    User user = searchBackup(sharePrefer.getId()) ;
                    phoneTV.setText(user.phone);
                    phoneEdit.setText(user.phone);
                    LeaveRepository.getInstance(context).getBackup(this);
                }
            }
        }

    }
    public User searchBackup(int id) {
        for (int i = 0 ; i<users.size(); i++) {
            if (users.get(i).getId() == id) {
                return users.get(i) ;
            }
        }
        return null ;
    }

    @Override
    public void onCreateDatabaseError(String mess) {

    }
}
