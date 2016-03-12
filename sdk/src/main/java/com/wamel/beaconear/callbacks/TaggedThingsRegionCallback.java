package com.wamel.beaconear.callbacks;

/**
 * Created by mreverter on 11/3/16.
 */
public interface TaggedThingsRegionCallback {
    void whenEntered(String regionName);
    void whenExited(String regionName);
}
