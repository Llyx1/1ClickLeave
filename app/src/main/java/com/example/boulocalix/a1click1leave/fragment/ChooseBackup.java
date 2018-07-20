package com.example.boulocalix.a1click1leave.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.boulocalix.a1click1leave.MainActivity;
import com.example.boulocalix.a1click1leave.R;
import com.example.boulocalix.a1click1leave.adapter.BackupResearchAdapter;
import com.example.boulocalix.a1click1leave.callbacks.BackupFind;
import com.example.boulocalix.a1click1leave.callbacks.LeaveAPICallbacks;
import com.example.boulocalix.a1click1leave.model.Backup;
import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.model.User;
import com.example.boulocalix.a1click1leave.repository.LeaveRepository;
import com.example.boulocalix.a1click1leave.util.ApiClient;
import com.example.boulocalix.a1click1leave.util.ApiInterface;
import com.example.boulocalix.a1click1leave.util.SharePrefer;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ChooseBackup extends Fragment implements LeaveAPICallbacks{

    MainActivity main ;
    Context context ;
    SharePrefer sharePrefer ;
    static ArrayList<User> offyUsers =new ArrayList<>() ;
    ArrayList<User> correspondingUsers = new ArrayList<>() ;
    EditText researchBackup ;
    BackupResearchAdapter adapter;
    RecyclerView recyclerView ;


    public ChooseBackup() {
    }

    public static ChooseBackup newInstance(List<User> backups) {
        ChooseBackup fragment = new ChooseBackup();
        Bundle args = new Bundle();
        if (backups.size() >0 ){
            for (int i = 0 ; i< backups.size() ; i++){
               args.putLong(Integer.toString( backups.get(i).id), backups.get(i).id);
            }
        }
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            main = (MainActivity) getActivity() ;
            context = getActivity() ;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
        sharePrefer = new SharePrefer(context) ;
        final Bundle bundle = this.getArguments() ;
        LeaveRepository.getInstance(context).getUsers(this);
        final int userId = sharePrefer.getId() ;
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<List<User>> call= apiService.getAllEmployees(sharePrefer.getAccessToken());
        call.enqueue(new retrofit2.Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                offyUsers =(ArrayList<User>) response.body() ;
                for (int i = 0; i <offyUsers.size(); i++) {
                    if (bundle.get(Integer.toString(offyUsers.get(i).getId()))!=null || offyUsers.get(i).getId() == userId) {
                        offyUsers.remove(i) ;
                        i-- ;
                    }
                }
                correspondingUsers.addAll(offyUsers) ;
                adapter = new BackupResearchAdapter(correspondingUsers, context);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayout.VERTICAL, false));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Log error here since request failed
                Log.e("Json error", t.toString());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final LinearLayout backupLL = (LinearLayout) inflater.inflate(R.layout.fragment_research_backup, container, false);
        recyclerView = backupLL.findViewById(R.id.backup_recycler) ;
        researchBackup = backupLL.findViewById(R.id.name_backup_research) ;
        researchBackup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                    correspondingUsers.clear() ;
                    for (int i = 0 ; i<offyUsers.size(); i++) {
                        if (offyUsers.get(i).getFullName().toLowerCase().contains(s.toString().toLowerCase())) {
                            correspondingUsers.add(offyUsers.get(i)) ;

                        }
                    }
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return  backupLL ;
    }

    @Override
    public void onCreateDatabaseError(String mess) {

    }

    @Override
    public void onContactDatabaseSuccess(Object data) {
        if (data instanceof List) {
            if (((List) data).size()>0 && ((List) data).get(0) instanceof Backup) {
                for (int i= 0 ; i<((List) data).size(); i++) {
                    
                }
            } else {
                LeaveRepository.getInstance(context).getBackup(this);
            }
        }
    }
}
