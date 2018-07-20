package com.example.boulocalix.a1click1leave.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.boulocalix.a1click1leave.MainActivity;
import com.example.boulocalix.a1click1leave.R;
import com.example.boulocalix.a1click1leave.adapter.HistoricFragmentAdapter;
import com.example.boulocalix.a1click1leave.callbacks.LeaveAPICallbacks;
import com.example.boulocalix.a1click1leave.callbacks.onMainToFragmentCallbacks;
import com.example.boulocalix.a1click1leave.model.Ticket;
import com.example.boulocalix.a1click1leave.model.User;
import com.example.boulocalix.a1click1leave.repository.LeaveRepository;
import com.example.boulocalix.a1click1leave.util.SharePrefer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HistoricFragment extends Fragment implements onMainToFragmentCallbacks, LeaveAPICallbacks{
    MainActivity main ;
    Context context = null ;
    SharePrefer sharePrefer ;
    HistoricFragmentAdapter adapter;
    RecyclerView recyclerView ;
    Spinner leaveTypeFilter ;
    Spinner statusFilter ;
    List<Ticket> ticketList ;
    List<Ticket> ticketListDisplayed ;
    Boolean firstUpdate = false ;

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
        ticketListDisplayed = new ArrayList<>() ;

    }

    @Override
    public void onResume() {
        super.onResume();
        main.setBottomNavigationViewSelectedItem(R.id.navigation_historic);
        ticketListDisplayed = new ArrayList<>();
        firstUpdate = true ;
        LeaveRepository.getInstance(context).getLeaveHistory(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        final FrameLayout history = (FrameLayout) inflater.inflate(R.layout.fragment_historic, container, false);
        recyclerView = history.findViewById(R.id.historic_recycler) ;
        leaveTypeFilter = history.findViewById(R.id.leave_type_filter) ;
        statusFilter = history.findViewById(R.id.status_filter) ;
        ArrayAdapter adapter = ArrayAdapter.createFromResource(context,
                R.array.historic_leave_type, R.layout.spinner_item_historic_single);
        adapter.setDropDownViewResource(R.layout.spinner_item_historic_dropdown);
        leaveTypeFilter.setAdapter(adapter);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(context,
                R.array.historic_status_type, R.layout.spinner_item_historic_single);
        adapter1.setDropDownViewResource(R.layout.spinner_item_historic_dropdown);
        statusFilter.setAdapter(adapter1);
        leaveTypeFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateListDisplayed() ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }});
        statusFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateListDisplayed() ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }});
        return history ;
    }


    public void updateListDisplayed() {
        if (ticketList != null) {
            ticketListDisplayed.clear();
            if (statusFilter.getSelectedItemId() == 0 && leaveTypeFilter.getSelectedItemId() == 0) {
                ticketListDisplayed.addAll(ticketList);
                adapter.notifyDataSetChanged();
                return;
            }
            if (leaveTypeFilter.getSelectedItem().equals("Allocate")) {
                for (int i = 0; i < ticketList.size(); i++) {
                    if (ticketList.get(i).getStartDate() == null) {
                        ticketListDisplayed.add(ticketList.get(i));
                    }
                }
            }
            for (int i = 0; i < ticketList.size(); i++) {

                if ((ticketList.get(i).getStatus() == statusFilter.getSelectedItemPosition() || statusFilter.getSelectedItemId() == 0) &&
                        (ticketList.get(i).getReason().equals(leaveTypeFilter.getSelectedItem()) || leaveTypeFilter.getSelectedItemId() == 0)) {
                    ticketListDisplayed.add(ticketList.get(i));
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMainToFragmentCallbacks(User user) {
        LeaveRepository.getInstance(context).getLeaveHistory(this);
    }

    @Override
    public void onContactDatabaseSuccess(Object data) {
        if (data!= null && data instanceof List) {
            if(((List) data).size()>0 && ((List) data).get(0) instanceof Ticket) {
                ticketList = (List<Ticket>)data;
                Collections.reverse(ticketList);
                ticketListDisplayed.addAll(ticketList);
                if (firstUpdate) {
                    adapter = new HistoricFragmentAdapter(ticketListDisplayed, context);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayout.VERTICAL, false));
                    firstUpdate = false ;
                }else {
                    updateListDisplayed();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreateDatabaseError(String mess) {
        Log.e("Json error", mess);
    }
}
