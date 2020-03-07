package com.lab.movietime.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class TimerService extends IntentService {

    public static volatile boolean shouldStop = false;
    public TimerService() {
        super(TimerService.class.getSimpleName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent intent1 = new Intent("com.lab.movietime.Service");
        intent1.putExtra("someName", "algostudio.net");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
        if(shouldStop) {
            stopSelf();
            return;
        }
    }
}
