package com.example.boulocalix.a1click1leave.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.boulocalix.a1click1leave.R;
import com.example.boulocalix.a1click1leave.model.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoricFragmentAdapter extends RecyclerView.Adapter<HistoricFragmentAdapter.MyViewHolder>{



    private List<Ticket> ticketList;
    private Context mContext;



    private int visibleThreshold = 2;
    private int lastVisibleItem,totalItemCount;

    public HistoricFragmentAdapter(List<Ticket> ticketList, Context context){
        this.ticketList = ticketList ;
        mContext = context;
    }

    @Override
    public HistoricFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(mContext).inflate(R.layout.leave_historic_item, parent, false);
            return new  MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(HistoricFragmentAdapter.MyViewHolder mholder, int position) {

            MyViewHolder holder = (MyViewHolder)mholder;
            Ticket ticket = ticketList.get(position);
            if(ticket !=null) {
                holder.reasonTv.setText(ticket.getReason());
                holder.startTv.setText(ticket.getStartDate());
                holder.endTv.setText(ticket.getEndDate());
                holder.numberOfDayTv.setText(ticket.getNumberOfDay());
            }


    }

    private void handleOnClickItem(Ticket ticket) {
        Bundle bundle = new Bundle();

//TODO faire un truc
    }

    private Bundle putDataIntoBundle(Bundle bundle, Ticket ticket) {

        return bundle;
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.history_reason)
        TextView reasonTv;
        @BindView(R.id.history_start)
        TextView startTv;
        @BindView(R.id.history_end)
        TextView endTv;
        @BindView(R.id.history_number_of_day)
        TextView numberOfDayTv;
        @BindView(R.id.leave_ticket)
        LinearLayout itemLayout ;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView) ;
        }
    }
}