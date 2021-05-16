package com.gymlazy.tripplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputLayout;
import com.gymlazy.tripplanner.Controller.DatePickerFragment;
import com.gymlazy.tripplanner.Controller.HotelListActivity;
import com.gymlazy.tripplanner.Controller.HotelPagerActivity;
import com.gymlazy.tripplanner.Controller.HotelTicketActivity;
import com.gymlazy.tripplanner.Controller.HotelTicketFragment;
import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.HotelList;
import com.gymlazy.tripplanner.Model.Trip;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TripPlannerFragment extends Fragment {
    private EditText mStartDateBtn;
    private TextInputLayout mStartDateLayout;
    private EditText mEndDateBtn;
    private TextInputLayout mEndDateLayout;
    private EditText mNumAdult;
    private TextInputLayout mNumAdultLayout;
    private EditText mNumChild;
    private TextInputLayout mNumChildLayout;
    private EditText mDestination;
    private TextInputLayout mDestinationLayout;
    private Spinner mLanguages;
    private RadioGroup mHasCovid;
    private Button mSearchBtn;
    private Date mDate;
    private Date mStartDate;
    private Date mEndDate;
    private SimpleDateFormat mSimpleDateFormat;
    private ArrayList<String> mStringArrayListLanguages;
    private static final String TAG = "TripPlannerFragment";
    private static final String DATE_PICKER_TAG = "DateTripPicker";
    public static final int MAX_TRIPPER = 1000;
    public static boolean mCanGoNextState;


    // create new instance
    public static Fragment newInstance() {
        return new TripPlannerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCanGoNextState = false;
        mSimpleDateFormat = new SimpleDateFormat("E MMM d yyyy", Locale.getDefault());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.trip_planner_fragment,container,false);

        mDestination = v.findViewById(R.id.trip_destination);
        mDestinationLayout = v.findViewById(R.id.trip_destination_input);
        mNumAdult = v.findViewById(R.id.num_adult);
        mNumAdultLayout = v.findViewById(R.id.trip_num_adult_input);
        mNumChild = v.findViewById(R.id.num_child);
        mNumChildLayout = v.findViewById(R.id.trip_num_child_input);
        mStartDateLayout = v.findViewById(R.id.trip_start_date_input);
        mStartDateBtn = v.findViewById(R.id.trip_start_date);
        mEndDateBtn = v.findViewById(R.id.trip_end_date);
        mEndDateLayout = v.findViewById(R.id.trip_end_date_input);

        // check whether the begin date and/or end date is picked
        if(savedInstanceState != null)
        {
            if(!savedInstanceState.getString(DatePickerFragment.PICK_START_DATE).isEmpty() &&
                    !savedInstanceState.getString(DatePickerFragment.PICK_END_DATE).isEmpty() )
            {
                try {
                    mStartDate = mSimpleDateFormat.parse(savedInstanceState.getString(DatePickerFragment.PICK_START_DATE));
                    mEndDate = mSimpleDateFormat.parse(savedInstanceState.getString(DatePickerFragment.PICK_END_DATE));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                mDate = Calendar.getInstance().getTime();
                mStartDate = mEndDate = mDate;
            }
        }
        else
        {
            mDate = Calendar.getInstance().getTime();
            mStartDate = mEndDate = mDate;
        }


        mStartDateLayout.setEndIconOnClickListener(new View.OnClickListener() { // event handler for start pick date
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

        // when start date edit text is on focus, the date picker icon is visible
        mStartDateBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mStartDateLayout.setEndIconVisible(true);
                    mStartDateLayout.setErrorIconDrawable(null);
                }
            }
        });

        // when the error appear, if the input is changed clear the error
        mStartDateBtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStartDateLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // when the error appear, if the input is changed clear the error
        mStartDateBtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStartDateLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // when the error appear, if the input is changed clear the error
        mDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDestinationLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // when the error appear, if the input is changed clear the error
        mNumAdult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNumAdultLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // when the error appear, if the input is changed clear the error
        mNumChild.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNumChildLayout.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEndDateLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                // create dialog instance
                DatePickerFragment df = DatePickerFragment.newInstance(mDate);

                df.setTargetFragment(TripPlannerFragment.this, DatePickerFragment.PICK_END_DATE_REQUEST);// set target fragment
                df.show(fm,DatePickerFragment.PICK_END_DATE);
            }
        });

        // when start date edit text is on focus, the date picker icon is visible
        mEndDateBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mEndDateLayout.setEndIconVisible(true);
                    mEndDateLayout.setErrorIconDrawable(null);
                }
            }
        });

        // when the error appear, if the input is changed clear the error
        mEndDateBtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEndDateLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mNumAdult = v.findViewById(R.id.num_adult);
        mNumAdult.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    hideKeyboard(v);
                }

            }
        });

        mNumChild = v.findViewById(R.id.num_child);
        mNumChild.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    hideKeyboard(v);
                }
            }
        });

        mDestination = (EditText) v.findViewById(R.id.trip_destination);
        mDestination.setFocusable(true);
        mDestination.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    hideKeyboard(v);
                }
            }
        });

        // set up adapter for spinner
        mLanguages = (Spinner) v.findViewById(R.id.language);
        mStringArrayListLanguages = new ArrayList<>();
        mStringArrayListLanguages.add("English");
        mStringArrayListLanguages.add("Spanish");
        mStringArrayListLanguages.add("Vietnamese");
        ArrayAdapter<String> mLanguageAdapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_spinner_item,
                mStringArrayListLanguages);
        mLanguageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLanguages.setAdapter(mLanguageAdapter);

        mHasCovid = v.findViewById(R.id.covid);
        mSearchBtn = v.findViewById(R.id.search_btn);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(validateInputs() != true)
                    {
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Toast.makeText(TripPlannerFragment.this.getActivity(), "You are at hotel screen!", Toast.LENGTH_SHORT)
                        .show();

                // save information about the trip
                SimpleDateFormat df = new SimpleDateFormat("E MMM d yyyy", Locale.getDefault());
                Trip trip = Trip.get(TripPlannerFragment.this.getContext());
                trip.setStartDate(df.format(mStartDate));
                trip.setEndDate(df.format(mEndDate));
                trip.setNumAdult(Integer.valueOf(mNumAdult.getText().toString()));
                trip.setNumChild(mNumChild.getText().toString().isEmpty() == true ? 0 : Integer.valueOf(mNumChild.getText().toString()));
                trip.setSpokenLanguage(mLanguages.getSelectedItem().toString());
                trip.setCovid(mHasCovid.getCheckedRadioButtonId() == R.id.covid_yes ? true : false);

                mCanGoNextState = true;
                Intent iHotelListFragment = new Intent(TripPlannerFragment.this.getActivity(), HotelListActivity.class);
                startActivity(iHotelListFragment);
            }
        });

        mDestination.setText("www");
        mNumAdult.setText("12");
        return v;
    }

    // get current date with specified format
    public static String getCurrentDate() {
        // get current date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("E MMM d yyyy", Locale.getDefault());
        return df.format(c);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DatePickerFragment.PICK_START_DATE, mStartDateBtn.getText().toString());
        outState.putString(DatePickerFragment.PICK_END_DATE, mEndDateBtn.getText().toString());
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
                    mStartDate = dChosenDate;
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
                    mEndDate = dChosenDate;
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.trip_planner_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.hotels_menu_item:
                // check whether all input is fulfilled
                try {
                    boolean isAllInputValid = validateInputs();
                    if( isAllInputValid && mCanGoNextState == false)
                    {
                        Toast.makeText(this.getContext(), "Please, Hit the search button for searching hotels", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else {
                        // check whether the home screen got key for next screen which is Hotel screen
                        if (mCanGoNextState) {
                            Intent a = new Intent(this.getContext(), HotelListActivity.class);
                            a.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(a);
                        }
                        else
                        {
                            Toast.makeText(this.getContext(), "Please, make sure to provide information for all the required field and they must be valid ", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                    return true;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            case R.id.hotel_ticket_menu_item:
                // check whether the key for this activity is available
                if(HotelTicketFragment.mHasKeyActivity)
                {
                    Intent i = new Intent(this.getContext(), HotelTicketActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
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
     *	Return: boolean: return true iff the date is valid. Otherwise, false
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
                    break;
                }
                bRetVal = true;
                break;
            case DatePickerFragment.PICK_END_DATE_REQUEST:
                /* Check whether the end date is valid compare against the begin date (which dDateToCompare) */
                if(dChosenDate.compareTo(dDateToCompare) < 0)
                {
                    break;
                }
                bRetVal = true;
                break;
            default:
                break;
        }

        return bRetVal;

    }

    /*
     *	Function: boolean checkNumberTripper(int iNumPeople)
     *	Description:
     *       The purpose of this function is to check whether the number of people for a particular
     *       category such as adult is valid
     *	Parameter: int iNumPeople: number of people to be processed
     *	Return: boolean: return true if the number is valid. Otherwise, false
     */
    public static boolean checkNumberTripper(int iNumPeople)
    {
        boolean bRetVal = false;

        if(iNumPeople <= MAX_TRIPPER && iNumPeople != 0)
        {
            bRetVal = true;
        }

        return bRetVal;
    }

    /*
     *	Function: boolean checkRequiredField(String sValue)
     *	Description:
     *       The purpose of this function is to check whether the required field is not empty
     *	Parameter: String sValue: the value to be processed
     *	Return: boolean: return true if the field is not empty. Otherwise, false
     */
    public static boolean checkRequiredField(String sValue)
    {
        if(sValue.isEmpty())
        {
            return false;
        }
        return true;
    }

    /*
     *	Function: hideKeyboard(View view)
     *	Description:
     *       The purpose of this function is to hide the keyboard of an EditText widget after clicking
     *          outside of the EditText widget
     *	Parameter: View view: the view to be processed
     *	Return: void: Not return anything
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*
     *	Function: hideKeyboard(View view)
     *	Description:
     *       The purpose of this function is to check whether all the required input field is filled
     *	Parameter: not receive anything
     *	Return: boolean: return true if all the inputs are valid
     */
    private boolean validateInputs() throws ParseException {
        int iSumPeopleTrip = 0;
        int iHasCovid = mHasCovid.getCheckedRadioButtonId();

        // check whether they have covid-19 symptom
        if(iHasCovid == -1)
        {
            Toast.makeText(this.getActivity(), R.string.invalid_covid_label, Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        else if (iHasCovid == R.id.covid_yes)
        {
            Toast.makeText(this.getActivity(), R.string.covid_yes_label, Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        else
        {
            // check whether the destination is empty
            if(checkRequiredField(mDestination.getText().toString()) == false)
            {
                mDestinationLayout.setError("Sorry, Please enter your destination!");
                return false;
            }
            if(checkRequiredField(mStartDateBtn.getText().toString()) == false)
            {
                mStartDateLayout.setError("Sorry, Please enter the start date of the trip!");
                return false;
            }
            if(checkRequiredField(mEndDateBtn.getText().toString()) == false)
            {
                mEndDateLayout.setError("Sorry, Please enter the start date of the trip!");
                return false;
            }
            else if( mNumAdult.getText().toString().isEmpty())
            {
                mNumAdultLayout.setError("Sorry, at least 1 adult on the trip");
                return false;
            }
            else
            {

                try {
                    Integer.parseInt(mNumAdult.getText().toString());
                } catch(Exception e) {
                    mNumAdultLayout.setError("Sorry, please enter integer number");
                    return false;
                }

                if(!mNumChild.getText().toString().isEmpty())
                {
                    try {
                        Integer.parseInt(mNumChild.getText().toString());
                    } catch(Exception e) {
                        mNumChildLayout.setError("Sorry, please enter integer number");
                        return false;
                    }

                    iSumPeopleTrip = Integer.valueOf(mNumAdult.getText().toString()) + Integer.valueOf(mNumChild.getText().toString());
                }
                else
                {
                    iSumPeopleTrip = Integer.valueOf(mNumAdult.getText().toString());
                }

                // check whether the number people on the trip is not zero
                if(iSumPeopleTrip > MAX_TRIPPER)
                {
                    Toast.makeText(this.getActivity(), "Sorry, maximum people on the trip is 1000!", Toast.LENGTH_SHORT)
                    .show();
                    return false;
                }
                else if(checkNumberTripper(iSumPeopleTrip) == false)
                {
                    mNumAdultLayout.setError("Sorry, at least 1 adult on the trip");
                    return false;
                } else if (checkDate(DatePickerFragment.PICK_END_DATE_REQUEST, mStartDate, mEndDate) != true)
                {
                    Toast.makeText(this.getActivity(), R.string.invalid_date_label, Toast.LENGTH_SHORT).show();
                    return false;
                }

            }

        }

        return true;
    }


}
