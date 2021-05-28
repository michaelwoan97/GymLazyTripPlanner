/*
*	PROJECT: Trip Planner
*	FILE: HotelList.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the HotelList class which is a singleton and it is used for any operations in regarding to the hotel list information
*/

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
    }

    // get hotel list from database
    public ArrayList<Hotel> getHotelList()
    {
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

    /**
     * retrieve hotel list
     * @param hotelCursorWrapper
     * @return
     */
    public ArrayList<Hotel> getHotelList(HotelCursorWrapper hotelCursorWrapper)
    {
        ArrayList<Hotel> hotels = new ArrayList<Hotel>();

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

    // get hotel data's table from the database and return a cursor which is wrapped around
    // return cursor for usage of content providerg
    /**
     * query hotel return cursor for the content provider
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public Cursor queryHotelsCursor(String whereClause, String[] whereArgs){
        Cursor cursor = mHotelDatabase.query(
                hotelTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        return cursor;
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

    }

    /*
     *	Function: void updateFavoriteHotel(Hotel hotel)
     *	Description:
     *       The purpose of this function is to update hotel with the favorite status
     *	Parameter: Hotel hotel: hotel to be updated
     *	Return: not return anything
     */
    public void updateFavoriteHotel(Hotel hotel){
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
     * update favorite hotel for content provider
     * @param hotelContentValue
     * @param selection
     * @return
     */
    public int updateFavoriteHotel(ContentValues hotelContentValue, String selection){
        int result = mHotelDatabase.update(
                hotelTable.NAME,
                hotelContentValue,
                selection,
                null
        );

        return result;
    }

    /**
     * create content-value pair object
     * @param hotel
     * @return the hotel as content-value pair
     */
    public static ContentValues getContentValues(Hotel hotel){
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
