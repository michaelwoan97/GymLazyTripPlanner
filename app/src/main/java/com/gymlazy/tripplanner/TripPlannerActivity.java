package com.gymlazy.tripplanner;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.gymlazy.tripplanner.Controller.SingleFragmentActivity;

public class TripPlannerActivity extends SingleFragmentActivity {

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