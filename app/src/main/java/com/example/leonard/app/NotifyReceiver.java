package com.example.leonard.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class NotifyReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
            context.startService(new Intent(context, NotifyService.class));
    }
}
