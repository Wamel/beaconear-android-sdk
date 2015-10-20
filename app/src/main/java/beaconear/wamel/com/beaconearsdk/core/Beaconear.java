package beaconear.wamel.com.beaconearsdk.core;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import beaconear.wamel.com.beaconearsdk.model.BeaconCallback;
import beaconear.wamel.com.beaconearsdk.model.BeaconBuilder;
import beaconear.wamel.com.beaconearsdk.model.BeaconType;
import beaconear.wamel.com.beaconearsdk.model.InfoBeacon;
import beaconear.wamel.com.beaconearsdk.model.PaymentBeacon;
import beaconear.wamel.com.beaconearsdk.model.Region;
import beaconear.wamel.com.beaconearsdk.model.RegionCallback;
import beaconear.wamel.com.beaconearsdk.rest.services.BeaconService;
import beaconear.wamel.com.beaconearsdk.util.HttpClientUtil;
import beaconear.wamel.com.beaconearsdk.util.JsonUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
/**
 * Created by Mauro on 19/09/2015.
 */
public class Beaconear {

    public static final String KEY_TYPE_PUBLIC = "public_key";
    public static final String KEY_TYPE_PRIVATE = "private_key";

    //private static final String BEACONEAR_API_BASE_URL = "http://vps-1091345-x.dattaweb.com:8083";
    private static final String BEACONEAR_API_BASE_URL = "http://wamel.io:8080";

    private RestAdapter mRestAdapterBeaconearApi;

    private final Region mRegion;
    private final RegionCallback mRegionCallback;

    private BeaconCallback<PaymentBeacon> mPaymentBeaconCallback = null;
    private BeaconCallback<InfoBeacon> mInfoBeaconCallback = null;
    private HashMap<String, BeaconCallback<BeaconBuilder>> mTypeCallbackMap;

    private String mKey = null;
    private String mKeyType = null;
    private Context mContext = null;

    private WMBeaconManager mWMBeaconManager;

    private Beaconear(Builder builder) {

        this.mContext = builder.mContext;
        this.mKey = builder.mKey;
        this.mKeyType = builder.mKeyType;

        this.mPaymentBeaconCallback = builder.mPaymentBeaconCallback;
        this.mInfoBeaconCallback = builder.mInfoBeaconCallback;
        this.mTypeCallbackMap = builder.mTypeCallbackMap;
        this.mRegion = builder.mRegion;
        this.mRegionCallback = builder.mRegionCallback;

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
            public void didEnterRegion(org.altbeacon.beacon.Region region) {
                mRegionCallback.whenEntered();
            }

            @Override
            public void didExitRegion(org.altbeacon.beacon.Region region) {
                mRegionCallback.whenExited();
            }

            @Override
            public void didDetermineStateForRegion(int state, org.altbeacon.beacon.Region region) {

            }
        };

