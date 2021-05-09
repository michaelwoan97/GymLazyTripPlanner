package com.gymlazy.tripplanner.Model;

import android.content.Context;

import java.util.Date;

public class Trip {
    private String mStartDate;
    private String mEndDate;
    private int mNumAdult;
    private int mNumChild;
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
}
