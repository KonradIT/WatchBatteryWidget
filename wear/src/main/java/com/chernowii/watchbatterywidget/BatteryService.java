package com.chernowii.watchbatterywidget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Konrad Iturbe on 02/28/16.
 */
public class BatteryService extends Service {
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public String batteryLevel = "";
    public String charging = "";
    private static final String TAG = "NotificationReceiver";
    GoogleApiClient mGoogleApiClient;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {

                    }
                    @Override
                    public void onConnectionSuspended(int cause) {

                    }
                }).build();
        mGoogleApiClient.connect();
        Toast.makeText(this, "Sending battery status!", Toast.LENGTH_LONG).show();
        sendBatteryStatus();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                sendBatteryStatus();
                handler.postDelayed(runnable, 120000);
            }
        };

        handler.postDelayed(runnable, 120000);

    }
    private void getNodes() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(
                new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        HashSet<String> results = new HashSet<String>();
                        for (Node node : getConnectedNodesResult.getNodes()) {
                            results.add(node.getId());
                            Log.d(TAG, node.getId().toString());
                        }
                        Log.d(TAG, results.toString());
                        sendMessageApi(results);
                    }
                }
        );
    }

    private void sendMessageApi(Collection<String> nodes) {
        for (String node : nodes) {
            Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, node, batteryLevel, charging.getBytes()).setResultCallback(
                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if (!sendMessageResult.getStatus().isSuccess()) {


                            } else {

                                try {
                                    finalize();
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }

                        }
                    }
            );
        }
    }

    private void sendBatteryStatus() {
        batteryLevel = String.valueOf(getBatteryLevel());
        charging = String.valueOf(isConnected(this));
        getNodes();
    }
    public float getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }


        float battery = ((float)level / (float)scale) * 100.0f;
        return battery;
    }
    public static boolean isConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }
    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        sendBatteryStatus();
    }

}
