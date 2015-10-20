package model;

import beaconear.wamel.com.beaconearsdk.model.Beacon;
import beaconear.wamel.com.beaconearsdk.model.BeaconBuilder;

/**
 * Created by User on 24/09/2015.
 */
public class EntradaBeacon extends Beacon {
    private String nombreLocal;

    public EntradaBeacon(BeaconBuilder builder) {
        super(builder);
        this.nombreLocal = builder.getString("nombre_local");
    }

    public String getNombreLocal() {
        return nombreLocal;
    }
}
