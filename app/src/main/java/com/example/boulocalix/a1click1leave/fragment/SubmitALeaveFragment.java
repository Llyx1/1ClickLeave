package com.example.boulocalix.a1click1leave.fragment;

import android.app.Activity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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

import com.example.boulocalix.a1click1leave.model.User;
import com.example.boulocalix.a1click1leave.util.CalendarUtil;
import com.example.boulocalix.a1click1leave.util.DateRangeCalendarView;
import com.example.boulocalix.a1click1leave.util.KeyboardUtil;
import com.example.boulocalix.a1click1leave.util.SharePrefer;
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
//    RadioButton recap ;
//    RadioButton recapMinus ;
    EditText recap ;
    Button minusZeroFive ;
    Button plusZeroFive ;
    Dialog myDialog ;
    SharePrefer sharePrefer ;

    public SubmitALeaveFragment() {}


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
        sharePrefer = new SharePrefer(context) ;
    }

    @Override
    public void onResume() {
        super.onResume();
        main.setBottomNavigationViewSelectedItem(R.id.navigation_submit_a_leave);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final FrameLayout submitALeave = (FrameLayout) inflater.inflate(R.layout.fragment_submit_aleave, null);
        submitALeave.setOnClickListener(this);
        submitButton = submitALeave.findViewById(R.id.button_submit) ;
        spinner = submitALeave.findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(context,
                R.array.choice_array, R.layout.spinner_item_leave_request);
        spinner.setAdapter(adapter);
        recap = submitALeave.findViewById(R.id.default_time) ;
        KeyboardUtil.hideKeyboard(context);
        recap.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Double wantedNumber = Double.parseDouble(v.getText().toString()) ;
                if (wantedNumber >= numberOfDay) {
                    if (wantedNumber > numberOfDay){
                        recap.setText(Double.toString(numberOfDay));
                    }
                    reset();

                } else if (wantedNumber <= numberOfDay/2) {
                    if (wantedNumber < numberOfDay/2) {
                        recap.setText(Double.toString(numberOfDay / 2));
                    }
                    minusZeroFive.setBackground(context.getDrawable(R.mipmap.moins_vide));
                    plusZeroFive.setBackground(context.getDrawable(R.mipmap.plus_plein));
                }else {
                    minusZeroFive.setBackground(context.getDrawable(R.mipmap.moins_plein));
                    plusZeroFive.setBackground(context.getDrawable(R.mipmap.plus_plein));
                }
                return false;
            }
        });
        minusZeroFive = submitALeave.findViewById(R.id.minus_zero_five) ;
        minusZeroFive.setOnClickListener(this);
        plusZeroFive = submitALeave.findViewById(R.id.plus_zero_five) ;
        plusZeroFive.setOnClickListener(this);
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
                    reset();
                    KeyboardUtil.hideKeyboard(context);
                }
            }

            @Override
            public void onDateSelected(Calendar date) {
                if (date!=null) {

                    startLeaveDate = date.getTime();
                    endLeaveDate=date.getTime() ;
                    numberOfDay = 1 ;
                    recap.setText("1.0");
                    reset();
                    KeyboardUtil.hideKeyboard(context);
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

    public void reset() {
        minusZeroFive.setBackground(context.getDrawable(R.mipmap.moins_plein));
        plusZeroFive.setBackground(context.getDrawable(R.mipmap.plus_vide));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.minus_zero_five :
                if (Double.parseDouble(recap.getText().toString())>numberOfDay/2) {
                    recap.setText(Double.toString(Double.parseDouble(recap.getText().toString()) - 0.5));
                    if (Double.parseDouble(recap.getText().toString()) <= numberOfDay / 2) {
                        minusZeroFive.setBackground(context.getDrawable(R.mipmap.moins_vide));
                    }
                    plusZeroFive.setBackground(context.getDrawable(R.mipmap.plus_plein));
                }
                break ;
            case R.id.plus_zero_five:
                if(Double.parseDouble(recap.getText().toString())<numberOfDay) {
                    recap.setText(Double.toString(Double.parseDouble(recap.getText().toString()) + 0.5));
                    if (Double.parseDouble(recap.getText().toString()) >= numberOfDay) {
                        plusZeroFive.setBackground(context.getDrawable(R.mipmap.plus_vide));
                    }
                    minusZeroFive.setBackground(context.getDrawable(R.mipmap.moins_plein));
                }
                break ;
            case R.id.button_submit :
                if (checkDate()) {
                    if (Double.parseDouble(recap.getText().toString()) < sharePrefer.getBalance()
                            || spinner.getSelectedItemId() !=0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    ArrayList<String> info = new ArrayList<>();
                    info.add(Double.toString(Double.parseDouble(recap.getText().toString())));
                    info.add(sdf.format(startLeaveDate));
                    info.add(sdf.format(endLeaveDate));
                    info.add(Integer.toString(spinner.getSelectedItemPosition()));
                    main.onFragmentToMainCallbacks("SubmitALeaveFragment", info);}
                    else {
                        Toast.makeText(context, "you don't have enough paid leave for that request", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "It seems that you are really late to submit that leave request. Please contact HR to regularize your situation", Toast.LENGTH_LONG).show();
                }
                break;
            default :
                KeyboardUtil.hideKeyboard(context);
                break;
        }
    }


    @Override
    public void onMainToFragmentCallbacks(User user) {

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
