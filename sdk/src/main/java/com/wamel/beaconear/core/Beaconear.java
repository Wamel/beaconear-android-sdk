package com.wamel.beaconear.core;

import android.content.Context;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.wamel.beaconear.callbacks.OnDetectionCallback;
import com.wamel.beaconear.callbacks.TaggedThingsRegionCallback;
import com.wamel.beaconear.model.TaggedThingBuilder;
import com.wamel.beaconear.model.TaggedThingsRegionMonitor;
import com.wamel.beaconear.rest.services.BeaconService;
import com.wamel.beaconear.util.HttpClientUtil;
import com.wamel.beaconear.util.JsonUtil;
import com.wamel.beaconear.util.NetworkUtils;
import com.wamel.beaconear.util.SharedPreferencesUtil;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
/**
 * Created by Mauro on 19/09/2015.
 */
public class Beaconear {

    private static final String BEACONEAR_API_BASE_URL = "http://wamel.io:8080";

    private RestAdapter mRestAdapterBeaconearApi;

    private HashMap<String, OnDetectionCallback<TaggedThingBuilder>> mTypeCallbackMap;
    private List<TaggedThingsRegionMonitor> mTaggedThingsRegionMonitors;

    private String mKey = null;
    private Context mContext = null;

    private WMBeaconManager mWMBeaconManager;
    private LocalDataManager mLocalDataManger;

    private Beaconear(Builder builder) {

        this.mContext = builder.mContext;
        this.mKey = builder.mKey;
        this.mTaggedThingsRegionMonitors = builder.mTaggedThingsRegionMonitors;
        this.mTypeCallbackMap = builder.mTypeCallbackMap;

        mLocalDataManger = new LocalDataManager(mContext);

        if(builder.mEnableLocalDataUpdate) {
            enableLocalDataUpdate();
        }

        mRestAdapterBeaconearApi = new RestAdapter.Builder()
                .setEndpoint(BEACONEAR_API_BASE_URL)
                .setLogLevel(Settings.RETROFIT_LOGGING)
                .setConverter(new GsonConverter(JsonUtil.getInstance().getGson()))
                .setClient(HttpClientUtil.getClient(this.mContext))
                .build();
    }

