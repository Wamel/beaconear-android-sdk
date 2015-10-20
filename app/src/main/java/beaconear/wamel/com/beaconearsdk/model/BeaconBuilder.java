package beaconear.wamel.com.beaconearsdk.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by Mauro on 19/09/2015.
 */
public class BeaconBuilder {

    private String apiKey;
    private String type;
    private JsonObject metadata;
    private transient double distance;

    public BeaconBuilder(String type, JsonElement metadata, String apiKey, double distance){
        this.type = type;
        this.metadata = metadata.getAsJsonObject();
        this.distance = distance;
        this.apiKey = apiKey;
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

    public Double getDouble(String key) {
        return this.metadata.getAsJsonPrimitive(key).getAsDouble();
    }

    public int getInt(String key) {
        return this.metadata.getAsJsonPrimitive(key).getAsInt();
    }

    public String getString(String key) {
        return this.metadata.getAsJsonPrimitive(key).getAsString();
    }

    public String getApiKey() {
        return apiKey;
    }
}
