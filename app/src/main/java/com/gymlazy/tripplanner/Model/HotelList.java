package com.gymlazy.tripplanner.Model;

import android.content.Context;

import com.gymlazy.tripplanner.R;

import java.util.ArrayList;

public class HotelList {
    private static HotelList sHotelList; // singleton
    private Context mContext;
    private ArrayList<Hotel> mHotelList;

    // get the singleton if it exist. Otherwise, instantiate one
    public static HotelList get(Context context){
        // check whether the HotelList has already has an instance
        if(sHotelList == null)
        {
            sHotelList = new HotelList(context);
        }

        return sHotelList;
    }

    private HotelList(Context context) {
        mContext = context.getApplicationContext(); // for later of using database
        mHotelList = new ArrayList<>();
        for(int i = 0; i < 5; i++)
        {
            Hotel hotel = new Hotel();
            hotel.setHotelId(i);
            hotel.setHotelName("Nha Trang");
            hotel.setHotelDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum");
            hotel.setHotelImage(R.drawable.nha_trang);
            mHotelList.add(hotel);
        }
    }

    public ArrayList<Hotel> getHotelList()
    {
        if(!mHotelList.isEmpty())
        {
            return mHotelList;
        }
        return null;
    }

    public Hotel getHotel(int iHotelID){
        if(!mHotelList.isEmpty())
        {
            for(Hotel hotel : mHotelList)
            {
                if(hotel.getHotelId() == iHotelID)
                {
                    return hotel;
                }
            }
        }
        return null;

    }
}
