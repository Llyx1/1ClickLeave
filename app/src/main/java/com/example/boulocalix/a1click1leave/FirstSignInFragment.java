package com.example.boulocalix.a1click1leave;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class FirstSignInFragment extends Fragment implements  onMainToFragmentCallbacks{


    public FirstSignInFragment() {
        // Required empty public constructor
    }

    public static FirstSignInFragment newInstance(String param1, String param2) {
        FirstSignInFragment fragment = new FirstSignInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_sign_in, container, false);
    }

    @Override
    public void onMainToFragmentCallbacks(ArrayList<String> info) {

    }

}
