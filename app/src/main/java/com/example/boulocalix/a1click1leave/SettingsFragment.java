package com.example.boulocalix.a1click1leave;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SettingsFragment extends Fragment implements  onMainToFragmentCallbacks{

    MainActivity main ;
    Context context = null ;
    TextView fullNameTV;
    EditText phoneET ;
    EditText backupET ;


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
        TextView user = main.findViewById(R.id.user_name_header) ;
        fullNameTV.setText(user.getText());
        return settings ;
    }


    @Override
    public void onMainToFragmentCallbacks(ArrayList<String> info) {

    }



}
