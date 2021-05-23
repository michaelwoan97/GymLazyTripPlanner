package com.gymlazy.tripplanner.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.HotelList;
import com.gymlazy.tripplanner.R;

import java.io.IOException;
import java.util.List;

public class TripMapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String EXTRA_HOTEL_ID = "hotelID";
    private static final String TAG = "TripMapsActivity";
    private Hotel mHotel;
    private GoogleMap mMap;

    public static Intent newIntent(Context packageContext, int iHotelId){
        Intent i = new Intent(packageContext, TripMapsActivity.class);
        i.putExtra(EXTRA_HOTEL_ID, iHotelId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_maps);

        int iHotelID = (int) getIntent().getSerializableExtra(EXTRA_HOTEL_ID);
        try {
            mHotel = HotelList.get(this).getHotel(iHotelID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            List<Address> hotelLocationMarker = new Geocoder(this).getFromLocationName(mHotel.getHotelAddress(), 1);
            Log.d(TAG, hotelLocationMarker + "\n");

            // check whether the list is not empty
            if(hotelLocationMarker.isEmpty()){
                Toast.makeText(this, "Sorry can not find the coordinate of the " + mHotel.getHotelName() + "!", Toast.LENGTH_SHORT).show();
                return;
            }

            Address hotelLocation = hotelLocationMarker.get(0);
            // check whether the hotel has latitude and longitude
            if(!hotelLocation.hasLatitude() && !hotelLocation.hasLongitude() ){
                Toast.makeText(this, "Sorry the " + mHotel.getHotelName() + " does not has coordinates", Toast.LENGTH_SHORT).show();
            } else {
                // Add a marker in Sydney and move the camera
                LatLng hoteLat = new LatLng(hotelLocation.getLatitude(), hotelLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(hoteLat).title(mHotel.getHotelName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hoteLat, 18));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}