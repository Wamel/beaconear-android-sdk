package beaconear.wamel.com.beaconearsdk.model;

/**
 * Created by Mauro on 19/09/2015.
 */
public class InfoBeacon extends Beacon {
    private String description;

    public InfoBeacon(BeaconBuilder builder) {
        super(builder);
        this.description = builder.getString("description");

    }

    public String getDescription() {
        return description;
    }

}
