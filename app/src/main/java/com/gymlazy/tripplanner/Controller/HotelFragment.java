package com.gymlazy.tripplanner.Controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.HotelList;
import com.gymlazy.tripplanner.R;

import org.w3c.dom.Text;

public class HotelFragment extends Fragment {
    private ImageView mHotelImage;
    private TextView mHotelName;
    private TextView mHotelDescription;
    private static final String HOTEL_ID = "hotel_id";
    private Hotel mHotel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.hotel_fragment, container, false);

        mHotelImage = v.findViewById(R.id.hotel_image_detail);
        mHotelName = v.findViewById(R.id.hotel_name_detail);
        mHotelDescription = v.findViewById(R.id.hotel_description_detail);

        int iHotelID = (int) getArguments().getSerializable(HOTEL_ID);
        mHotel = HotelList.get(this.getContext()).getHotel(iHotelID);
        // check whether the hotel is existed
        if(mHotel != null)
        {
            mHotelImage.setImageResource(mHotel.getHotelImage());
            mHotelName.setText(mHotel.getHotelName() + " " + mHotel.getHotelId());
            mHotelDescription.setText(mHotel.getHotelDescription());
        }

        return v;
    }

    /*
     *	Function: Fragment newInstance(int iHotelID)
     *	Description:
     *       The purpose of this function is to create a new fragment for HotelFragment. The reason using
     *       fragment argument is because encapsulation. Fragment should not know anything about hosting activity.
     *       the activity can know something specific about fragment
     *	Parameter: int iHotelId : hotel id
     *	Return: Fragment: return fragment
     */
    public static Fragment newInstance(int iHotelID){
        // create bundle
        Bundle b = new Bundle();
        b.putSerializable(HOTEL_ID, iHotelID);

        // create fragment and put arguments before it attach to activity
        HotelFragment hotelFragment = new HotelFragment();
        hotelFragment.setArguments(b);
        return hotelFragment;
    }
}
