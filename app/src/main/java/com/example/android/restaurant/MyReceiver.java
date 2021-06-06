package com.example.android.restaurant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

//receiver from kitchen
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context ct, Intent intent) {
        Bundle b = intent.getExtras();
        Intent it  = new Intent("receive-food");
        it.putExtra("food-index",b.getInt("index"));
        System.out.println("XXX");
        ct.sendBroadcast(it);
    }
}
