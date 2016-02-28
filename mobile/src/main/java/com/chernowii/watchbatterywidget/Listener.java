package com.chernowii.watchbatterywidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Konrad Iturbe on 02/28/16.
 */
public class Listener extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        //Toast.makeText(getApplicationContext(),messageEvent.getPath(),Toast.LENGTH_SHORT).show();
        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.watch_widget);
        ComponentName thisWidget = new ComponentName(context, WatchWidget.class);
        String batterystatus = messageEvent.getPath();
        String chargingstatus = null;
        remoteViews.setTextViewText(R.id.battery,batterystatus + "%");
        remoteViews.setTextViewText(R.id.charging,chargingstatus);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }
}
