package com.example.boulocalix.a1click1leave.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.boulocalix.a1click1leave.MainActivity;
import com.example.boulocalix.a1click1leave.R;
import com.example.boulocalix.a1click1leave.callbacks.BackupFind;
import com.example.boulocalix.a1click1leave.fragment.ChooseBackup;
import com.example.boulocalix.a1click1leave.fragment.ProfileFragment;
import com.example.boulocalix.a1click1leave.model.Employee;
import com.example.boulocalix.a1click1leave.model.User;
import com.example.boulocalix.a1click1leave.repository.LeaveRepository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BackupResearchAdapter extends RecyclerView.Adapter<BackupResearchAdapter.MyViewHolder> {

    private List<User> offyList;
    private Context mContext;


    public BackupResearchAdapter(List<User> offyList, Context context){
        this.offyList = offyList ;
        mContext = context;
    }

    @Override
    public BackupResearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.backup_item, parent, false);
        return new BackupResearchAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BackupResearchAdapter.MyViewHolder mholder, int position) {

        final BackupResearchAdapter.MyViewHolder holder = (BackupResearchAdapter.MyViewHolder)mholder;
        final User employee = offyList.get(position);
        if(employee !=null) {
            holder.nameTv.setText(employee.getFullName());
            holder.emailTv.setText(employee.getEmail());
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOnClickItem(employee);
                }
            });
        }
    }

    private void handleOnClickItem(User employee) {
        if (employee != null) {
            ((MainActivity) mContext).popPreviousFragmentWithoutReload();
            ((MainActivity) mContext).setNewBackup(employee);
        }
    }

    @Override
    public int getItemCount() {
        return offyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_backup)
        TextView nameTv;
        @BindView(R.id.email_backup)
        TextView emailTv;
        @BindView(R.id.backup_item)
        LinearLayout itemLayout ;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView) ;
        }
    }
}
