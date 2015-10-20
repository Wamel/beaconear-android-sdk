package beaconear.wamel.com.beaconearsdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mauro on 19/09/2015.
 */
public abstract class Beacon {

    @SerializedName("api_key")
    private String apiKey;
    private String type;
    private String uuid;
    private String major;
    private String minor;
    private double distance;

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
