package com.example.boulocalix.a1click1leave.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boulocalix.a1click1leave.MainActivity;
import com.example.boulocalix.a1click1leave.R;
import com.example.boulocalix.a1click1leave.fragmentInterface.onMainToFragmentCallbacks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SubmitALeaveFragment extends Fragment implements onMainToFragmentCallbacks, View.OnClickListener{

    MainActivity main ;
    Context context = null ;
    RadioGroup radioGroup ;
    LinearLayout oneDayLeaveDateChoice ;
    LinearLayout multiDayLeaveDateChoice ;
    Calendar myCalendar ;
    LinearLayout startLeave ;
    LinearLayout endLeave ;
    LinearLayout dateLeave ;
    LinearLayout currentLayout ;
    Button submitButton ;
    Spinner spinner ;
    double numberOfDay ;
    double numberOfDayTotal ;
    Date startLeaveDate ;
    Date endLeaveDate ;
    TextView recap ;
    LinearLayout adapting ;
    CheckBox plus ;
    CheckBox minus ;

    public SubmitALeaveFragment() {}

    public static SubmitALeaveFragment newInstance() {
        SubmitALeaveFragment fragment = new SubmitALeaveFragment();
        Bundle args = new Bundle();
        //args.putInt("Remaining days", days);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            remainingDay = getArguments().getInt("Remaining days");
//        }
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
        radioGroup = (RadioGroup) submitALeave.findViewById(R.id.toggle);
        radioGroup.check(R.id.one_day);
        oneDayLeaveDateChoice = submitALeave.findViewById(R.id.pickup_date_single) ;
        multiDayLeaveDateChoice = submitALeave.findViewById(R.id.pickup_date_multi) ;
        submitButton = submitALeave.findViewById(R.id.button_submit) ;
        spinner = submitALeave.findViewById(R.id.spinner);
        myCalendar = Calendar.getInstance();
        startLeave=  submitALeave.findViewById(R.id.leaving_date);
        endLeave = submitALeave.findViewById(R.id.return_date) ;
        dateLeave = submitALeave.findViewById(R.id.leaving_date_single);
        recap = submitALeave.findViewById(R.id.tv_recap) ;
        adapting = submitALeave.findViewById(R.id.adapting) ;
        plus = submitALeave.findViewById(R.id.plus05) ;
        plus.setOnClickListener(this);
        minus = submitALeave.findViewById(R.id.moins05) ;
        minus.setOnClickListener(this);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            private void updateLabel(LinearLayout layout) {
                String day = "dd "; //In which you need put here
                String month = "MMMM";
                String year = "yyyy";
                TextView tvDay;
                TextView tvMonth;
                TextView tvYear;
                SimpleDateFormat sdfday = new SimpleDateFormat(day, Locale.US);
                SimpleDateFormat sdfmonth = new SimpleDateFormat(month, Locale.US);
                SimpleDateFormat sdfyear = new SimpleDateFormat(year, Locale.US);
                if (layout == startLeave) {
                    tvDay = layout.findViewById(R.id.day_leave);
                    tvMonth = layout.findViewById(R.id.month_leave);
                    tvYear = layout.findViewById(R.id.year_leave);
                    startLeaveDate = myCalendar.getTime() ;
                    calculateDayOff() ;
                } else if (layout == endLeave) {
                    tvDay = layout.findViewById(R.id.day_return);
                    tvMonth = layout.findViewById(R.id.month_return);
                    tvYear = layout.findViewById(R.id.year_return);
                    endLeaveDate = myCalendar.getTime() ;
                    calculateDayOff() ;
                } else  {
                    tvDay = layout.findViewById(R.id.day_one);
                    tvMonth = layout.findViewById(R.id.month_one);
                    tvYear = layout.findViewById(R.id.year_one);
                    startLeaveDate = myCalendar.getTime() ;
                    endLeaveDate = myCalendar.getTime() ;
                    calculateDayOff() ;
                }
                tvDay.setText(sdfday.format(myCalendar.getTime()));
                tvMonth.setText(sdfmonth.format(myCalendar.getTime()));
                tvYear.setText(sdfyear.format(myCalendar.getTime()));
            }

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(currentLayout);
            }
        };

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                adapting.setVisibility(View.GONE);
                endLeaveDate =startLeaveDate = null ;
                if (checkedId==R.id.one_day) {
                    oneDayLeaveDateChoice.setVisibility(View.VISIBLE);
                    multiDayLeaveDateChoice.setVisibility(View.INVISIBLE);
                }
                else {
                    oneDayLeaveDateChoice.setVisibility(View.INVISIBLE);
                    multiDayLeaveDateChoice.setVisibility(View.VISIBLE);
                }
            }
        });

        startLeave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                currentLayout = startLeave ;
            }

        });
        endLeave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                currentLayout = endLeave ;
            }

        });
        oneDayLeaveDateChoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                currentLayout = dateLeave ;
            }

        });

        submitButton.setOnClickListener(this) ;
        return submitALeave ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.plus05 :
                if(plus.isChecked()){
                    numberOfDayTotal = numberOfDayTotal + 0.5 ;
                }
                else {
                    numberOfDayTotal = numberOfDayTotal - 0.5 ;
                }
                recap.setText(getResources().getString(R.string.recap, Double.toString(numberOfDayTotal)));
                break ;
            case R.id.moins05 :
                if (minus.isChecked()) {
                    numberOfDayTotal = numberOfDayTotal - 0.5 ;
                }
                else {
                    numberOfDayTotal = numberOfDayTotal + 0.5 ;
                }
                recap.setText(getResources().getString(R.string.recap, Double.toString(numberOfDayTotal)));
                break ;
            case R.id.button_submit :
                if (checkDate()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                    ArrayList<String> info = new ArrayList<>();
                    numberOfDay = calculateDayOffToDelete();
                    info.add(Double.toString(numberOfDay));
                    info.add(sdf.format(startLeaveDate));
                    info.add(sdf.format(endLeaveDate));
                    info.add(Integer.toString(spinner.getSelectedItemPosition()));
                    main.onFragmentToMainCallbacks("SubmitALeaveFragment", info);
                }
                break ;
        }
    }

    @Override
    public void onMainToFragmentCallbacks(ArrayList<String> info) {

    }

    public double calculateDayOff() {
        if (  startLeaveDate !=null && endLeaveDate != null){
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(startLeaveDate);

                Calendar endCal = Calendar.getInstance();
                endCal.setTime(endLeaveDate);

                int workDays = 0;

                if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
                    recap.setText(getResources().getString(R.string.recap, "1"));
                    workDays=1 ;
                }else {
                    if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
                        startCal.setTime(endLeaveDate);
                        endCal.setTime(startLeaveDate);
                    }
                    while (startCal.getTimeInMillis() < endCal.getTimeInMillis()) {
                        if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                            ++workDays;
                        }
                        startCal.add(Calendar.DAY_OF_MONTH, 1);
                    }
                }
                recap.setText(getResources().getString(R.string.recap, Float.toString(workDays)));
                adapting.setVisibility(View.VISIBLE);
                return numberOfDayTotal = workDays;
        }
        else
        {
            recap.setText("");
            return -1 ;
        }
    }
    public double calculateDayOffToDelete() { //this function will need some work with the HR to know how they are currently counting the days off.
      double daySum = numberOfDayTotal ;

      String textSpinner = spinner.getSelectedItem().toString() ;
//        Annual leave
//        Wedding
//        Children's wedding
        if (textSpinner.equals("Unpaid leave") || textSpinner.equals("Work at home") || textSpinner.equals("National day-off") ){
            daySum =0 ;
        }
        //TODO Add the automatic changement from paid leave to unpaid. Ask HR to see their current interface to provide them something similar.
//        Sick leave
//        Paternity
//        Maternity
//        Funeral of a close relative
//        Funeral of a relative
//        Work at home

        return daySum ;
    }

    public boolean checkDate() {
        //TODO modify so late date are accepted depending on the reason
        if (startLeaveDate == null || endLeaveDate == null ) {
            Toast.makeText(context, "Please enter a date before to submit", Toast.LENGTH_LONG).show(); ;
            return false ;
        } else if (startLeaveDate.compareTo(endLeaveDate) == 0) {
            Calendar calendar = Calendar.getInstance() ;
            calendar.setTime(endLeaveDate) ;
            calendar.add(Calendar.DATE, 1) ;
            endLeaveDate=  calendar.getTime() ;
        }
        Calendar calendarLocale = Calendar.getInstance(Locale.KOREA) ;
        calendarLocale.add(Calendar.DATE, -1) ;
        if ((!startLeaveDate.before(endLeaveDate)) || !startLeaveDate.after(calendarLocale.getTime())) {
            Toast.makeText(context, "Please enter appropriate date", Toast.LENGTH_LONG).show();
            return false ;
        }
        return true ;
    }
}
