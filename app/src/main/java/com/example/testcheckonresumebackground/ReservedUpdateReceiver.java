package com.example.testcheckonresumebackground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReservedUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "ReservedUpdateReceiver onReceive:" + intent.getAction());

        if (intent.getAction().equals(Constants.ACTION_RESERVE_RECEIVE)){
            Log.d("Receiver", "### ReservedUpdateReceiver");
            Intent reservedUpdateIntent = new Intent(context, MUpdateService.class);
            context.startService(reservedUpdateIntent);
        }
    }
}
