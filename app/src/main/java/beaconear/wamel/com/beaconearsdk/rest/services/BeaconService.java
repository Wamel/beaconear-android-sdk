package beaconear.wamel.com.beaconearsdk.rest.services;

import java.util.List;

import beaconear.wamel.com.beaconearsdk.model.BuilderBeacon;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Mauro on 19/09/2015.
 */
public interface BeaconService {
    @GET("/beacons")
    void getBuilderBeacon(@Query("public_key") String publicKey, @Query("namespace") String uuid, @Query("instance") String instance, Callback<List<BuilderBeacon>> callback);
}
