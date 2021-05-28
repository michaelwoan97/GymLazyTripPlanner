/*
*	PROJECT: Trip Planner
*	FILE: HotelPagerActivity.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the HotelPagerActivity class used for hosting HotelFragment
*/

package com.gymlazy.tripplanner.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

import java.io.IOException;
import java.util.List;

public class HotelPagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Hotel> mHotelList;
    private static final String EXTRA_HOTEL_ID = "com.gymlazy.tripplaner.hotel_id";
    private static final String TAG = "HotelPagerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_pager);

        int iHotelID = (int) getIntent().getSerializableExtra(EXTRA_HOTEL_ID);
        mViewPager = (ViewPager) findViewById(R.id.hotel_view_pager);
        try {
            mHotelList = HotelList.get(this).getHotelList();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
//        for(Hotel hotel : mHotelList)
//        {
//            if(hotel.getHotelId() == iHotelID)
//            {
//                // minus 1 is because the array list start at 0
//                // because the beginning of the hotel list has the id of 1
//                // it cause the adapter to start get the hotel at 1 index
//
//                mViewPager.setCurrentItem(iHotelID - 1);
//                break;
//            }
//        }

        // this loop is much better than above because it help
        // the adapter to start extract hotel data at first index which is 0
        for (int i = 0; i < mHotelList.size(); i++) {
            if (mHotelList.get(i).getHotelId() == iHotelID) {
                // set current item also set the current position in the adapter
                mViewPager.setCurrentItem(i);
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
