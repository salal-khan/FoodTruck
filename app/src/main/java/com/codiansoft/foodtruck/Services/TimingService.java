package com.codiansoft.foodtruck.Services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import com.codiansoft.foodtruck.R;

/**
 * Created by salal on 4/20/2017.
 */

public class TimingService extends Service {

    public long counter = 30000;
    private Context ctx;
    private Activity localActivity;


    private NotificationManager nm;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        declareNotification();
        timer.start();
        //Toast.makeText(this, "Timer is :" + counter, Toast.LENGTH_LONG).show();
        //showNotification();
    }


    public void getActivity(Activity activity) {
        localActivity = activity;
    }

    //count to end the game
    public CountDownTimer timer = new CountDownTimer(30000, 1000) {

        public void onTick(long millisUntilFinished) {
            counter = millisUntilFinished / 1000;
        }

        public void onFinish() {
            counter = 0;


            //Kill the game
            int i = android.os.Process.myPid();
            android.os.Process.killProcess(i);

        }

    };

    /*
     * Show a notification while this service is running
     */
    public void declareNotification() {
        //Declare a new notification

        Notification notification = new Notification(R.drawable.man_icon, "A New Notification", System.currentTimeMillis());

        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;

        Intent intent = new Intent(this, TimingService.class);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
        //notification.setLatestEventInfo(this, "herp", "counter: " + counter, activity);


        //This is clearly not 1337, but a good joke
        startForeground(1337, notification);

    }

}
