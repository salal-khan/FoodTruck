package com.codiansoft.foodtruck.firebaseSettings;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.codiansoft.foodtruck.DrawerActivity;
import com.codiansoft.foodtruck.OrderTiming;
import com.codiansoft.foodtruck.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.codiansoft.foodtruck.Utils.AppConstants.OrderTime;

/**
 * Created by Ali on 26-Oct-16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String ordetime = "2016-04-21 07:03:39";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, " Message data: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, " Message Body: " + remoteMessage.getNotification().getBody());


            Object obj = remoteMessage.getData().get("deliveryTime");

            if (obj != null) {
                try {
                    ordetime = obj.toString();

                    OrderTime = ordetime;

                } catch (Exception e) {
                    ordetime = "failed";
                    e.printStackTrace();
                }
            }

        }
        Log.d(TAG, " Message Bodyssss: " + OrderTime);
        String click_action = remoteMessage.getNotification().getClickAction();
        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), click_action, ordetime);

    }


    //This method is only generating push notification
    private void sendNotification(String Title, String messageBody, String click_action, String ordertime) {
        Intent intent = new Intent(click_action);
        intent.putExtra("deliveryTime", ordertime);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)


                .setSmallIcon(R.drawable.ft_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ft_logo))

                .setContentTitle(Title)
                .setContentText(messageBody)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
