package beaconear.wamel.com.beaconearsdk.model;

import com.google.gson.JsonElement;

/**
 * Created by Mauro on 19/09/2015.
 */
public class BuilderBeacon {

    private double distance;
    private String type;
    private JsonElement metadata;

    public BuilderBeacon(String type, double distance, JsonElement metadata){
        this.type = type;
        this.distance = distance;
        this.metadata = metadata;
    }

    public String getType() {
        return type;
    }

    public JsonElement getMetadata() {
        return metadata;
    }

    public double getDistance() {
        return distance;
    }

    public BeaconState getState() {
        return BeaconState.getState(this.getDistance());
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

}
