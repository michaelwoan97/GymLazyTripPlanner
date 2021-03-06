/*
*	PROJECT: Trip Planner
*	FILE: HotelListFragment.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the HotelListFragment class used for displaying the list of hotel based on the destination
*/

package com.gymlazy.tripplanner.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.gymlazy.tripplanner.Controller.HotelListFragment.FetchHotelDB.downloadImgCallback;
import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.HotelList;
import com.gymlazy.tripplanner.Model.databases.HotelBaseHelper;
import com.gymlazy.tripplanner.Model.databases.HotelCursorWrapper;
import com.gymlazy.tripplanner.R;
import com.gymlazy.tripplanner.TripPlannerActivity;
import com.gymlazy.tripplanner.Utils.ImageDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HotelListFragment extends VisibleFragment {
    private RecyclerView mRecyclerView;
    private HotelAdapter mHotelAdapter;
    private LinearLayout mProgressIndicator;
    private boolean mIsSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final String TAG = "HotelListFragment";
    private static final int REQUEST_STORAGE_CODE = 111;
    private ArrayList<Hotel> mHotelArrayList;

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
        mRecyclerView.setVisibility(View.GONE);

        mProgressIndicator = v.findViewById(R.id.progress_indicator);
        // configures the adapter appropriately on your RecyclerView
        setupAdapter();

        // declare the interface callback in the constructor
        //  Permission check
        if (!hasPermissions(this.getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})) {
            // Permission ask
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_CODE);
        } else {
            // if permission is already granted than load url
            new FetchHotelDB(new downloadImgCallback() {
                @Override
                public void downloadImgFromDB() {
                    // invoke another asyncTask
                    new FetchHotelImage().execute();
                }
            }).execute(this.getContext());
        }


        try {
            updateUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.trip_planner_menu, menu);
        menu.add(0, R.id.show_subtitle, Menu.NONE, getString(R.string.show_sub_menu_item)).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, R.id.menu_item_toggle_polling, Menu.NONE, getString(R.string.start_polling));

        if(mIsSubtitleVisible)
        {
            menu.findItem(R.id.show_subtitle).setTitle(R.string.hide_sub_menu_item);
        }
        else
        {
            menu.findItem(R.id.show_subtitle).setTitle(R.string.show_sub_menu_item);
        }

        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if (PollService.isServiceAlarmOn(getActivity())) {
            toggleItem.setTitle(R.string.stop_polling);
        } else {
            toggleItem.setTitle(R.string.start_polling);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.show_subtitle:
                mIsSubtitleVisible = !mIsSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                try {
                    updateSubtitle();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
                getActivity().invalidateOptionsMenu();
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

        public void bind(Hotel hHotel) throws IOException {
            mHotel = hHotel;
            mHotelName.setText(mHotel.getHotelName());
            mHotelDescription.setText(mHotel.getHotelDescription());

            // get image file of the hotel to display
            String root = Environment.getExternalStorageDirectory().toString(); // get external storage location
            String sFilePath = ImageDownloader.getImageNameFromURL(mHotel.getStringHotelImage(), root);

            // convert file to bitmap
            Bitmap hotelBitmap = BitmapFactory.decodeFile(sFilePath);
            mHotelImage.setImageBitmap(hotelBitmap);

            mIsFavorite = mHotel.isFavorite();
            mFavorite.setSelected(mIsFavorite ? true : false);
            mFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

                    //try {
//                        // use singleton to update hotel favorite
////                        HotelList.get(getActivity()).updateFavoriteHotel(mHotel);
//

//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
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

    private void setupAdapter() {
        //This confirms that the fragment has been attached to an activity, and in turn that getActivity() will not be null.
        if (isAdded()) {
            mRecyclerView.setAdapter(new HotelAdapter(mHotelArrayList));
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
            try {
                holder.bind(hotel);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    private void updateUI() throws IOException {
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
    private void updateSubtitle() throws IOException {
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

    // Function for check permission already granted or not
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"Permission is denied");
                    return false;
                }
            }
        }
        Log.d(TAG,"Permission is granted");
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_CODE) {
            if(grantResults.length != 0 ){
                for (int result : grantResults){
                    if(result != PackageManager.PERMISSION_GRANTED){
                        // if permission is not granted then only fetch hotel databse
                        new FetchHotelDB(new downloadImgCallback() {
                            @Override
                            public void downloadImgFromDB() {
                                return;
                            }
                        }).execute(this.getContext());
                        return;
                    }
                    Log.d(TAG,"Permission " + result + " is granted");
                }
                // if permission is already granted then fetch hotel db and images
                new FetchHotelDB(new downloadImgCallback() {
                    @Override
                    public void downloadImgFromDB() {
                        // invoke another asyncTask
                        new FetchHotelImage().execute();
                    }
                }).execute(this.getContext());
            }
        }

    }


    public static class FetchHotelDB extends AsyncTask<Context, Void, Void> {

        //  attach to activities that implement its interface.
        // Once attached, it could use methods in its hosting activities
        private downloadImgCallback mDownloadImgCallback;
        public FetchHotelDB(downloadImgCallback downloadImgCallback) {
            mDownloadImgCallback = downloadImgCallback; // reference who is its parents
        }


        @Override
        protected Void doInBackground(Context... contexts) {
            try {
                HotelList.get(contexts[0]).getHotelList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // chain another asyncTask by using callback interface delegate the work to the parent
            mDownloadImgCallback.downloadImgFromDB();
        }

        /**
         * the purpose of this interface is to delegate the work that involves in downloading image from
         * database to its parent
         */
        public interface downloadImgCallback{ // Interface can not be instantiated that is why it is static
            void downloadImgFromDB();
        }
    }

    private class FetchHotelImage extends AsyncTask<Void, Void, ArrayList<Hotel>> {

        @Override
        protected ArrayList<Hotel> doInBackground(Void... voids) {
            ArrayList<Hotel> hotelList = null;
            try {
                // using singleton to get data
//                hotelList = HotelList.get(getActivity()).getHotelList();

                // proof of concept using content provider to retrieve hotel data
                Uri hotelUri = Uri.parse("content://" + HotelsProvider.AUTHORITY + "/" + HotelsProvider.BASE_PATH);
                HotelCursorWrapper hotelCursorWrapper = new HotelCursorWrapper(getContext().getContentResolver().query(hotelUri, null, null,null, null));
                hotelList = HotelList.get(getContext()).getHotelList(hotelCursorWrapper); // singleton process hotel data

                // loop through each hotel to download and save the hotel image in a file
                int count = 1;
                for(Hotel hotel : hotelList){
                    ImageDownloader.downloadImgFromURL(hotel.getStringHotelImage());
                    Log.d(TAG, "Downloaded Image " + count);
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return hotelList;
        }

        /**
         * the purpose of this function is after downloading image into file update the main UI
         * @param hotels
         */
        @Override
        protected void onPostExecute(ArrayList<Hotel> hotels) {
            mHotelArrayList = hotels;
            setupAdapter();

            // save the first result of hotel data
            QueryPreferences.setPrefLastHotelId(getContext(),mHotelArrayList.get(0).getHotelId());

            // hide progress bar and display hotel data
            mProgressIndicator.setVisibility(ViewGroup.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
