package com.example.boulocalix.a1click1leave;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by bouloc.alix on 4/23/2018.
 */

public class HomePageFragment extends Fragment implements onMainToFragmentCallbacks {

    MainActivity main ;
    Context context = null ;

    public HomePageFragment(){}

    public static HomePageFragment newInstance() {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            main = (MainActivity) getActivity() ;
            context = getActivity() ;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final RelativeLayout homePage = (RelativeLayout) inflater.inflate(R.layout.fragment_home_page, null);
        return homePage ;
    }

    @Override
    public void onMainToFragmentCallbacks(ArrayList<String> info) {
    }
}
