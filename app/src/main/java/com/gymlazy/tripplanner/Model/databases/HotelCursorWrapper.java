package com.gymlazy.tripplanner.Model.databases;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.databases.HotelDbSchema.Cols;

public class HotelCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public HotelCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Hotel getHotel() {
        // extract the value from a row by using cursor
        int hotelId = getInt(getColumnIndex(Cols.ID));
        String hotelName = getString(getColumnIndex(Cols.NAME));
        String hotelImg = getString(getColumnIndex(Cols.IMG));
        Boolean favorite = getInt(getColumnIndex(Cols.FAVORITE)) > 0;
        String hotelDescription = getString(getColumnIndex(Cols.DESCRIPTION));
        String hotelWebURL = getString(getColumnIndex(Cols.WEB_URL));
        String hotelAddress = getString(getColumnIndex(Cols.ADDRESS));
        String hotelPhone = getString(getColumnIndex(Cols.PHONE_NUMBER));

        // create Hotel instance
        Hotel hotel = new Hotel(hotelId);
        hotel.setHotelName(hotelName);
        hotel.setStringHotelImage(hotelImg);
        hotel.setHotelDescription(hotelDescription);
        hotel.setHotelWebURL(hotelWebURL);
        hotel.setHotelAddress(hotelAddress);
        hotel.setFavorite(favorite);
        hotel.setHotelPhoneNumber(hotelPhone);

        return hotel;


        /// retrieve hotel list
        /// render
        /// download image
        /// async
    }
}
