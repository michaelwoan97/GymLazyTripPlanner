/*
*	PROJECT: Trip Planner
*	FILE: Hotel.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the Hotel class which is the hotel model
*/

package com.gymlazy.tripplanner.Model;

import androidx.recyclerview.widget.RecyclerView;

public class Hotel {
    private int mHotelId;
    private String mHotelName;
    private String mHotelDescription;
    private int mHotelImage;
    private String mStringHotelImage; // for database usage
    private String mHotelAddress;
    private String mHotelWebURL;
    private String mHotelPhoneNumber;
    private boolean mIsFavorite;

    public Hotel(int iHotelID){
        mHotelId = iHotelID;
    }

    public int getHotelId() {
        return mHotelId;
    }

    public void setHotelId(int hotelId) {
        mHotelId = hotelId;
    }

    public String getHotelName() {
        return mHotelName;
    }

    public void setHotelName(String hotelName) {
        mHotelName = hotelName;
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

    public String getStringHotelImage() {
        return mStringHotelImage;
    }

    public void setStringHotelImage(String stringHotelImage) {
        mStringHotelImage = stringHotelImage;
    }

    public String getHotelAddress() {
        return mHotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        mHotelAddress = hotelAddress;
    }

    public String getHotelWebURL() {
        return mHotelWebURL;
    }

    public void setHotelWebURL(String hotelWebURL) {
        mHotelWebURL = hotelWebURL;
    }

    public String getHotelPhoneNumber() {
        return mHotelPhoneNumber;
    }

    public void setHotelPhoneNumber(String hotelPhoneNumber) {
        mHotelPhoneNumber = hotelPhoneNumber;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }
}
