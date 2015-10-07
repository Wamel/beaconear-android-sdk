package model;

import com.google.gson.JsonObject;

import beaconear.wamel.com.beaconearsdk.model.Beacon;
import beaconear.wamel.com.beaconearsdk.model.BuilderBeacon;

/**
 * Created by User on 23/09/2015.
 */
public class MesaBeacon extends Beacon {
    private int numeroDeMesa;
    private int cartaId;

    public MesaBeacon(BuilderBeacon builder) {
        super(builder);
        this.numeroDeMesa = metadata.getAsJsonPrimitive("numero_mesa").getAsInt();
        this.cartaId = metadata.getAsJsonPrimitive("carta_id").getAsInt();

    }

    public int getNumeroDeMesa() {
        return numeroDeMesa;
    }

    public int getCartaId() {
        return cartaId;
    }
}
