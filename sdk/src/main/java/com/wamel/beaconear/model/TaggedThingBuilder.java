package com.wamel.beaconear.model;

import com.google.gson.JsonObject;

import com.wamel.beaconear.util.JsonUtil;

/**
 * Created by Mauro on 19/09/2015.
 */
public class TaggedThingBuilder {

    private double distance;
    private JsonObject metadata;

    public void setDistance(double distance) {
        this.distance = distance;
        this.metadata.addProperty("distance", distance);
    }

    public String getType() {
        return this.metadata.getAsJsonPrimitive("type").getAsString();
    }

    public String getUuid() {
        return this.metadata.getAsJsonPrimitive("uuid").getAsString();
    }

    public String getMajor() {
        return this.metadata.getAsJsonPrimitive("major").getAsString();
    }

    public String getMinor() {
        return this.metadata.getAsJsonPrimitive("minor").getAsString();
    }

    public <T extends TaggedThing> T buildTaggedThing(Class<T> tClass)
    {
        return new GenericBuilder<T>().build(metadata, tClass);
    }

    public double getDistance() {
        return distance;
    }

    private class GenericBuilder<T extends TaggedThing>{
        public T build(JsonObject json, Class<T> tClass){
            return JsonUtil.getInstance().fromJson(json.toString(), tClass);
        }
    }
}
