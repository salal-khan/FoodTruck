package com.codiansoft.foodtruck;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OrderTiming extends AppCompatActivity {
    TextView txtSecond;
    TextView txtMinute;
    TextView txtHour;
    TextView txtDay;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_timing);
        Object values = getIntent().getExtras().get("deliveryTime");
        String ordertime = (String) values;


        //Toast.makeText(getApplicationContext(), "" +  "Key:  Value: " + values, Toast.LENGTH_LONG).show();
        Log.d("MainActivity: ", "Key: Value: " + ordertime);



        /*
        getting All Firebase notification data background click method
         */
//        if (getIntent().getExtras() != null) {
//            for (String key : getIntent().getExtras().keySet()) {
//                Object value = getIntent().getExtras().get(key);
//                Toast.makeText(getApplicationContext(), "" +  "Key: " + key + " Value: " + value, Toast.LENGTH_LONG).show();
//                Log.d("MainActivity: ", "Key: " + key + " Value: " + value);
//            }
//        }


        txtHour = (TextView) findViewById(R.id.cntHour);


        timerStart(ordertime);


    }

    private void timerStart(String date) {


        long timer = FestCountdownTimer(date);
        new CountDownTimer(timer, 1000) {

            public void onTick(long millisUntilFinished) {

                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);


                txtHour.setText(days + " " + hours + " " + minutes + " " + seconds);

            }

            public void onFinish() {
                txtHour.setText("done!");
            }
        }.start();

    }


    public long FestCountdownTimer(String dates) {


        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dates);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int second = calendar.get(Calendar.SECOND);
            int minute = calendar.get(Calendar.MINUTE);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int monthDay = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);


            long intervalMillis;
            Time futureTime = new Time();

            // Set date to future time
            futureTime.set(second, minute, hour, monthDay, month, year);
            futureTime.normalize(true);
            long futureMillis = futureTime.toMillis(true);

            Time timeNow = new Time();

            // Set date to current time
            timeNow.setToNow();
            timeNow.normalize(true);
            long nowMillis = timeNow.toMillis(true);

            // Subtract current milliseconds time from future milliseconds time to retrieve interval
            return intervalMillis = futureMillis - nowMillis;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


}