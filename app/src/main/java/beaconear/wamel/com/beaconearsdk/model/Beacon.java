package beaconear.wamel.com.beaconearsdk.model;

import com.google.gson.JsonObject;

/**
 * Created by Mauro on 19/09/2015.
 */
public abstract class Beacon {

    protected JsonObject metadata;
    private String type;
    private String uuid;
    private String major;
    private String minor;
    private double distance;

    public Beacon(BuilderBeacon builder){
        this.type = builder.getType();
        this.distance = builder.getDistance();

        JsonObject metadata = builder.getMetadata().getAsJsonObject();
        this.uuid = metadata.getAsJsonPrimitive("uuid").getAsString();
        this.minor = metadata.getAsJsonPrimitive("minor").getAsString();
        this.major = metadata.getAsJsonPrimitive("major").getAsString();
        this.distance = builder.getDistance();
        this.metadata = metadata;
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

}
