package com.gymlazy.tripplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

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