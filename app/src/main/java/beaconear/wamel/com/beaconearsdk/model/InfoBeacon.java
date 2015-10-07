package beaconear.wamel.com.beaconearsdk.model;

import com.google.gson.JsonObject;

/**
 * Created by Mauro on 19/09/2015.
 */
public class InfoBeacon extends Beacon {
    private String description;

    public InfoBeacon(BuilderBeacon builder) {
        super(builder);
        this.description = metadata.getAsJsonPrimitive("description").getAsString();

    }

    public String getDescription() {
        return description;
    }

}
