package com.chernowii.watchbatterywidget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class WatchActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Button setOn = (Button) findViewById(R.id.start);
                setOn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StartService();
                    }
                });
            }
        });
    }
    public void StartService(){
        startService(new Intent(this, BatteryService.class));
    }
}
