package com.example.boulocalix.a1click1leave.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.boulocalix.a1click1leave.R;
import com.example.boulocalix.a1click1leave.model.Ticket;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoricFragmentAdapter extends RecyclerView.Adapter<HistoricFragmentAdapter.MyViewHolder>{



    private List<Ticket> ticketList;
    private Context mContext;
    private String[] status_table = {"Pending","Cancelled", "Approved", "Bring certificate to HR"} ;



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

            final MyViewHolder holder = (MyViewHolder)mholder;
            final Ticket ticket = ticketList.get(position);
            if(ticket !=null) {
                holder.reasonTv.setText(ticket.getReason());
                holder.startTv.setText(ticket.getStartDate());
                holder.endTv.setText(ticket.getEndDate());
                holder.numberOfDayTv.setText(Double.toString(ticket.getNumberOfDay()));
                holder.status.setText(status_table[ticket.getStatus()-1]) ;
                holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleOnClickItem(ticket, holder);
                    }
                });
            }



    }

    private void handleOnClickItem(Ticket ticket, MyViewHolder holder) {
        String endDate = ticket.getEndDate() ;
        int day = Integer.parseInt(endDate.substring(0,2)) ;
        int month = Integer.parseInt(endDate.substring(3,5)) ;
        int year = Integer.parseInt(endDate.substring(6,8)) ;
        Calendar calendar = new GregorianCalendar(2000+year,month-1,day);
        Calendar currentDate = GregorianCalendar.getInstance(TimeZone.getDefault());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        if (currentDate.get(Calendar.YEAR)<calendar.get(Calendar.YEAR)) {
            showCancelDialog(holder);
        }
        if (calendar.get(Calendar.YEAR)== currentDate.get(Calendar.YEAR)&& currentDate.get(Calendar.DAY_OF_YEAR)<= calendar.get(Calendar.DAY_OF_YEAR)){
            showCancelDialog(holder);
        }

    }

    private void showCancelDialog(MyViewHolder holder) {
        final MyViewHolder holderF = holder ;
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.pop_cancel_leave);
        Button dialogCancelButton = dialog.findViewById(R.id.cancel_leave);
        Button dialogOKButton = dialog.findViewById(R.id.confirm_leave) ;
        // if ok button is clicked, close the custom dialog
        dialogOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holderF.status.setText("Cancelled");
                //TODO send cancel reauest to API
                dialog.dismiss();
            }
        });
        // if close button is clicked, close the custom dialog
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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
        @BindView(R.id.status)
        TextView status ;
        @BindView(R.id.leave_ticket)
        LinearLayout itemLayout ;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView) ;
        }
    }
}