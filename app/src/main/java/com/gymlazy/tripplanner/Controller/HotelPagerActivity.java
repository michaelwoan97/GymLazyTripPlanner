package com.gymlazy.tripplanner.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.HotelList;
import com.gymlazy.tripplanner.R;

import java.util.List;

public class HotelPagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Hotel> mHotelList;
    private static final String EXTRA_HOTEL_ID = "com.gymlazy.tripplaner.hotel_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_pager);

        int iHotelID = (int) getIntent().getSerializableExtra(EXTRA_HOTEL_ID);
        mViewPager = (ViewPager) findViewById(R.id.hotel_view_pager);
        mHotelList = HotelList.get(this).getHotelList();
        FragmentManager fm = getSupportFragmentManager(); // fragment manager is involved in the conservation between ViewPager and PagerAdapter
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Hotel hotel = mHotelList.get(position);
                return HotelFragment.newInstance(hotel.getHotelId());
            }

            @Override
            public int getCount() {
                return mHotelList.size();
            }
        });

        // display the selected hotel not the first in the list
        for(Hotel hotel : mHotelList)
        {
            if(hotel.getHotelId() == iHotelID)
            {
                mViewPager.setCurrentItem(iHotelID);
                break;
            }
        }



    }

    /*
     *	Function: static Intent newIntent(Context packageContext, int iHotelId)
     *	Description:
     *       The purpose of this function is to create a new intent for hotel activity class
     *	Parameter: Context packageContext : the context of the application
     *             int iHotelId : hotel id
     *	Return: intent: return intent
     */
    public static Intent newIntent(Context packageContext, int iHotelId){
        Intent i = new Intent(packageContext, HotelPagerActivity.class);
        i.putExtra(EXTRA_HOTEL_ID, iHotelId);
        return i;
    }
}
