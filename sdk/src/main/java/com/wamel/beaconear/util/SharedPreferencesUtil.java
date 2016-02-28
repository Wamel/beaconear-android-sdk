package com.wamel.beaconear.util;

import android.content.Context;
import android.content.SharedPreferences;

import beaconear.wamel.com.beaconearsdk.R;

/**
 * Created by mreverter on 28/2/16.
 */
public class SharedPreferencesUtil {
    public static void saveKey(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.last_api_key), key);
        editor.apply();
    }
    public static String getKey(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.last_api_key), "");
    }
}
