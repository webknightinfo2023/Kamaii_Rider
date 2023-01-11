package com.kamaii.rider.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.kamaii.rider.LocationService;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.preferences.SharedPrefrence;

public class Restarter extends BroadcastReceiver {

    SharedPrefrence preference;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
//        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
        preference = SharedPrefrence.getInstance(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, LocationService.class));
        } else {
            if (!preference.getBooleanValue(Consts.TESTING_ACCOUNT)){

                context.startService(new Intent(context, LocationService.class));
            }
        }
    }
}