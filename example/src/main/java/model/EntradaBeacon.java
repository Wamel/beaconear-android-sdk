package model;

import beaconear.wamel.com.beaconearsdk.model.Beacon;
import beaconear.wamel.com.beaconearsdk.model.BuilderBeacon;

/**
 * Created by User on 24/09/2015.
 */
public class EntradaBeacon extends Beacon {
    private String nombreLocal;

    public EntradaBeacon(BuilderBeacon builder) {
        super(builder);
        this.nombreLocal = metadata.getAsJsonPrimitive("nombre_local").getAsString();

    }

    public String getNombreLocal() {
        return nombreLocal;
    }
}
