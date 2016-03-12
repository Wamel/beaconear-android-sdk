package com.wamel.beaconear.callbacks;

/**
 * Created by Mauro on 23/09/2015.
 */
public interface OnDetectionCallback<T>{

    void onDetected(T thingBuilder);

}
