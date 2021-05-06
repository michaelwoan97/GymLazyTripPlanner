package com.gymlazy.tripplanner.Controller;

import androidx.fragment.app.Fragment;

public class HotelListActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new HotelListFragment();
    }
}
