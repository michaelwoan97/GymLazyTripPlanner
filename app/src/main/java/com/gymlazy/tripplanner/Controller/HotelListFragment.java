package com.gymlazy.tripplanner.Controller;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.HotelList;
import com.gymlazy.tripplanner.Model.databases.HotelBaseHelper;
import com.gymlazy.tripplanner.R;
import com.gymlazy.tripplanner.TripPlannerActivity;

import java.io.IOException;
import java.util.List;

public class HotelListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private HotelAdapter mHotelAdapter;
    private boolean mIsSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final String TAG = "HotelListFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // test database
        try {
            SQLiteDatabase hotelBaseHelper = new HotelBaseHelper(this.getContext()).getWritableDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(savedInstanceState != null)
        {
            mIsSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        else
        {
            mIsSubtitleVisible = false;
        }

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.hotel_list_fragment, container, false);
        mRecyclerView = v.findViewById(R.id.hotel_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateUI();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.trip_planner_menu, menu);
        menu.add(0, R.id.show_subtitle, Menu.NONE, getString(R.string.show_sub_menu_item)).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        if(mIsSubtitleVisible)
        {
            menu.findItem(R.id.show_subtitle).setTitle(R.string.hide_sub_menu_item);
        }
        else
        {
            menu.findItem(R.id.show_subtitle).setTitle(R.string.show_sub_menu_item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.show_subtitle:
                mIsSubtitleVisible = !mIsSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            case R.id.home_menu_item:
                Intent a = new Intent(this.getContext(), TripPlannerActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(a);
            case R.id.hotel_ticket_menu_item:
                // check whether the key for this activity is available
                if(HotelTicketFragment.mHasKeyActivity)
                {
                    Intent i = new Intent(this.getContext(), HotelTicketActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mIsSubtitleVisible);
    }

    // view holder
    private class HotelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mHotelName;
        private TextView mHotelDescription;
        private ImageView mHotelImage;
        private ImageButton mFavorite;
        private boolean mIsFavorite;
        private Hotel mHotel;

        /*
        *   Purpose: create ViewHolder
        *   LayoutInflater layoutInflater : inflate layout for ViewHolder
        *   ViewGroup parent : the parent of the ViewHolder
        * */
        public HotelViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            // Assign reference of the view to itemView field of the base class
            super(layoutInflater.inflate(R.layout.hotel_list_item, parent, false));

            // assign references for the widgets
            mHotelName = itemView.findViewById(R.id.hotel_name);
            mHotelDescription = itemView.findViewById(R.id.hotel_description);
            mHotelImage = itemView.findViewById(R.id.hotel_image);
            mFavorite = itemView.findViewById(R.id.fav_img_btn);

            mHotelName.setOnClickListener(this);
            mHotelImage.setOnClickListener(this);
            mHotelDescription.setOnClickListener(this);

        }

        public void bind(Hotel hHotel){
            mHotel = hHotel;
            mHotelName.setText(mHotel.getHotelName());
            mHotelDescription.setText(mHotel.getHotelDescription());
            mHotelImage.setImageResource(mHotel.getHotelImage());
            mIsFavorite = mHotel.isFavorite();
            mFavorite.setSelected(mIsFavorite ? true : false);
            mFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIsFavorite = !mIsFavorite;
                    mHotel.setFavorite(mIsFavorite);
                    HotelList.get(getActivity()).updateFavoriteHotel(mHotel);
                    v.setSelected(mIsFavorite ? true : false);
                }
            });
        }

        @Override
        public void onClick(View v) {
            // create intent
            Intent iHotelIntent = HotelPagerActivity.newIntent(getContext(), mHotel.getHotelId());
            startActivity(iHotelIntent);// start activity
        }
    }

    private class HotelAdapter extends RecyclerView.Adapter<HotelViewHolder> {
        private List<Hotel> mHotelList;

        public HotelAdapter(List<Hotel> lHotelList)
        {
            mHotelList = lHotelList;
        }

        @NonNull
        @Override
        public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new HotelViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
            Hotel hotel = mHotelList.get(position);
            holder.bind(hotel);
        }


        @Override
        public int getItemCount() {
            return mHotelList.size();
        }


    }

    /*
     *	Function: updateUI()
     *	Description:
     *       The purpose of this function is to update the hotel UI
     *	Parameter: not receive anything
     *	Return: void: Not return anything
     */
    private void updateUI()
    {
        // get hotel list
        HotelList hlHotelList = HotelList.get(getContext());
        List<Hotel> lHotelList = hlHotelList.getHotelList();

        // create adapter
        mHotelAdapter = new HotelAdapter(lHotelList);
        mRecyclerView.setAdapter(mHotelAdapter);

        updateSubtitle();
    }

    /*
     *	Function: void updateSubtitle()
     *	Description:
     *       The purpose of this function is to update the subtitle for the hotel results
     *	Parameter: not receive anything
     *	Return: void: Not return anything
     */
    private void updateSubtitle()
    {
        List<Hotel> hotelList = HotelList.get(getActivity()).getHotelList(); // get list result of the hotel
        int totalHotels = hotelList.size(); // count
        String sHotelPlural = totalHotels != 1 ? "Hotels" : "Hotel";
        String sSubtitle = getString(R.string.result_hotel_list, totalHotels, sHotelPlural); // get string format

        if(!mIsSubtitleVisible)
        {
            sSubtitle = null;
        }
        AppCompatActivity activityCompat = (AppCompatActivity) getActivity();
        activityCompat.getSupportActionBar().setSubtitle(sSubtitle);
    }
}
