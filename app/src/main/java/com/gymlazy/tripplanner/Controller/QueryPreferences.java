/*
*	PROJECT: Trip Planner
*	FILE: QueryPreferences.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the QueryPreferences class used for getting/saving information into shared preferences
*/

package com.gymlazy.tripplanner.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class QueryPreferences {
    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_LAST_HOTEL_ID = "lastHotelID";
    private static final String PREF_IS_ALARM_ON = "isAlarmOn";
    private static final String PREF_EVENT_ID = "eventID";
    private static final String PREF_START_DATE = "startDate";
    private static final String PREF_END_DATE = "endDate";

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

    public static boolean isAlarmOn(Context packageContext){
        return PreferenceManager.getDefaultSharedPreferences(packageContext)
                .getBoolean(PREF_IS_ALARM_ON, false);
    }

    public static void setAlarmOn(Context packgeContext, boolean isOn){
        PreferenceManager.getDefaultSharedPreferences(packgeContext)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, isOn)
                .apply();
    }

    /**
     * get the event ID of the newly created trip
     * @param packageContext
     * @return
     */
    public static int getPrefEventID(Context packageContext){
        return PreferenceManager.getDefaultSharedPreferences(packageContext)
                .getInt(PREF_EVENT_ID, 0);
    }

    /**
     * save the event ID of the newly created trip
     * @param packageContext
     * @param iHotelID
     */
    public static void setPrefEventId(Context packageContext, int iHotelID){
        PreferenceManager.getDefaultSharedPreferences(packageContext)
                .edit()
                .putInt(PREF_EVENT_ID, iHotelID)
                .apply();
    }


    /**
     * get the start date of the newly created trip
     * @param packageContext
     * @return
     */
    public static String getPrefStartDate(Context packageContext){
        return PreferenceManager.getDefaultSharedPreferences(packageContext)
                .getString(PREF_START_DATE, null);
    }

    /**
     * save the start date of the newly created trip
     * @param packageContext
     * @param sHotelStartDate
     */
    public static void setPrefStartDate(Context packageContext, String sHotelStartDate){
        PreferenceManager.getDefaultSharedPreferences(packageContext)
                .edit()
                .putString(PREF_START_DATE, sHotelStartDate)
                .apply();
    }

    /**
     * get the end date of the newly created trip
     * @param packageContext
     * @return
     */
    public static String getPrefEndDate(Context packageContext){
        return PreferenceManager.getDefaultSharedPreferences(packageContext)
                .getString(PREF_END_DATE, null);
    }

    /**
     * save the end date of the newly created trip
     * @param packageContext
     * @param sHotelEndDate
     */
    public static void setPrefEndDate(Context packageContext, String sHotelEndDate){
        PreferenceManager.getDefaultSharedPreferences(packageContext)
                .edit()
                .putString(PREF_END_DATE, sHotelEndDate)
                .apply();
    }


}
