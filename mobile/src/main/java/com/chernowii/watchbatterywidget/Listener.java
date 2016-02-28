package com.chernowii.watchbatterywidget;

import android.app.Notification;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.apps.dashclock.api.ExtensionData;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Arrays;

/**
 * Created by Konrad Iturbe on 02/28/16.
 */
public class Listener extends WearableListenerService {
    public static final String DATA = "DATA";
    public static final String battery_data = "battery";
    public static final String charging_data = "charging";
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        //Toast.makeText(getApplicationContext(),messageEvent.getPath(),Toast.LENGTH_SHORT).show();
        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.watch_widget);
        ComponentName thisWidget = new ComponentName(context, WatchWidget.class);
        if (messageEvent.getPath().equals("Charging")){
            String chargingstatus = messageEvent.getPath();
            remoteViews.setTextViewText(R.id.charging,chargingstatus);
            int batterystatus = Integer.parseInt(new String(messageEvent.getData()));
            remoteViews.setTextViewText(R.id.battery, batterystatus + "%");
            SharedPreferences settings;
            SharedPreferences.Editor editor;
            settings = getApplicationContext().getSharedPreferences(DATA, Context.MODE_PRIVATE); //1
            editor = settings.edit(); //2

            editor.putInt(battery_data, batterystatus);
            editor.putString(charging_data, chargingstatus);
            editor.commit();
            Intent serviceIntent = new Intent(this, DashClockService.class);
            startService(serviceIntent);
            //Toast.makeText(getApplicationContext(), "WORKS", Toast.LENGTH_LONG).show();
        }
        if (messageEvent.getPath().equals("Not Charging")) {
            String chargingstatus = messageEvent.getPath();
            remoteViews.setTextViewText(R.id.charging, chargingstatus);
            int batterystatus = Integer.parseInt(new String(messageEvent.getData()));
            if (batterystatus == 100){
                Notification n  = new Notification.Builder(this)
                        .setContentTitle("Watch Battery Fully Charged!")

                        .setSmallIcon(R.mipmap.ic_launcher).build();


                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                notificationManager.notify(0, n);
            }
            remoteViews.setTextViewText(R.id.battery, batterystatus + "%");
            SharedPreferences settings;
            SharedPreferences.Editor editor;
            settings = getApplicationContext().getSharedPreferences(DATA, Context.MODE_PRIVATE); //1
            editor = settings.edit(); //2

            editor.putInt(battery_data, batterystatus);
            editor.putString(charging_data, chargingstatus);
            editor.commit();
            Intent serviceIntent = new Intent(this, DashClockService.class);
            startService(serviceIntent);
        }


        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

    }

}
