/*
*	PROJECT: Trip Planner
*	FILE: HotelTicketFragment.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the HotelTicketFragment class used for confirm whether the hotel is going to be booked
*/

package com.gymlazy.tripplanner.Controller;

import android.Manifest;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.HotelList;
import com.gymlazy.tripplanner.Model.Trip;
import com.gymlazy.tripplanner.R;
import com.gymlazy.tripplanner.TripPlannerActivity;
import com.gymlazy.tripplanner.TripPlannerFragment;
import com.gymlazy.tripplanner.Utils.ImageDownloader;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class HotelTicketFragment extends Fragment {
    private TextView mHotelName;
    private TextView mTVNumTripper;
    private TextView mTVDateTrip;
    private ImageView mHotelImage;
    private TextView mHotelPhone;
    private TextView mHotelAddress;
    private Button mConfirmBtn;
    private CheckBox mCheckBox;
    private static final String ARG_HOTEL_ID = "hotelID";
    private static final String TAG = "HotelTicketFragment";
    private static final int SET_UP_EVENTS = 0;
    private static final int REQUEST_EVENT_CODE = 10;
    public static boolean mHasKeyActivity;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM d yyyy", Locale.getDefault());
    private Hotel mHotel;
    private Trip mTrip;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mHasKeyActivity = true;
        int iHotelID = (int) getArguments().getSerializable(ARG_HOTEL_ID);
        try {
            mHotel = HotelList.get(this.getContext()).getHotel(iHotelID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mTrip = Trip.get(this.getContext());

        Log.d(TAG, mTrip.getStartDate() + "->" + mTrip.getEndDate());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.trip_planner_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_menu_item:
                Intent homeIntent = new Intent(this.getContext(), TripPlannerActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(homeIntent);
                return true;
            case R.id.hotels_menu_item:
                Intent i = new Intent(this.getContext(), HotelPagerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                mHasKeyActivity = false;
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.hotel_ticket_fragment, container, false);

        mHotelName = (TextView) v.findViewById(R.id.hotel_name_confirm);
        mHotelName.setText(mHotel.getHotelName() + " " + mHotel.getHotelId());

        mHotelImage = (ImageView) v.findViewById(R.id.hotel_img_confirm);
        // get image file of the hotel to display
        String root = Environment.getExternalStorageDirectory().toString(); // get external storage location
        String sFilePath = ImageDownloader.getImageNameFromURL(mHotel.getStringHotelImage(), root);

        // convert file to bitmap
        Bitmap hotelBitmap = BitmapFactory.decodeFile(sFilePath);
        mHotelImage.setImageBitmap(hotelBitmap);

        mTVNumTripper = (TextView) v.findViewById(R.id.trip_people_confirm);
        mTVNumTripper.setText(getString(R.string.trip_people_confirm, mTrip.getNumAdult(), mTrip.getNumChild()));

        mTVDateTrip = (TextView) v.findViewById(R.id.trip_date_confirm);
        mTVDateTrip.setText(getString(R.string.trip_date_confirm, mTrip.getStartDate(), mTrip.getEndDate()));

        mHotelAddress = v.findViewById(R.id.hotel_address_confirm);
        mHotelAddress.setText(getString(R.string.hotel_address_confirm, mHotel.getHotelAddress()));

        mHotelPhone = v.findViewById(R.id.hotel_phone_confirm);
        mHotelPhone.setText(getString(R.string.hotel_phone_confirm, mHotel.getHotelPhoneNumber()));

        mCheckBox = (CheckBox) v.findViewById(R.id.agreenment_confirm);
        mConfirmBtn = (Button) v.findViewById(R.id.confirm_btn);
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check whether the user agree to the term
                if(!mCheckBox.isChecked())
                {
                    Toast.makeText(HotelTicketFragment.this.getContext(), "Please, read and agree with the term and condition!", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                //  Permission check
                if (!hasPermissions(HotelTicketFragment.this.getContext(), new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR})) {
                    // Permission ask
                    requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, REQUEST_EVENT_CODE);
                } else {
                    // if permission is already granted then open calendar to set up event
                    setUpCalendarEvent();
                }

            }
        });

        return v;
    }

    /**
     * open calendar to set up an event
     */
    private void setUpCalendarEvent() {
        try {
            // convert simple date format to UTC milliseconds from the epoch.
            long lBeginDate = simpleDateFormat.parse(mTrip.getStartDate()).getTime();
            long lEndDate = simpleDateFormat.parse(mTrip.getEndDate()).getTime();

            // calendar content provider for establishing an event
            Intent i = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, lBeginDate)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, lEndDate)
                    .putExtra(CalendarContract.Events.TITLE, mTrip.getDestination() + " Trip")
                    .putExtra(CalendarContract.Events.DESCRIPTION, "The duration of the trip")
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, mHotel.getHotelName())
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

            startActivityForResult(i, SET_UP_EVENTS);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED)
        {
            saveEventID();

            notifyTimerAppWidget();

            //build intent
            backToDestinationActivity();
        }
    }

    /**
     * use for noticing the app widget
     */
    private void notifyTimerAppWidget() {
        Intent intent = new Intent(this.getContext(), TripPlannerWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(getContext())
                .getAppWidgetIds(new ComponentName(this.getActivity(), TripPlannerWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        this.getActivity().sendBroadcast(intent);
    }

    /**
     * use for saving event ID
     */
    private void saveEventID() {
        int iEventID = listSelectedCalendars(mTrip.getDestination() + " Trip");
        Log.d(TAG, "The event ID for the newly created trip is: " + iEventID);
        mTrip.setEventCalendarID(iEventID);
        QueryPreferences.setPrefEventId(this.getContext(), iEventID);
    }

    /**
     * use for building intent to go back to destination activity
     */
    private void backToDestinationActivity() {
        Intent i = new Intent(HotelTicketFragment.this.getContext(), TripPlannerActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        getActivity().finish();
        mHasKeyActivity = false;
        Toast.makeText(HotelTicketFragment.this.getContext(), "Thank you and Enjoy the Trip", Toast.LENGTH_SHORT)
                .show();
    }

    public static Fragment newInstance(int iHotelID){
        Bundle b = new Bundle();
        b.putSerializable(ARG_HOTEL_ID, iHotelID);

        HotelTicketFragment hotelTicketFragment = new HotelTicketFragment();
        hotelTicketFragment.setArguments(b);
        return hotelTicketFragment;
    }

    /**
     * to find the event ID for the trip event newly created
     * @param eventtitle
     * @return
     */
    private int listSelectedCalendars(String eventtitle) {


        Uri eventUri;
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            // the old way

            eventUri = Uri.parse("content://calendar/events");
        } else {
            // the new way

            eventUri = Uri.parse("content://com.android.calendar/events");
        }

        int result = 0;
        String projection[] = { "_id", "title" };
        Cursor cursor = getActivity().getContentResolver().query(eventUri, null, null, null,
                null);

        if (cursor.moveToFirst()) {

            String calName;
            String calID;

            int nameCol = cursor.getColumnIndex(projection[1]);
            int idCol = cursor.getColumnIndex(projection[0]);

            /* loop and try to find the event Id for the newly created event for the trip
             */
            do {
                calName = cursor.getString(nameCol);
                calID = cursor.getString(idCol);

                if (calName != null && calName.contains(eventtitle)) {
                    result = Integer.parseInt(calID);
                }

            } while (cursor.moveToNext());
            cursor.close();
        }

        return result;

    }

    // Function for check permission already granted or not
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"Permission is denied");
                    return false;
                }
            }
        }
        Log.d(TAG,"Permission is granted");
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_EVENT_CODE) {
            if(grantResults.length != 0 ){

                // check whether the user agree to open calendar
                for (int result : grantResults){
                    if(result != PackageManager.PERMISSION_GRANTED){
                        backToDestinationActivity();
                        return;
                    }
                    Log.d(TAG,"Permission " + result + " is granted");
                }
                // if permission is already granted then use calendar
                setUpCalendarEvent();
            } else {
                //build intent
                backToDestinationActivity();
            }

        }

    }
}
