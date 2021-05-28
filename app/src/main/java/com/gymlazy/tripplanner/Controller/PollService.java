/*
*	PROJECT: Trip Planner
*	FILE: PollService.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the PollService class used for creating a service for checking whether a new hotel has been added to the database
*/

package com.gymlazy.tripplanner.Controller;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gymlazy.tripplanner.Model.Hotel;
import com.gymlazy.tripplanner.Model.HotelList;
import com.gymlazy.tripplanner.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PollService extends IntentService {
    private static final String TAG = "PollService";

    // Set interval to 1 minute
    private static final long POLL_INTERVAL_MS = TimeUnit.SECONDS.toMillis(3);

    public static final String ACTION_SHOW_NOTIFICATION =
            "com.gymlazy.tripplanner.SHOW_NOTIFICATION";

    public static final String PERM_PRIVATE =
            "com.gymlazy.tripplanner.PRIVATE";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";


    public static Intent newIntent(Context packageContext){
        return new Intent(packageContext, PollService.class);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(!isNetworkAvailableAndConnected())
        {
            return;
        }

        // get the stored query & last hotel ID
        String query = QueryPreferences.getStoredQuery(this);
        int iHotelID = QueryPreferences.getPrefLastHotelId(this);

        // fetch database
        try {
            ArrayList<Hotel> hotelArrayList = HotelList.get(this).getHotelList();

            // check whether the list is null
            if(hotelArrayList.size() == 0)
            {
                return;
            }

            // generate ID randomly from 0->10
            int iRandomID = getRandomNumber(0, ( hotelArrayList.size() - 1));

            // compare with the last hotel ID, if difference store the latest one
            if(iHotelID != iRandomID)
            {
                QueryPreferences.setPrefLastHotelId(this, iRandomID);

                // notification
                Resources resources = getResources(); // get recourse
                Intent i = HotelListActivity.newIntent(this); // intent to fire
                PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0); // the pending intent

                Notification notification = new NotificationCompat.Builder(this)
                        .setTicker(resources.getString(R.string.new_hotel_title))
                        .setSmallIcon(android.R.drawable.ic_menu_report_image)
                        .setContentTitle(resources.getString(R.string.new_hotel_title))
                        .setContentText(resources.getString(R.string.new_hotel_text, query))
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .build();

                showBackgroundNotification(0, notification);

            }

            Log.d(TAG, "Received an Intent: " + intent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * check the network connection
     * @return
     */
    private Boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); // get connectivity service

        Boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null ; // check active network info
        Boolean isNetworkConnected = isNetworkAvailable &&
                                    cm.getActiveNetworkInfo().isConnected(); // check whether active network is connected

        return isNetworkConnected;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * set service alarm
     * @param packageContext
     * @param isOn
     */
    public static void setServiceAlarm(Context packageContext, boolean isOn){
        Intent i = PollService.newIntent(packageContext);
        PendingIntent pi = PendingIntent.getService(packageContext, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)
                packageContext.getSystemService(Context.ALARM_SERVICE);

        if(isOn){
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }

        QueryPreferences.setAlarmOn(packageContext, isOn);
    }

    /**
     * check whether the service alarm is on
     * @param packageContext
     * @return null means that the alarm is not set
     */
    public static boolean isServiceAlarmOn(Context packageContext){
        Intent i = PollService.newIntent(packageContext);
        PendingIntent pi = PendingIntent
                .getService(packageContext, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    private void showBackgroundNotification(int requestCode, Notification notification){
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE, requestCode);
        i.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(i, PERM_PRIVATE, null, null,
                Activity.RESULT_OK, null, null);
    }
}
