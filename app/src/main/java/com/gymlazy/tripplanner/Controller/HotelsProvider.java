package com.gymlazy.tripplanner.Controller;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.HotelList;
import com.gymlazy.tripplanner.Model.databases.HotelBaseHelper;

import java.io.IOException;
import java.util.ArrayList;

public class HotelsProvider extends ContentProvider {
    public static final String AUTHORITY = "com.gymlazy.tripplanner.tripHotels";
    public static final String BASE_PATH = "hotels";
    public static final Uri CONTENT_URI = Uri.parse("Content://" + AUTHORITY + "/" + BASE_PATH); // uniquely identify data sources

    // Return value for uri matcher
    private static final int HOTELS = 1;
    private static final int HOTEL_ID = 2;

    // set up uri
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, HOTELS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", HOTEL_ID);
    }

    private HotelList mHotelList; // singleton of the application

    @Override
    public boolean onCreate() {
        try {
            mHotelList = HotelList.get(this.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * query hotel data
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case HOTELS:
                // check whether it is requested to fetch all hotel data
                if(selection != null)
                {
                    cursor = mHotelList.queryHotelsCursor(selection, selectionArgs);
                } else {
                    cursor = mHotelList.queryHotelsCursor(null, null);
                }
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        /*
        They are used symbiotically. If you are implementing a ContentProvider, essentially when someone queries your provider, you produce a Cursor and call setNotificationUri() on it with some rational Uri (such as the Uri used to make the query). Later, if data served by your ContentProvider changes, e.g. after an insert/update/delete, you call getContentResolver().notifyChange(uri, null) so that anyone who currently has a Cursor (because they queried earlier) gets notified that data has changed and they should re-query. If they are using a CursorLoader, the re-query happens automatically.
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case HOTELS:
                return "vnd.android.cursor.dir/vnd.tripplanner.Hotel";
            case HOTEL_ID: // single hotel
                return "vnd.android.cursor.item/vnd.tripplanner.Hotel";
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int upCount = 0;

        switch (uriMatcher.match(uri)){
            case HOTELS:
                // check whether selectionArgs is not null
                if(selection != null){
                    upCount = mHotelList.updateFavoriteHotel(values, selection);
                } else {
                    upCount = mHotelList.updateFavoriteHotel(values, null);
                }
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }

        return upCount;
    }
}
