/*
*	PROJECT: Trip Planner
*	FILE: VisibleFragment.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the VisibleFragment class which is a generic fragment that hides foreground notifications
*/

package com.gymlazy.tripplanner.Controller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 *  a generic fragment that hides foreground notifications
 */
public class VisibleFragment extends Fragment {
    private static final String TAG = "VisibleFragment";

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mOnShowNotification, filter,
                PollService.PERM_PRIVATE, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mOnShowNotification);
    }

    private BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // If we receive this, we're visible, so cancel
            // the notification
            Log.i(TAG, "canceling notification");
            setResultCode(Activity.RESULT_CANCELED);
        }
    };
}
