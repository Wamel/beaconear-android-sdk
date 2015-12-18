package beaconear.wamel.com.beaconearsdk.model;

import com.google.gson.JsonObject;

import beaconear.wamel.com.beaconearsdk.util.JsonUtil;

/**
 * Created by Mauro on 19/09/2015.
 */
public class BeaconBuilder {

    private double distance;
    private JsonObject metadata;

    public void setDistance(double distance) {
        this.metadata.addProperty("distance", distance);
    }

    public String getBeaconType() {
        return this.metadata.getAsJsonPrimitive("type").getAsString();
    }

    public <T extends Beacon> T buildBeacon(Class<T> tClass)
    {
        return new GenericBuilder<T>().build(metadata, tClass);
    }

    private class GenericBuilder<T extends Beacon>{
        public T build(JsonObject json, Class<T> tClass){
            T beacon = JsonUtil.getInstance().fromJson(json.toString(), tClass);
            return beacon;
        }
    }
}