    public void startScan() {

        MonitorNotifier monitorNotifier = new MonitorNotifier() {
            @Override
            public void didEnterRegion(org.altbeacon.beacon.Region region) {}

            @Override
            public void didExitRegion(org.altbeacon.beacon.Region region) {}

            @Override
            public void didDetermineStateForRegion(int state, org.altbeacon.beacon.Region region) {
                if(state == 0) {
                    notifyRegionExitedToMonitors(region);
                }
            }
        };

        RangeNotifier rangeNotifier = new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, org.altbeacon.beacon.Region region) {
                processRangedBeacons(beacons);
            }
        };

        this.mWMBeaconManager = new WMBeaconManager(monitorNotifier, rangeNotifier, this.mContext);

        this.bindBeaconManagerToMonitors();

        this.mWMBeaconManager.startScan();
    }

    private void notifyRegionExitedToMonitors(org.altbeacon.beacon.Region region) {
        for(TaggedThingsRegionMonitor monitor : mTaggedThingsRegionMonitors) {
            monitor.handleExitedRegion(region);
        }
    }

    private void bindBeaconManagerToMonitors() {
        for(TaggedThingsRegionMonitor monitor : mTaggedThingsRegionMonitors) {
            monitor.bindBeaconManager(this.mWMBeaconManager);
        }
    }
    public void stopScanning() {
        this.mWMBeaconManager.stopScanning();
    }

    public void enableLocalDataUpdate() {
        SharedPreferencesUtil.saveKey(mContext, this.mKey);
        this.mLocalDataManger.scheduleDataUpdate(this.mKey);
    }

    public void disableLocalDataUpdate() {
        //TODO: set off alarms.
    }

    private void processRangedBeacons(Collection<Beacon> beacons) {
        for(Beacon beacon : beacons){
            this.getBeaconBuilders(beacon);
        }
    }

    private void getBeaconBuilders(final Beacon beacon) {

        String uuid = beacon.getId1() != null ? beacon.getId1().toString() : "";
        String major = beacon.getId2() != null ? beacon.getId2().toString() : "";
        String minor = beacon.getIdentifiers().size() == 3 ? beacon.getId3().toString() : "";
        double distance = beacon.getDistance();

        if(NetworkUtils.isNetworkAvailable(mContext)) {
            retrieveBeaconBuildersOnline(uuid, major, minor, distance);
        } else {
            retrieveBeaconBuildersOffline(uuid, major, minor, distance);
        }
    }

    private void retrieveBeaconBuildersOffline(String uuid, String major, String minor, double distance) {
        if(mLocalDataManger.hasData()) {
            List<TaggedThingBuilder> taggedThingBuilders = mLocalDataManger.getBeaconBuilders(uuid, major, minor);
            for (TaggedThingBuilder builder : taggedThingBuilders) {
                builder.setDistance(distance);
            }
            sendNotificationsByTypes(taggedThingBuilders);
        }
    }

    private void retrieveBeaconBuildersOnline(String uuid, String major, String minor, double distance) {
        BeaconService service = mRestAdapterBeaconearApi.create(BeaconService.class);

        final Double currentDistance = distance;
        service.getBuilderBeacons(this.mKey, uuid, major, minor, new Callback<List<TaggedThingBuilder>>() {
            @Override
            public void success(List<TaggedThingBuilder> taggedThingBuilders, Response response) {
                for (TaggedThingBuilder builder : taggedThingBuilders) {
                    builder.setDistance(currentDistance);
                }
                sendNotificationsByTypes(taggedThingBuilders);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.println(Log.ERROR, "getBeaconBuilders", error.getMessage());
            }
        });
    }

    private void sendNotificationsByTypes(List<TaggedThingBuilder> taggedThingBuilders) {
        for(TaggedThingBuilder taggedThingBuilder : taggedThingBuilders) {
            sendRangingNotifications(taggedThingBuilder);
            sendRegionMonitoringNotificationsForType(taggedThingBuilder);
        }
    }

    private void sendRangingNotifications(TaggedThingBuilder taggedThingBuilder) {
        String type = taggedThingBuilder.getType();
        OnDetectionCallback<TaggedThingBuilder> callback = this.mTypeCallbackMap.get(type);
        if (callback != null) {
            callback.onDetected(taggedThingBuilder);
        }
    }

    private void sendRegionMonitoringNotificationsForType(TaggedThingBuilder thingBuilder) {
        for(TaggedThingsRegionMonitor monitor : this.mTaggedThingsRegionMonitors) {
            monitor.handleThingFound(thingBuilder);
        }
    }

    public static class Builder {

        private Context mContext;
        private String mKey;

        private HashMap<String, OnDetectionCallback<TaggedThingBuilder>> mTypeCallbackMap;
        private List<TaggedThingsRegionMonitor> mTaggedThingsRegionMonitors;

        private boolean mEnableLocalDataUpdate;

        public Builder() {
            mContext = null;
            mKey = null;
            mTypeCallbackMap = new HashMap<>();
            mTaggedThingsRegionMonitors = new ArrayList<>();
        }

        public Builder setContext(Context context) {

            if (context == null) throw new IllegalArgumentException("context is null");
            this.mContext = context;
            return this;
        }

        public Builder setPublicKey(String key) {

            this.mKey = key;
            return this;
        }

        public Beaconear build() {

            if (this.mContext == null) throw new IllegalStateException("context is null");
            if (this.mKey == null) throw new IllegalStateException("key is null");

            return new Beaconear(this);
        }

        public Builder addOnDetectionCallbackForType(String type, OnDetectionCallback<TaggedThingBuilder> onDetectionCallback) {
            this.mTypeCallbackMap.put(type, onDetectionCallback);
            return this;
        }

        public Builder enableLocalDataUpdate() {
            this.mEnableLocalDataUpdate = true;
            return this;
        }

        public Builder addTaggedThingsRegionMonitoring(List<String> typesToRange, String regionName, TaggedThingsRegionCallback taggedThingRegionCallback) {
            this.mTaggedThingsRegionMonitors.add(new TaggedThingsRegionMonitor(typesToRange, regionName, taggedThingRegionCallback));
            return this;
        }

        public Builder addTaggedThingsRegionMonitoring(String type, String regionName, TaggedThingsRegionCallback taggedThingRegionCallback) {
            List<String> typesToRange = new ArrayList<>();
            typesToRange.add(type);
            this.mTaggedThingsRegionMonitors.add(new TaggedThingsRegionMonitor(typesToRange, regionName, taggedThingRegionCallback));
            return this;
        }
    }
}
