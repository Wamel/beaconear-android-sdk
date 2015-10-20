package beaconear.wamel.com.beaconearsdk.model;

/**
 * Created by Mauro on 23/09/2015.
 */
public abstract class BeaconCallback<T>{

    public abstract void whenFound(T beacon);

}
