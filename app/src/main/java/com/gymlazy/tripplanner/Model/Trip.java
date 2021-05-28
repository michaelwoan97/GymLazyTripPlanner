/*
*	PROJECT: Trip Planner
*	FILE: Trip.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the Trip class which is a singleton and it is used for any operations in regarding to the trip information
*/

package com.gymlazy.tripplanner.Model;

import android.content.Context;

import java.util.Date;

public class Trip {
    private String mDestination;
    private String mStartDate;
    private String mEndDate;
    private int mNumAdult;
    private int mNumChild;
    private int mEventCalendarID;
    private String mSpokenLanguage;
    private Boolean mIsCovid;
    private static Trip sTrip;

    public static Trip get(Context packageContext) {
        // check whether the singleton is not existed
        if (sTrip == null) {
            sTrip = new Trip(packageContext);
        }
        return sTrip;
    }

    public Trip(Context packageContext)
    {
        // constructor
    }

    public String getDestination() {
        return mDestination;
    }

    public void setDestination(String destination) {
        mDestination = destination;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }

    public int getNumAdult() {
        return mNumAdult;
    }

    public void setNumAdult(int numAdult) {
        mNumAdult = numAdult;
    }

    public int getNumChild() {
        return mNumChild;
    }

    public void setNumChild(int numChild) {
        mNumChild = numChild;
    }

    public String getSpokenLanguage() {
        return mSpokenLanguage;
    }

    public void setSpokenLanguage(String spokenLanguage) {
        mSpokenLanguage = spokenLanguage;
    }

    public Boolean getCovid() {
        return mIsCovid;
    }

    public void setCovid(Boolean covid) {
        mIsCovid = covid;
    }

    public int getEventCalendarID() {
        return mEventCalendarID;
    }

    public void setEventCalendarID(int eventCalendarID) {
        mEventCalendarID = eventCalendarID;
    }
}
