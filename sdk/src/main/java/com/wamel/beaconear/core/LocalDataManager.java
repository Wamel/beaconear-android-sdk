package com.wamel.beaconear.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.wamel.beaconear.model.TaggedThing;
import com.wamel.beaconear.model.TaggedThingBuilder;
import com.wamel.beaconear.services.LocalDataService;
import com.wamel.beaconear.util.FileUtils;
import com.wamel.beaconear.util.JsonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by mreverter on 21/2/16.
 */
public class LocalDataManager {

    private final String DATABASE_FILENAME = "database";
    private Context mContext;
    private static Date mLastUpdateDate;

    public LocalDataManager(Context context)
    {
        this.mContext = context;
    }

    public boolean hasData() {
        File file = new File(mContext.getFilesDir(), DATABASE_FILENAME);
        return file.isFile();
    }
    public List<TaggedThingBuilder> getBeaconBuilders(String uuid, String major, String minor) {

        String json = FileUtils.getStringFromFile(mContext, DATABASE_FILENAME);
        List<TaggedThingBuilder> builders = new ArrayList<>();
        if (json != null && !json.isEmpty()) {
            builders = JsonUtil.getInstance().listFromJson(json, TaggedThingBuilder.class);
        }

        builders = filterBuildersByIdentifiers(builders, uuid, major, minor);

        return builders;
    }

    private List<TaggedThingBuilder> filterBuildersByIdentifiers(List<TaggedThingBuilder> builders, String uuid, String major, String minor) {
        List<TaggedThingBuilder> filteredBuilders = new ArrayList<>();
        for(TaggedThingBuilder builder : builders) {
            if(builder.getUuid().equals(uuid) && builder.getMajor().equals(major) && builder.getMinor().equals(minor)) {
                filteredBuilders.add(builder);
            }
        }
        return filteredBuilders;
    }

    public void scheduleDataUpdate(String key) {
        Intent intent = new Intent(mContext, LocalDataService.class);
        intent.putExtra("key", key);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent);
    }

    public void updateLocalData(List<TaggedThingBuilder> taggedThingBuilders) {
        String json = JsonUtil.getInstance().toJson(taggedThingBuilders);
        boolean succeeded = FileUtils.saveStringInFile(mContext, DATABASE_FILENAME, json);
        if(succeeded) {
            mLastUpdateDate = (new GregorianCalendar()).getTime();
        }
    }

    public static Date getLastUpdateDate() {
        return mLastUpdateDate;
    }
}
