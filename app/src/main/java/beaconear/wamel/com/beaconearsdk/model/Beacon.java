package beaconear.wamel.com.beaconearsdk.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import beaconear.wamel.com.beaconearsdk.util.JsonUtil;
/**
 * Created by Mauro on 19/09/2015.
 */
public abstract class Beacon {

    private String apiKey;
    private String type;
    private String uuid;
    private String major;
    private String minor;
    private double distance;

    public Beacon(BeaconBuilder builder){
        this.type = builder.getType();
        this.distance = builder.getDistance();

        this.uuid = builder.getString("uuid");
        this.minor = builder.getString("minor");
        this.major = builder.getString("major");
        this.apiKey = builder.getApiKey();
        this.distance = builder.getDistance();
    }

    public String getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public double getDistance() {
        return distance;
    }

    public BeaconState getState() {
        return BeaconState.getState(this.distance);
    }

    public boolean isImmediate() {
        return BeaconState.IMMEDIATE == this.getState();
    }

    public boolean isNear() {
        return BeaconState.NEAR == this.getState();
    }

    public boolean isFar() {
        return BeaconState.FAR == this.getState();
    }

    public String getApiKey() {
        return apiKey;
    }
}
