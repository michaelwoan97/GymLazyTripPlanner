package com.gymlazy.tripplanner.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class QueryPreferences {
    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_LAST_HOTEL_ID = "lastHotelID";

    /**
     * get the last query from the search destination
     * @param packageContext
     * @return
     */
    public static String getStoredQuery(Context packageContext){
        return PreferenceManager.getDefaultSharedPreferences(packageContext)
                .getString(PREF_SEARCH_QUERY, null);
    }

    /**
     * store the query of the destination in shared preferences
     * @param packageContext
     * @param query
     */
    public static void setStoredQuery(Context packageContext, String query){
        PreferenceManager.getDefaultSharedPreferences(packageContext)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply();
    }

    /**
     * get the last hotel ID of the searched destination
     * @param packageContext
     * @return
     */
    public static int getPrefLastHotelId(Context packageContext){
        return PreferenceManager.getDefaultSharedPreferences(packageContext)
                .getInt(PREF_LAST_HOTEL_ID, 0);
    }

    /**
     * save the latest hotel id of the searched destination
     * @param packageContext
     * @param iHotelID
     */
    public static void setPrefLastHotelId(Context packageContext, int iHotelID){
        PreferenceManager.getDefaultSharedPreferences(packageContext)
                .edit()
                .putInt(PREF_LAST_HOTEL_ID, iHotelID)
                .apply();
    }


}
