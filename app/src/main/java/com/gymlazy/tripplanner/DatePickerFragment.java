package com.gymlazy.tripplanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


// how to create dialog
// communicate which fragment to be sent
// update approriate fragment

// create date picker fragment
// Credit: codinginflow
public class DatePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "ARG_DATE";
    private static final String TAG = "DatePickerFragment";
    public static final String PICK_START_DATE = "PickStartDate";
    public static final String PICK_END_DATE = "PickEndDate";
    public static final int PICK_START_DATE_REQUEST = 1;
    public static final int PICK_END_DATE_REQUEST = 2;
    public static final int RESULT_PICK_START_DATE = 10;
    public static final int RESULT_PICK_END_DATE = 11;

    private DatePicker mDatePicker;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get current date to display
        Date dCurrentDate = (Date) getArguments().getSerializable(ARG_DATE);

        // use the date to configure the calendar object to configure the date picker correctly
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // from current context, inflate the date picker dialog xml
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.date_picker_dialog,null);

        // from the date picker in the view, init with current date
        mDatePicker = (DatePicker) v.findViewById(R.id.date_picker);
        mDatePicker.init(year,month,day,null);

        // create builder
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Trip Date Picker:")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // configure calendar with the chosen date
                        Calendar c = Calendar.getInstance();
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, day);

                        // create format date
                        SimpleDateFormat df = new SimpleDateFormat("E MMM d yyyy", Locale.getDefault());
                        Date dDate = c.getTime();
                        String sChosenDate = df.format(dDate);
                        Log.i(TAG,"Date picked: " + sChosenDate);

                        // check whether it is picking the start date or end date
                        if(DatePickerFragment.this.getTargetRequestCode() == PICK_START_DATE_REQUEST)
                        {
                            sendResult(Activity.RESULT_OK,sChosenDate);
                        }
                        else if(DatePickerFragment.this.getTargetRequestCode() == PICK_END_DATE_REQUEST)
                        {
                            sendResult(Activity.RESULT_OK,sChosenDate);
                        }

                    }
                })
                .create();

    }

    /*
     *	Function: void sendResult()
     *	Description:
     *       The purpose of this function is to send result back to the target fragment with appropriate request code
     *	Parameter:  int resultCode : the result code
     *              String sDate: The given date
     *	Return: Fragment: return the DatePickerFragment with the date given
     */
    private void sendResult(int resultCode, String sDate)
    {
        // check whether the target fragment is null
        if(getTargetFragment() == null)
        {
            return;
        }

        // create intent with the chosen date
        Intent i = new Intent();

        // check whether it is picking the start date or end date
        if(DatePickerFragment.this.getTargetRequestCode() == PICK_START_DATE_REQUEST)
        {
            i.putExtra(PICK_START_DATE,sDate);

        }
        else if(DatePickerFragment.this.getTargetRequestCode() == PICK_END_DATE_REQUEST)
        {
            i.putExtra(PICK_END_DATE,sDate);
        }

        // send back date to the target fragment
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);

    }


    /*
     *	Function: newInstance(Date date)
     *	Description:
     *       The purpose of this function is to create an instance of the DatePickerFragment class with a date given
     *	Parameter: Date date: The given date
     *	Return: Fragment: return the DatePickerFragment with the date given
     */
    // create new instance of the DatePickerFragment with
    public static DatePickerFragment newInstance(Date date)
    {
        // create bundle and put the given in the arguments
        Bundle b = new Bundle();
        b.putSerializable(ARG_DATE, date);

        // create DatePickerFragment
        DatePickerFragment df = new DatePickerFragment();
        df.setArguments(b);

        return df;


    }
}
