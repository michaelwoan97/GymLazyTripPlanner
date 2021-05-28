/*
*	PROJECT: Trip Planner
*	FILE: HotelListActivity.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the HotelListActivity class for hosting HotelListFragment
*/

package com.gymlazy.tripplanner.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.gymlazy.tripplanner.TripPlannerActivity;
import com.gymlazy.tripplanner.TripPlannerFragment;

public class HotelListActivity extends SingleFragmentActivity{
    private static final String TAG = "HotelListActivity";

    public static Intent newIntent(Context context) {
        return new Intent(context, HotelListActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return new HotelListFragment();
    }

    @Override
    public void onBackPressed() {
        // check whether the previous screen has the key for the current screen which hotel screen
        if(TripPlannerFragment.mCanGoNextState)
        {
            TripPlannerFragment.mCanGoNextState = false;
            finish();
        }
    }



}
