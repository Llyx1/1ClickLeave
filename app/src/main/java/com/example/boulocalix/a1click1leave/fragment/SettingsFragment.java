package com.example.boulocalix.a1click1leave.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.boulocalix.a1click1leave.MainActivity;
import com.example.boulocalix.a1click1leave.R;
import com.example.boulocalix.a1click1leave.callbacks.onMainToFragmentCallbacks;
import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.util.ApiClient;
import com.example.boulocalix.a1click1leave.util.ApiInterface;
import com.example.boulocalix.a1click1leave.util.SharePrefer;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements onMainToFragmentCallbacks, View.OnClickListener {

    MainActivity main ;
    Context context = null ;
    TextView fullNameTV;
    EditText phoneET ;
    EditText backupET ;
    Button editPhoneBtn ;
    Button editBackupBtn ;
    SharePrefer sharePrefer ;


    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(Employee employee) {
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
            main.onFragmentToMainCallbacks("settings", null);
        sharePrefer = new SharePrefer(context) ;
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        final FrameLayout settings = (FrameLayout) inflater.inflate(R.layout.fragment_settings, container, false);
        fullNameTV = settings.findViewById(R.id.user_fullname) ;
        phoneET = settings.findViewById(R.id.user_phone);
        backupET = settings.findViewById(R.id.user_backup) ;
        editBackupBtn = settings.findViewById(R.id.edit_button_backup) ;
        editBackupBtn.setOnClickListener(this);
        editPhoneBtn = settings.findViewById(R.id.edit_button_phone) ;
        editPhoneBtn.setOnClickListener(this);
        fullNameTV.setText(sharePrefer.getFullName());
        phoneET.setText(sharePrefer.getPhone());
        backupET.setText(sharePrefer.getBackup());
        return settings ;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.edit_button_backup || v.getId()==R.id.edit_button_phone) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            JsonObject info = new JsonObject();
            info.addProperty("id", sharePrefer.getId());
            Call<Employee> call ;
            if (v.getId()==R.id.edit_button_backup) {
                info.addProperty("backup", backupET.getText().toString());
                 call= apiService.submitBackup(sharePrefer.getAccessToken(), info);
            }else {
                    info.addProperty("phone", phoneET.getText().toString());
                    call = apiService.submitPhone(sharePrefer.getAccessToken(), info);
            }
            call.enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {
                    if (response.body()!= null) {

                        Employee employee = response.body();
                        sharePrefer.setEmployee(employee);
                    }
                }
                @Override
                public void onFailure(Call<Employee> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("Json error", t.toString());
                }
            });
        }
    }

    @Override
    public void onMainToFragmentCallbacks(Employee employee) {
    }



}
