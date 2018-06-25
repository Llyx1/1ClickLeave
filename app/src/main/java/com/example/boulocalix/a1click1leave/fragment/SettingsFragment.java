package com.example.boulocalix.a1click1leave.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.boulocalix.a1click1leave.MainActivity;
import com.example.boulocalix.a1click1leave.R;
import com.example.boulocalix.a1click1leave.callbacks.onMainToFragmentCallbacks;
import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.model.User;
import com.example.boulocalix.a1click1leave.util.ApiClient;
import com.example.boulocalix.a1click1leave.util.ApiInterface;
import com.example.boulocalix.a1click1leave.util.SharePrefer;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements onMainToFragmentCallbacks, View.OnClickListener {

    MainActivity main ;
    Context context = null ;
    TextView fullNameTV;
    TextView phoneTV ;
    TextView backupTV ;
    View editBtn ;
    SharePrefer sharePrefer ;


    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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
//            main.onFragmentToMainCallbacks("settings", null);
        sharePrefer = new SharePrefer(context) ;
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        final FrameLayout settings = (FrameLayout) inflater.inflate(R.layout.fragment_settings, container, false);
        fullNameTV = settings.findViewById(R.id.user_fullname) ;
        phoneTV = settings.findViewById(R.id.user_phone);
        backupTV = settings.findViewById(R.id.user_backup) ;
        editBtn = settings.findViewById(R.id.edit) ;
        editBtn.setOnClickListener(this);
        fullNameTV.setText(sharePrefer.getFullName());
        phoneTV.setText(sharePrefer.getPhone());
        backupTV.setText(sharePrefer.getBackup());
        return settings ;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.edit) {
            showEditDialog();
        }
    }

    private void showEditDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.pop_up_settings);
        final  LinearLayout linearLayout = dialog.findViewById(R.id.pop_up_settings) ;
        final EditText phoneET= dialog.findViewById(R.id.user_phone_edit);
        final EditText backupET = dialog.findViewById(R.id.user_backup_edit) ;
        Button dialogCancelButton = dialog.findViewById(R.id.cancel_btn);
        Button dialogSaveButton = dialog.findViewById(R.id.save_btn) ;
        Button addBackup = dialog.findViewById(R.id.add_backup) ;
        phoneET.setText(sharePrefer.getPhone());
        String backups = sharePrefer.getBackup() ;
        final ArrayList<EditText> backupsET = new ArrayList<>() ;
        int i = 0 ;
        while (backups.contains("\n")) {
            backupsET.add(new EditText(context));
            linearLayout.addView(backupsET.get(i), linearLayout.getChildCount()-2);
            backupsET.get(i).setText(backups.substring(backups.lastIndexOf("\n")+1));
            backupsET.get(i).setSingleLine();
            backupsET.get(i).setBackground(context.getDrawable(R.color.transparent));
            backupsET.get(i).setTextSize(18);
            backups = backups.substring(0,backups.lastIndexOf("\n")) ;
            i++ ;
        }
        backupET.setText(backups);
        backupsET.add(backupET) ;
        addBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backupsET.add(new EditText(context));
                linearLayout.addView(backupsET.get(backupsET.size()-1), linearLayout.getChildCount()-2);
                backupsET.get(backupsET.size()-1).setSingleLine();
                backupsET.get(backupsET.size()-1).setBackground(context.getDrawable(R.color.transparent));
                backupsET.get(backupsET.size()-1).setTextSize(18);
            }
        });
        // if ok button is clicked, close the custom dialog
        dialogSaveButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  String backups = "" ;
                                                  for (int i = backupsET.size()-1; i>=0; i--) {
                                                      if (backupsET.get(i).getText() != null|| backupsET.get(i).getText().toString().equals("") ) {
                                                          backups = backups + backupsET.get(i).getText().toString() + "\n";
                                                      }
                                                  }
                                                  APIProvider("backup", backups.substring(0, backups.length()-1));
                                                  APIProvider("phone", phoneET.getText().toString());
                                                  dialog.dismiss();
                                              }
                                          });
        // if close button is clicked, close the custom dialog
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void APIProvider(final String information, final String content) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        JsonObject info = new JsonObject();
        info.addProperty("id", sharePrefer.getId());
        Call<User> call ;
        if (information.equals("backup")){
            info.addProperty("backup",content);
            call= apiService.submitBackup(sharePrefer.getAccessToken(), info);
        } else {
            info.addProperty("phone", content);
            call = apiService.submitPhone(sharePrefer.getAccessToken(), info);
        }
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body()!= null) {
                    if (information.equals("backup")){
                        sharePrefer.setBackup(content);
                        backupTV.setText(content);
                    }else {
                   sharePrefer.setPhone(content);
                   phoneTV.setText(content);
                  }

                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Log error here since request failed
                Log.e("Json error", t.toString());
            }
        });
    }


    @Override
    public void onMainToFragmentCallbacks(Employee employee) {
    }



}
