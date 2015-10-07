package beaconear.wamel.com.beaconearsdk.model;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Mauro on 23/09/2015.
 */
public abstract class BeaconCallback<T>{

    public abstract void whenImmediate(T beacon);

    public abstract void whenNear(T beacon);

    public abstract void whenFar(T beacon);
}
