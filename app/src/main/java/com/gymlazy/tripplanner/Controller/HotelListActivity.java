package com.gymlazy.tripplanner.Controller;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.gymlazy.tripplanner.TripPlannerActivity;
import com.gymlazy.tripplanner.TripPlannerFragment;

public class HotelListActivity extends SingleFragmentActivity{
    private static final String TAG = "HotelListActivity";

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
