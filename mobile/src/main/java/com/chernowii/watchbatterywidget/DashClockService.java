package com.chernowii.watchbatterywidget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

/**
 * Created by Konrad Iturbe on 02/28/16.
 */
public class DashClockService extends DashClockExtension {
    public static final String DATA = "DATA";
    public static final String battery_data = "battery";
    public static final String charging_data = "battery";

    protected void onUpdateData(int reason) {
        String batteryLevel;
        String charging;
        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences(DATA, Context.MODE_PRIVATE); //1
        batteryLevel = settings.getString(battery_data, null); //2
        charging = settings.getString(charging_data, null); //2
        publishUpdate(new ExtensionData()
                .visible(true)
                .icon(R.drawable.dashclockicon)
                .status("Watch Battery for DashClock")
                .expandedTitle("Battery Level:" + batteryLevel)
                .expandedBody(charging));
    }
}
