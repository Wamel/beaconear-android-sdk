package model;

import com.google.gson.annotations.SerializedName;

import beaconear.wamel.com.beaconearsdk.model.Beacon;

/**
 * Created by User on 23/09/2015.
 */
public class MesaBeacon extends Beacon {

    @SerializedName("numero_mesa")
    private int numeroDeMesa;

    @SerializedName("carta_id")
    private int cartaId;

    public int getNumeroDeMesa() {
        return numeroDeMesa;
    }

    public int getCartaId() {
        return cartaId;
    }

    public void setNumeroDeMesa(int numeroDeMesa) {
        this.numeroDeMesa = numeroDeMesa;
    }

}
