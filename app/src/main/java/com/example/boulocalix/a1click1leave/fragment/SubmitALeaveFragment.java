package com.example.boulocalix.a1click1leave.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boulocalix.a1click1leave.MainActivity;
import com.example.boulocalix.a1click1leave.R;
import com.example.boulocalix.a1click1leave.callbacks.onMainToFragmentCallbacks;
import com.example.boulocalix.a1click1leave.model.Employee;

import com.example.boulocalix.a1click1leave.util.CalendarUtil;
import com.example.boulocalix.a1click1leave.util.DateRangeCalendarView;
import com.google.android.gms.plus.PlusShare;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;

public class SubmitALeaveFragment extends Fragment implements onMainToFragmentCallbacks, View.OnClickListener {

    MainActivity main ;
    Context context = null ;
    DateRangeCalendarView calendarRangePicker ;
    Button submitButton ;
    Spinner spinner ;
    double numberOfDay ;
    double numberOfDayTotal ;
    Date startLeaveDate ;
    Date endLeaveDate ;
    RadioButton recap ;
    RadioButton recapMinus ;
    Dialog myDialog ;

    public SubmitALeaveFragment() {}

    public interface CalendarUtil {
        void onDateRangeSelected(Calendar startDate, Calendar endDate);
        void onDateSelected(Calendar date) ;
        void onCancel();
    }

    public static SubmitALeaveFragment newInstance() {
        SubmitALeaveFragment fragment = new SubmitALeaveFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final FrameLayout submitALeave = (FrameLayout) inflater.inflate(R.layout.fragment_submit_aleave, null);
        submitButton = submitALeave.findViewById(R.id.button_submit) ;
        spinner = submitALeave.findViewById(R.id.spinner);
        recap = submitALeave.findViewById(R.id.default_time) ;
        recap.setOnClickListener(this);
//        recapPlus = submitALeave.findViewById(R.id.plus_zero_five) ;
//        recapPlus.setOnClickListener(this);
        recapMinus = submitALeave.findViewById(R.id.minus_zero_five) ;
        recapMinus.setOnClickListener(this);
        myDialog = new Dialog(context) ;
        calendarRangePicker = submitALeave.findViewById(R.id.calendar) ;
        calendarRangePicker.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                if (startDate != null && endDate != null) {
                    startLeaveDate=startDate.getTime();
                    endLeaveDate=endDate.getTime() ;
                    numberOfDay = calculateDayOff() ;
                    recap.setText(Double.toString(numberOfDay));
                    recapMinus.setText(Double.toString(numberOfDay - 0.5));
                    recap.setTypeface(null, Typeface.BOLD);
                    recapMinus.setTypeface(Typeface.DEFAULT);
                }
            }

            @Override
            public void onDateSelected(Calendar date) {
                if (date!=null) {

                    startLeaveDate = date.getTime();
                    endLeaveDate=date.getTime() ;
                    numberOfDay = 1 ;
                    recap.setText("1.0");
                    recapMinus.setText("0.5");
//                    recapPlus.setText("1.5");
                    recap.setTypeface(null, Typeface.BOLD);
                    recapMinus.setTypeface(Typeface.DEFAULT);
//                    recapPlus.setTypeface(Typeface.DEFAULT);
                }
            }



            @Override
            public void onCancel() {
                startLeaveDate = endLeaveDate = null ;
            }
        });
        submitButton.setOnClickListener(this) ;
        return submitALeave ;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.minus_zero_five :
                numberOfDay= Double.parseDouble(recapMinus.getText().toString()) ;
                recapMinus.setTypeface(null, Typeface.BOLD);
                recap.setTypeface(Typeface.DEFAULT);
                recapMinus.setTextColor(context.getColor(R.color.dark_grey)) ;
                recap.setTextColor(context.getColor(R.color.grey));
                break ;
            case R.id.default_time :
                numberOfDay= Double.parseDouble(recap.getText().toString()) ;
                recap.setTypeface(null, Typeface.BOLD);
                recapMinus.setTypeface(Typeface.DEFAULT);
                recap.setTextColor(context.getColor(R.color.dark_grey)) ;
                recapMinus.setTextColor(context.getColor(R.color.grey));
                break ;
            case R.id.button_submit :
                if (checkDate()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    ArrayList<String> info = new ArrayList<>();
                    info.add(Double.toString(numberOfDay));
                    info.add(sdf.format(startLeaveDate));
                    info.add(sdf.format(endLeaveDate));
                    info.add(Integer.toString(spinner.getSelectedItemPosition()));
                    main.onFragmentToMainCallbacks("SubmitALeaveFragment", info);
                } else {
                    Toast.makeText(context, "It seems that you are really late to submit that leave request. Please contact HR to regularize your situation", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onMainToFragmentCallbacks(Employee employee) {

    }

    public double calculateDayOff() {
        if (  startLeaveDate !=null && endLeaveDate != null){
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(startLeaveDate);

                Calendar endCal = Calendar.getInstance();
                endCal.setTime(endLeaveDate);

                int workDays = 0;


                    if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
                        startCal.setTime(endLeaveDate);
                        endCal.setTime(startLeaveDate);
                    }
                    while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
                        if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                            ++workDays;
                        }
                        startCal.add(Calendar.DAY_OF_MONTH, 1);
                    }

                return numberOfDayTotal = workDays;
        }
        else
        {
            recap.setText("");
            return -1 ;
        }
    }

    public boolean checkDate() {
        Calendar curentDay = Calendar.getInstance(TimeZone.getDefault()) ;
        curentDay.add(Calendar.DAY_OF_YEAR, -7);
        Calendar dayStart = Calendar.getInstance() ;
        dayStart.setTime(startLeaveDate);
        if (dayStart.after(curentDay)){
            return true ;
        }
        return false ;
    }
}
