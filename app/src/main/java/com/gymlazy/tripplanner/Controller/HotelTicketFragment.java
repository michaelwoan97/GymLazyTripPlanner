package com.gymlazy.tripplanner.Controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
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
    public static boolean mHasKeyActivity;
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
                Intent i = new Intent(HotelTicketFragment.this.getContext(), TripPlannerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                getActivity().finish();
                mHasKeyActivity = false;
                Toast.makeText(HotelTicketFragment.this.getContext(), "Thank you and Enjoy the Trip", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        return v;
    }

    public static Fragment newInstance(int iHotelID){
        Bundle b = new Bundle();
        b.putSerializable(ARG_HOTEL_ID, iHotelID);

        HotelTicketFragment hotelTicketFragment = new HotelTicketFragment();
        hotelTicketFragment.setArguments(b);
        return hotelTicketFragment;
    }
}
