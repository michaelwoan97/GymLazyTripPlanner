package com.gymlazy.tripplanner.Controller;

import android.util.Log;

import androidx.fragment.app.Fragment;

public class HotelListActivity extends SingleFragmentActivity{
    private static final String TAG = "HotelListActivity";

    @Override
    protected Fragment createFragment() {
        return new HotelListFragment();
    }
}
