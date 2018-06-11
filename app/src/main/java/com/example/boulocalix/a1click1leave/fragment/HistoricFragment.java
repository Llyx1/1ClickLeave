package com.example.boulocalix.a1click1leave.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.boulocalix.a1click1leave.MainActivity;
import com.example.boulocalix.a1click1leave.R;
import com.example.boulocalix.a1click1leave.adapter.HistoricFragmentAdapter;
import com.example.boulocalix.a1click1leave.callbacks.onMainToFragmentCallbacks;
import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.model.Ticket;
import com.example.boulocalix.a1click1leave.util.ApiClient;
import com.example.boulocalix.a1click1leave.util.ApiInterface;
import com.example.boulocalix.a1click1leave.util.SharePrefer;
import com.google.gson.JsonObject;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoricFragment extends Fragment implements onMainToFragmentCallbacks, View.OnClickListener{
    MainActivity main ;
    Context context = null ;
    SharePrefer sharePrefer ;
    HistoricFragmentAdapter adapter;
    RecyclerView recyclerView ;

    public HistoricFragment() {
        // Required empty public constructor
    }


    public static HistoricFragment newInstance() {
        HistoricFragment fragment = new HistoricFragment();
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
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<List<Ticket>> call = apiService.getUserHistoric(sharePrefer.getAccessToken(), sharePrefer.getId());
        call.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if (response.body()!= null) {

                   List<Ticket> ticketList = response.body();
                    adapter = new HistoricFragmentAdapter(ticketList, context);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayout.VERTICAL, false));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                // Log error here since request failed
                Log.e("Json error", t.toString());
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        final FrameLayout history = (FrameLayout) inflater.inflate(R.layout.fragment_historic, container, false);
        recyclerView = history.findViewById(R.id.historic_recycler) ;
        return history ;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMainToFragmentCallbacks(Employee employee) {
    }
}
