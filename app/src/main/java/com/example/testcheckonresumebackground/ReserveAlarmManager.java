package com.example.testcheckonresumebackground;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class ReserveAlarmManager{

    public void reserve(Context context, int reserveTime) {
        Intent intent = new Intent(context, ReservedUpdateReceiver.class);
        intent.setAction(Constants.ACTION_RESERVE_RECEIVE);

        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE);


        alarmManager.cancel(sender);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + reserveTime);
        long alarmTime = calendar.getTimeInMillis();

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, sender);


    }
}
