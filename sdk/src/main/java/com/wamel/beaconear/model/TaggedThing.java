package com.wamel.beaconear.model;
/**
 * Created by Mauro on 19/09/2015.
 */
public abstract class TaggedThing {

    private double distance;

    public double getDistance() {
        return distance;
    }

    public DistanceState getState() {
        return DistanceState.getState(this.distance);
    }

    public boolean isImmediate() {
        return DistanceState.IMMEDIATE == this.getState();
    }

    public boolean isNear() {
        return DistanceState.NEAR == this.getState();
    }

    public boolean isFar() {
        return DistanceState.FAR == this.getState();
    }
}
