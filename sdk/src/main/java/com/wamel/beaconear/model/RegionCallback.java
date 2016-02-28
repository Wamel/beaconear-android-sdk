package com.wamel.beaconear.model;

/**
 * Created by Mauro on 24/09/2015.
 */
public abstract class RegionCallback {

    public abstract void whenEntered(Region region);

    public abstract void whenExited(Region region);
}
