/*
*	PROJECT: Trip Planner
*	FILE: TripPlannerActivity.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the TripPlannerActivity class for hosting TripPlannerFragment
*/

package com.gymlazy.tripplanner;

import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.gymlazy.tripplanner.Controller.HotelsProvider;
import com.gymlazy.tripplanner.Controller.SingleFragmentActivity;
import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.databases.HotelCursorWrapper;
import com.gymlazy.tripplanner.Model.databases.HotelDbSchema;

public class TripPlannerActivity extends SingleFragmentActivity {
    private static final String TAG = "TripPlannerActivity";

    @Override
    protected Fragment createFragment() {
        return TripPlannerFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

    }

}