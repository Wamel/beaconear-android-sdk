package com.wamel.beaconear.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wamel.beaconear.core.LocalDataManager;
import com.wamel.beaconear.util.SharedPreferencesUtil;

public class ResetLocalDataUpdateReceiver extends BroadcastReceiver {
    public ResetLocalDataUpdateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = SharedPreferencesUtil.getKey(context);
        if(!key.isEmpty()) {
            LocalDataManager localDataManager = new LocalDataManager(context);
            localDataManager.scheduleDataUpdate(key);
        }
    }
}
