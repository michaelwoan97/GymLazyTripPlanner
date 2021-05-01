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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TripPlannerFragment extends Fragment {
    private Button mStartDateBtn;
    private Button mEndDateBtn;
    private Date mDate;
    private Date mStartDate;
    private Date mEndDate;
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
        mStartDate = mEndDate = mDate;

        mStartDateBtn = v.findViewById(R.id.trip_start_date);
        mStartDateBtn.setText(getCurrentDate());
        mStartDateBtn.setOnClickListener(new View.OnClickListener() { // event handler for start pick date
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
        mEndDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                // create dialog instance
                DatePickerFragment df = DatePickerFragment.newInstance(mDate);

                df.setTargetFragment(TripPlannerFragment.this, DatePickerFragment.PICK_END_DATE_REQUEST);// set target fragment
                df.show(fm,DatePickerFragment.PICK_END_DATE);
            }
        });

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
        if(resultCode != Activity.RESULT_OK)
        {
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM d yyyy", Locale.getDefault());

        // check for the result code
        if(requestCode == DatePickerFragment.PICK_START_DATE_REQUEST)
        {

            try {
                String sChosenDate = data.getStringExtra(DatePickerFragment.PICK_START_DATE);
                Date dChosenDate = simpleDateFormat.parse(sChosenDate);

                // check the whether the beginning date of the trip is valid
                if(checkDate(DatePickerFragment.PICK_START_DATE_REQUEST, mEndDate, dChosenDate) == true)
                {
                    mStartDateBtn.setText(sChosenDate);
                }
                else
                {
                    Toast.makeText(TripPlannerFragment.this.getActivity(), R.string.invalid_date_label, Toast.LENGTH_LONG).show();
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else if(requestCode == DatePickerFragment.PICK_END_DATE_REQUEST)
        {
            try {
                String sChosenDate = data.getStringExtra(DatePickerFragment.PICK_END_DATE);
                Date dChosenDate = simpleDateFormat.parse(sChosenDate);

                // check the whether the beginning date of the trip is valid
                if(checkDate(DatePickerFragment.PICK_END_DATE_REQUEST, mStartDate, dChosenDate) == true)
                {
                    mEndDateBtn.setText(sChosenDate);
                }
                else
                {
                    Toast.makeText(TripPlannerFragment.this.getActivity(), R.string.invalid_date_label, Toast.LENGTH_LONG).show();
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    /*
     *	Function: boolean checkDate(int requestCode, Date dChosenDate)
     *	Description:
     *       The purpose of this function is to check whether the beginning date of the trip or
     *           the end date of the trip is valid depends on the request code
     *	Parameter: int requestCode : request code of the chosen date
     *             Date dDateToCompare : date to be compared
     *             Date dChosenDate : the chosen date
     *	Return: boolean: return true of the date is valid. Otherwise, false
     */
    public static boolean checkDate(int requestCode, Date dDateToCompare, Date dChosenDate) throws ParseException {
        boolean bRetVal = false;
//        long lDateToCompare = dDateToCompare.getTime();
//        long lChosenDate = dChosenDate.getTime(); // convert to Date objects

        // check for request codes
        switch (requestCode)
        {
            case DatePickerFragment.PICK_START_DATE_REQUEST:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM d yyyy", Locale.getDefault());
                Date dCurrentDate = simpleDateFormat.parse(getCurrentDate());

                /* Check whether the beginning date is valid compare against current date */
                if(dChosenDate.compareTo(dCurrentDate) < 0)
                {
                    return bRetVal;
                }
                bRetVal = true;
                break;
            case DatePickerFragment.PICK_END_DATE_REQUEST:
                /* Check whether the end date is valid compare against the begin date (which dDateToCompare) */
                if(dChosenDate.compareTo(dDateToCompare) < 0)
                {
                    return bRetVal;
                }
                bRetVal = true;
                break;
            default:
                return bRetVal;
        }

        return bRetVal;

    }




}
