package com.gymlazy.tripplanner;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TripPlannerFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener {
    private Button mStartDateBtn;
    private Button mEndDateBtn;
    private Date mDate;
    private static final String TAG = "TripPlannerFragment";
    private static final String DATE_PICKER_TAG = "DateTripPicker";



    // create new instance
    public static Fragment newInstance() {
        return new TripPlannerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.trip_planner_fragment,container,false);
        mDate = Calendar.getInstance().getTime();

        mStartDateBtn = v.findViewById(R.id.trip_start_date);
        mStartDateBtn.setText(getCurrentDate());
        mStartDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get fragment manager
                FragmentManager fm = getFragmentManager();
                // create DatePickerFragment with the given Date
                DatePickerFragment df = DatePickerFragment.newInstance(mDate);
                // Set target fragment
                df.setTargetFragment(TripPlannerFragment.this, DatePickerFragment.PICK_START_DATE_REQUEST);
                df.show(fm,DatePickerFragment.PICK_START_DATE);

            }
        });
        mEndDateBtn = v.findViewById(R.id.trip_end_date);
        mEndDateBtn.setText(getCurrentDate());

        return v;
    }

    // get current date with specified format
    private static String getCurrentDate() {
        // get current date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("E MMM d yyyy", Locale.getDefault());
        return df.format(c);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // check whether the activity operation is succeed
        if(requestCode != DatePickerFragment.PICK_START_DATE_REQUEST)
        {
            return;
        }

        // check for the result code
        if(resultCode == DatePickerFragment.RESULT_PICK_START_DATE)
        {

            String sChosenDate = data.getStringExtra(DatePickerFragment.PICK_START_DATE);
            Log.i(TAG, "Date picked: " + sChosenDate);
            mStartDateBtn.setText(sChosenDate);
        }


  }

    // handler when the date was chosen from date picker fragment
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat df = new SimpleDateFormat("E MMM d yyyy", Locale.getDefault());
        String sChosenDate = df.format(c);


    }
}
