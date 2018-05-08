package com.example.boulocalix.a1click1leave;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.common.SignInButton;

import java.util.ArrayList;


public class FirstSignInFragment extends Fragment implements  onMainToFragmentCallbacks{


        MainActivity main ;
        Context context ;
        SignInClass signIn ;

    public FirstSignInFragment() {
        // Required empty public constructor
    }

    public static FirstSignInFragment newInstance() {
        FirstSignInFragment fragment = new FirstSignInFragment();
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
        signIn = new SignInClass(context,main) ;
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout signInView = (FrameLayout) inflater.inflate(R.layout.fragment_first_sign_in, container, false);
        SignInButton signInButton = signInView.findViewById(R.id.sign_in_button) ;
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn.signIn() ;
                main.onFragmentToMainCallbacks("FirstSignIn",null);
            }
        });
        return  signInView ;
    }

    @Override
    public void onMainToFragmentCallbacks(ArrayList<String> info) {

    }

}
