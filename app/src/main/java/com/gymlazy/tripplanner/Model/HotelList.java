package com.gymlazy.tripplanner.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gymlazy.tripplanner.Model.databases.HotelBaseHelper;
import com.gymlazy.tripplanner.Model.databases.HotelCursorWrapper;
import com.gymlazy.tripplanner.Model.databases.HotelDbSchema;
import com.gymlazy.tripplanner.Model.databases.HotelDbSchema.Cols;
import com.gymlazy.tripplanner.Model.databases.HotelDbSchema.hotelTable;
import com.gymlazy.tripplanner.R;

import java.io.IOException;
import java.util.ArrayList;

public class HotelList {
    private static HotelList sHotelList; // singleton
    private Context mContext;
//    private ArrayList<Hotel> mHotelList;
    private static final String TAG = "HotelList";
    private SQLiteDatabase mHotelDatabase;

    // get the singleton if it exist. Otherwise, instantiate one
    public static HotelList get(Context context) throws IOException {
        // check whether the HotelList has already has an instance
        if(sHotelList == null)
        {
            sHotelList = new HotelList(context);
        }

        return sHotelList;
    }

    // constructor
    private HotelList(Context context) throws IOException {
        mContext = context.getApplicationContext(); // for later of using database
        mHotelDatabase = new HotelBaseHelper(mContext).getWritableDatabase();

//        mHotelList = new ArrayList<>();
//        for(int i = 0; i < 5; i++)
//        {
//            Hotel hotel = new Hotel();
//            hotel.setHotelId(i);
//            hotel.setHotelName("Nha Trang");
//            hotel.setHotelDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum");
//            hotel.setHotelImage(R.drawable.nha_trang);
//            hotel.setFavorite(false);
//            mHotelList.add(hotel);
//        }
    }

    // get hotel list from database
    public ArrayList<Hotel> getHotelList()
    {
//        if(!mHotelList.isEmpty())
//        {
//            return mHotelList;
//        }
//        return null;

        ArrayList<Hotel> hotels = new ArrayList<Hotel>();

        // select everthing from the table and wrap it with CursorWrapper
        HotelCursorWrapper hotelCursorWrapper = new HotelCursorWrapper(queryHotels(null, null));

        try {
            hotelCursorWrapper.moveToFirst();
            while(!hotelCursorWrapper.isAfterLast()){
                hotels.add(hotelCursorWrapper.getHotel());
                hotelCursorWrapper.moveToNext();
            }
        } finally {
            hotelCursorWrapper.close();
        }

        return hotels;
    }

    // get hotel data's table from the database and return a cursor which is wrapped around
    // cursor wrapper
    private HotelCursorWrapper queryHotels(String whereClause, String[] whereArgs){
        Cursor cursor = mHotelDatabase.query(
                hotelTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new HotelCursorWrapper(cursor);
    }

    // get a specific hotel id from the database
    public Hotel getHotel(int iHotelID){
        HotelCursorWrapper hotelCursor = new HotelCursorWrapper(queryHotels(
                Cols.ID + " = ?",
                new String[]{String.valueOf(iHotelID)}));// sql statement to get the cursor the hotel

        try {
            // check whether it return not zero
            if(hotelCursor.getCount() == 0)
            {
                return null;
            }

            hotelCursor.moveToFirst();
            return hotelCursor.getHotel();
        } finally {
            hotelCursor.close();
        }
//        if(!mHotelList.isEmpty())
//        {
//            for(Hotel hotel : mHotelList)
//            {
//                if(hotel.getHotelId() == iHotelID)
//                {
//                    return hotel;
//                }
//            }
//        }

    }

    /*
     *	Function: void updateFavoriteHotel(Hotel hotel)
     *	Description:
     *       The purpose of this function is to update hotel with the favorite status
     *	Parameter: Hotel hotel: hotel to be updated
     *	Return: not return anything
     */
    public void updateFavoriteHotel(Hotel hotel){
//        for(Hotel h : mHotelList)
//        {
//            if(hotel.getHotelId() == h.getHotelId())
//            {
//                h.setFavorite(hotel.isFavorite());
//                Log.i(TAG, "Update successful! " + h.getHotelName() + " " + h.getHotelId() + "- " + ( h.isFavorite() ? "is favorite" : "is not favorite!"));
//                break;
//            }
//        }
        ContentValues cvHotel = null;
        HotelCursorWrapper hotelCursorWrapper = new HotelCursorWrapper(queryHotels(
                Cols.ID + " = ?",
                new String[]{String.valueOf(hotel.getHotelId())}
        ));

        try {
            // check whether the cursor returned is not null
            if(hotelCursorWrapper.getCount() == 0){
                return;
            }

            hotelCursorWrapper.moveToFirst();
            cvHotel = getContentValues(hotel);
            mHotelDatabase.update(
                    hotelTable.NAME,
                    cvHotel,
                    Cols.ID + " = ?",
                    new String[]{ String.valueOf(hotel.getHotelId())}
            );

        } finally {
            hotelCursorWrapper.close();
        }
    }

    /**
     * create content-value pair object
     * @param hotel
     * @return the hotel as content-value pair
     */
    public ContentValues getContentValues(Hotel hotel){
        ContentValues hotelContentValue = new ContentValues();
        hotelContentValue.put(Cols.ID, hotel.getHotelId());
        hotelContentValue.put(Cols.NAME, hotel.getHotelName());
        hotelContentValue.put(Cols.IMG, hotel.getStringHotelImage());
        hotelContentValue.put(Cols.FAVORITE, hotel.isFavorite());
        hotelContentValue.put(Cols.DESCRIPTION, hotel.getHotelDescription());
        hotelContentValue.put(Cols.ADDRESS, hotel.getHotelAddress());
        hotelContentValue.put(Cols.WEB_URL, hotel.getHotelWebURL());
        hotelContentValue.put(Cols.PHONE_NUMBER, hotel.getHotelPhoneNumber());

        return hotelContentValue;
    }
}