        RangeNotifier rangeNotifier = new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, org.altbeacon.beacon.Region region) {
                notifyBeaconsRanged(beacons);
            }
        };

        this.mWMBeaconManager = new WMBeaconManager(monitorNotifier, rangeNotifier, this.mContext, this.mRegion);
        this.mWMBeaconManager.startScan();
    }

    public void stopScanning() {
        this.mWMBeaconManager.stopScanning();
    }

    private void notifyBeaconsRanged(Collection<org.altbeacon.beacon.Beacon> beacons) {
        BeaconDTO beaconDTO = null;
        for(org.altbeacon.beacon.Beacon beacon : beacons){
            beaconDTO = new BeaconDTO(beacon);
            this.getBeaconBuilders(beaconDTO);
        }
    }

    private void getBeaconBuilders(final BeaconDTO beaconDTO) {

        BeaconService service = mRestAdapterBeaconearApi.create(BeaconService.class);

        service.getBuilderBeacons(this.mKey, beaconDTO.getUuid(), beaconDTO.getMajor(), beaconDTO.getMinor(), new Callback<List<BeaconBuilder>>() {
            @Override
            public void success(List<BeaconBuilder> beaconBuilders, Response response) {
                List<BeaconBuilder> builderBeaconsWithDistance = new ArrayList<>();

                for (BeaconBuilder builder : beaconBuilders) {
                    builderBeaconsWithDistance.add(new BeaconBuilder(builder.getType(), builder.getMetadata(), builder.getApiKey(), beaconDTO.getDistance()));
                }

                sendNotifications(builderBeaconsWithDistance);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void sendNotifications(List<BeaconBuilder> beaconBuilders) {

        for(BeaconBuilder beaconBuilder : beaconBuilders) {
            switch (beaconBuilder.getType()) {
                case BeaconType.PAYMENT:
                    if (mPaymentBeaconCallback != null) {
                            this.mPaymentBeaconCallback.whenFound(new PaymentBeacon(beaconBuilder));
                    }
                    break;

                case BeaconType.INFO:
                    if (mInfoBeaconCallback != null) {
                            this.mInfoBeaconCallback.whenFound(new InfoBeacon(beaconBuilder));
                    }
                    break;

                default:
                    BeaconCallback<BeaconBuilder> callback = this.mTypeCallbackMap.get(beaconBuilder.getType());
                    if (callback != null) {
                            callback.whenFound(beaconBuilder);
                    }
                    break;

            }
        }
    }

    public static class Builder {

        private Context mContext;
        private String mKey;
        private String mKeyType;

        private BeaconCallback<PaymentBeacon> mPaymentBeaconCallback;
        private BeaconCallback<InfoBeacon> mInfoBeaconCallback;

        private HashMap<String, BeaconCallback<BeaconBuilder>> mTypeCallbackMap;

        private Region mRegion;

        private RegionCallback mRegionCallback;

        public Builder() {
            mContext = null;
            mKey = null;
            mTypeCallbackMap = new HashMap<>();
        }

        public Builder setContext(Context context) {

            if (context == null) throw new IllegalArgumentException("context is null");
            this.mContext = context;
            return this;
        }

        public Builder setKey(String key, String keyType) {

            this.mKey = key;
            this.mKeyType = keyType;
            return this;
        }

        public Builder setPrivateKey(String key) {

            this.mKey = key;
            this.mKeyType = Beaconear.KEY_TYPE_PRIVATE;
            return this;
        }

        public Builder setPublicKey(String key) {

            this.mKey = key;
            this.mKeyType = Beaconear.KEY_TYPE_PUBLIC;
            return this;
        }

        public Beaconear build() {

            if (this.mContext == null) throw new IllegalStateException("context is null");
            if (this.mKey == null) throw new IllegalStateException("key is null");
            if (this.mKeyType == null) throw new IllegalStateException("key type is null");
            if ((!this.mKeyType.equals(Beaconear.KEY_TYPE_PRIVATE)) &&
                    (!this.mKeyType.equals(Beaconear.KEY_TYPE_PUBLIC))) throw new IllegalArgumentException("invalid key type");
            return new Beaconear(this);
        }

        public Builder setPaymentBeaconCallback(BeaconCallback<PaymentBeacon> beaconCallback) {
            this.mPaymentBeaconCallback = beaconCallback;
            return this;
        }

        public Builder setInfoBeaconCallback(BeaconCallback<InfoBeacon> beaconCallback) {
            this.mInfoBeaconCallback = beaconCallback;
            return this;
        }

        public Builder addCustomizedBeaconCallback(String type, BeaconCallback<BeaconBuilder> beaconCallback) {
            this.mTypeCallbackMap.put(type, beaconCallback);
            return this;
        }

        public Builder setRegionStateMonitoringCallback(Region region, RegionCallback regionCallback) {
            this.mRegion = region;
            this.mRegionCallback = regionCallback;
            return this;
        }
    }

    public static void save(beaconear.wamel.com.beaconearsdk.model.Beacon beacon){
        String type = beacon.getType();
        String apiKey =  beacon.getApiKey();

        String json = JsonUtil.getInstance().toJson(beacon);
        JsonObject metadata = (JsonObject)new JsonParser().parse(json);
        metadata.remove("distance");
        metadata.remove("type");
        metadata.remove("api_key");

        JsonObject beaconJson = new JsonObject();
        beaconJson.addProperty("api_key", apiKey);
        beaconJson.addProperty("type", type);
        beaconJson.add("metadata", metadata);

        RestAdapter restAdapterBeaconearApi = new RestAdapter.Builder()
                .setEndpoint(BEACONEAR_API_BASE_URL)
                .setLogLevel(Settings.RETROFIT_LOGGING)
                .setConverter(new GsonConverter(JsonUtil.getInstance().getGson()))
                .build();

        BeaconService service = restAdapterBeaconearApi.create(BeaconService.class);

        service.updateBeacon(apiKey, beacon.getUuid(), beacon.getMajor(), beacon.getMinor(), type, beaconJson, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {}

            @Override
            public void failure(RetrofitError error) {

            }
        });



    }

}
