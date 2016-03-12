package com.wamel.beaconear.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wamel.beaconear.core.LocalDataManager;
import com.wamel.beaconear.core.Settings;
import com.wamel.beaconear.model.TaggedThingBuilder;
import com.wamel.beaconear.rest.services.BeaconService;
import com.wamel.beaconear.util.HttpClientUtil;
import com.wamel.beaconear.util.JsonUtil;
import com.wamel.beaconear.util.NetworkUtils;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by mreverter on 21/2/16.
 */
public class LocalDataService extends Service {

    private static final String BEACONEAR_API_BASE_URL = "http://wamel.io:8080";
    private RestAdapter mRestAdapterBeaconearApi;
    @Override
    public void onCreate() {
        super.onCreate();
        mRestAdapterBeaconearApi = new RestAdapter.Builder()
                .setEndpoint(BEACONEAR_API_BASE_URL)
                .setLogLevel(Settings.RETROFIT_LOGGING)
                .setConverter(new GsonConverter(JsonUtil.getInstance().getGson()))
                .setClient(HttpClientUtil.getClient(this))
                .build();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String key = intent.getStringExtra("key");
        if(NetworkUtils.isNetworkAvailable(this)) {
            this.getBeaconBuilders(key);
        }
        return Service.START_STICKY;
    }

    private void getBeaconBuilders(String key) {
        BeaconService beaconService = mRestAdapterBeaconearApi.create(BeaconService.class);
        beaconService.getBuilderBeacons(key, new Callback<List<TaggedThingBuilder>>() {
            @Override
            public void success(List<TaggedThingBuilder> taggedThingBuilders, Response response) {
                updateLocalData(taggedThingBuilders);
                finishService();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.println(Log.ERROR, "getBeaconBu..-LocalDa..", error.getMessage());
            }
        });
    }

    //TODO: notificaciones
    /*private void notifyUpdate(String key) {
        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                new Intent(), // add this
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification n  = new Notification.Builder(this)
                .setContentTitle("Updated beaconear data")
                .setContentText("Data has been updated with key " + key)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }*/

    private void finishService() {
        this.stopSelf();
    }

    private void updateLocalData(List<TaggedThingBuilder> taggedThingBuilders) {
        LocalDataManager localDataManager = new LocalDataManager(this);
        localDataManager.updateLocalData(taggedThingBuilders);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
