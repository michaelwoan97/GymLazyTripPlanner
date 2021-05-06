package com.gymlazy.tripplanner.Model;

import androidx.recyclerview.widget.RecyclerView;

public class Hotel {
    private int mHotelId;
    private String mHotelName;
    private String mHotelDescription;
    private int mHotelImage;

    public String getHotelName() {
        return mHotelName;
    }

    public void setHotelName(String hotelName) {
        mHotelName = hotelName;
    }

    public int getHotelId() {
        return mHotelId;
    }

    public void setHotelId(int hotelId) {
        mHotelId = hotelId;
    }

    public String getHotelDescription() {
        return mHotelDescription;
    }

    public void setHotelDescription(String hotelDescription) {
        mHotelDescription = hotelDescription;
    }

    public int getHotelImage() {
        return mHotelImage;
    }

    public void setHotelImage(int hotelImage) {
        mHotelImage = hotelImage;
    }
}
