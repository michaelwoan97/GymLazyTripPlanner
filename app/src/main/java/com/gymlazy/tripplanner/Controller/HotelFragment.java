/*
*	PROJECT: Trip Planner
*	FILE: HotelFragment.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the HotelFragment class for displaying the detail about a hotel
*/

package com.gymlazy.tripplanner.Controller;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.HotelList;
import com.gymlazy.tripplanner.Model.databases.HotelCursorWrapper;
import com.gymlazy.tripplanner.R;
import com.gymlazy.tripplanner.Utils.ImageDownloader;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class HotelFragment extends Fragment {
    private ImageView mHotelImage;
    private TextView mHotelName;
    private TextView mHotelDescription;
    private ImageButton mImageButton;
    private Button mVisitBtn;
    private Button mBookBtn;
    private static final String HOTEL_ID = "hotel_id";
    private static final String TAG = "HotelFragment";
    private Hotel mHotel;
    private boolean mIsFavorite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        int iHotelID = (int) getArguments().getSerializable(HOTEL_ID);

        // proof of concept to use content provider to update data
        Uri hotelUri = Uri.parse("content://" + HotelsProvider.AUTHORITY + "/" + HotelsProvider.BASE_PATH);
        Cursor cursor = getContext().getContentResolver().query(hotelUri,null, "id = " + iHotelID, null, null);

        // check whether update successfully
        if(cursor.getCount() == 0)
        {
            Toast.makeText(getContext(), "Sorry can not retrieve the selected hotel!", Toast.LENGTH_SHORT)
                    .show();
        }
        mHotel = new HotelCursorWrapper(cursor).getHotel();

        // use singleton to retrieve hotel data from database
//        try {
//            mHotel = HotelList.get(this.getContext()).getHotel(iHotelID);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.hotel_fragment, container, false);

        mHotelImage = v.findViewById(R.id.hotel_image_detail);
        mHotelName = v.findViewById(R.id.hotel_name_detail);
        mHotelDescription = v.findViewById(R.id.hotel_description_detail);

        // check whether the hotel is existed
        if(mHotel != null)
        {
            // get image file of the hotel to display
            String root = Environment.getExternalStorageDirectory().toString(); // get external storage location
            String sFilePath = ImageDownloader.getImageNameFromURL(mHotel.getStringHotelImage(), root);

            // convert file to bitmap
            Bitmap hotelBitmap = BitmapFactory.decodeFile(sFilePath);
            mHotelImage.setImageBitmap(hotelBitmap);
            mHotelName.setText(mHotel.getHotelName() + " " + mHotel.getHotelId());
            mHotelDescription.setText(mHotel.getHotelDescription());
        }

        mIsFavorite = mHotel.isFavorite();

        mVisitBtn = v.findViewById(R.id.visit_website);
        mVisitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(mHotel.getHotelWebURL()));
                startActivity(i);
            }
        });

        mBookBtn = (Button) v.findViewById(R.id.book_btn_detail);
        mBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = HotelTicketActivity.newIntent(HotelFragment.this.getContext(), mHotel.getHotelId());
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.hotel_fragment_menu, menu);
        MenuItem favBtnItem = menu.findItem(R.id.fav_btn_menu_item);
        favBtnItem.setIcon(mIsFavorite ? R.drawable.ic_baseline_favorite_red_24 : R.drawable.ic_baseline_favorite_white_24);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.fav_btn_menu_item:
                mIsFavorite = !mIsFavorite;
                mHotel.setFavorite(mIsFavorite);

                // proof of concept to use content provider to update data
                Uri hotelUri = Uri.parse("content://" + HotelsProvider.AUTHORITY + "/" + HotelsProvider.BASE_PATH);
                int result = getContext().getContentResolver().update(hotelUri, HotelList.getContentValues(mHotel), "id = " + mHotel.getHotelId(), null);

                // check whether update successfully
                if(result == 0)
                {
                    Toast.makeText(getContext(), "Sorry update is not successful!", Toast.LENGTH_SHORT)
                            .show();
                }

                //use singleton to update hotel data in database
//                try {
//                    HotelList.get(getActivity()).updateFavoriteHotel(mHotel);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                Drawable dFavIcon = getResources().getDrawable( mIsFavorite ? R.drawable.ic_baseline_favorite_red_24 : R.drawable.ic_baseline_favorite_white_24);
                item.setIcon(dFavIcon);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.location_btn_menu_item:
                Intent i = TripMapsActivity.newIntent(getContext(), mHotel.getHotelId());
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }

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
