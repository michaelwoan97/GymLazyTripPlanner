package com.gymlazy.tripplanner.Controller;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.RemoteViews;

import com.gymlazy.tripplanner.Model.Trip;
import com.gymlazy.tripplanner.R;
import com.gymlazy.tripplanner.TripPlannerActivity;
import com.gymlazy.tripplanner.TripPlannerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TripPlannerWidget extends AppWidgetProvider {
    private static final String TAG = "TripPlannerWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetID : appWidgetIds)
        {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.trip_planner_widget);

            // get event ID of the newly created trip
            int iEventID = QueryPreferences.getPrefEventID(context);
            Log.d(TAG, "The event ID for the newly created trip is: " + iEventID);
            // Construct an Intent object to open calendar content provider to view events
            Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, iEventID);
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(uri);

            Log.d(TAG, "Intent is clicked");
            // In widget we are not allowing to use intents as usually. We have to use PendingIntent instead of 'startActivity'
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Here the basic operations the remote view can do.
            views.setOnClickPendingIntent(R.id.trip_planner_widget, pendingIntent);

            // estimate the different between begin and end date
            SimpleDateFormat df = new SimpleDateFormat("E MMM d yyyy", Locale.getDefault());

            // can not use singleton because it maybe killed when the process is killed
//            Trip trip = Trip.get(TripPlannerFragment.this.getContext());
//            trip.setStartDate(df.format(mStartDate));
//            trip.setEndDate(df.format(mEndDate));

            String sStartDate = QueryPreferences.getPrefStartDate(context);
            String sEndDate = QueryPreferences.getPrefEndDate(context);

            if(sStartDate != null && sEndDate != null)
            {
                try {
                    long lStartDate = df.parse(sStartDate).getTime();
                    long lEndDate = df.parse(sEndDate).getTime();
                    long lTimeRemain = lEndDate - lStartDate;

                    new CountDownTimer(lTimeRemain, 1000) {

                        public void onTick(long millisUntilFinished) {
                            // convert time remaining to format of day:hours:minutes:seconds
                            int day = (int) TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                            long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - (day *24);
                            long minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - (TimeUnit.MILLISECONDS.toHours(millisUntilFinished)* 60);
                            long second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) *60);
                            String sTimeRemains = String.format("%d DAYS: %d HRS: %02d MINS: %02d SECS", day, hours, minute, second);
                            Log.d(TAG, sTimeRemains);
                            views.setTextViewText(R.id.time_count_down, sTimeRemains);
                            // Instruct the widget manager to update the widget
                            appWidgetManager.updateAppWidget(appWidgetID, views);
                        }

                        public void onFinish() {
                            views.setTextViewText(R.id.time_count_down,"done!");
                            // Instruct the widget manager to update the widget
                            appWidgetManager.updateAppWidget(appWidgetID, views);
                        }
                    }.start();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }



            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetID, views);
        }


    }

}
