package com.wamel.beaconear.model;

/**
 * Created by Mauro on 23/09/2015.
 */
public abstract class OnDetectionCallback<T>{

    public abstract void onDetected(T thingBuilder);

}
