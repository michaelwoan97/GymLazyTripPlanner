package com.gymlazy.tripplanner.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.HotelList;

public class HotelTicketActivity extends SingleFragmentActivity {
    private static final String EXTRA_HOTEL_ID = "com.gymlazy.tripplanner.hotelticketactivity.hotel_id";

    @Override
    protected Fragment createFragment() {
        int iHotelID = (int) getIntent().getSerializableExtra(EXTRA_HOTEL_ID);
        return HotelTicketFragment.newInstance(iHotelID);
    }


    public static Intent newIntent(Context packageContext, int iHotelId){
        Intent i = new Intent(packageContext, HotelTicketActivity.class);
        i.putExtra(EXTRA_HOTEL_ID, iHotelId);
        return i;
    }
}
