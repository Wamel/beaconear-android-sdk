package beaconear.wamel.com.beaconearsdk.rest.services;

import com.google.gson.JsonObject;
import java.util.List;

import beaconear.wamel.com.beaconearsdk.model.BeaconBuilder;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Query;

/**
 * Created by Mauro on 19/09/2015.
 */
public interface BeaconService {
    @GET("/beacon")
    void getBuilderBeacons(@Query("api_key") String publicKey, @Query("uuid") String uuid, @Query("major") String major, @Query("minor") String minor, Callback<List<BeaconBuilder>> callback);

    @PUT("/beacon")
    void updateBeacon(@Query("api_key") String publicKey, @Query("uuid") String uuid, @Query("major") String major, @Query("minor") String minor, @Query("type") String type, @Body JsonObject beacon, Callback<Response> callback);

}
