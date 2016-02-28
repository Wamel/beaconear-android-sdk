package model;

import com.google.gson.annotations.SerializedName;

import com.wamel.beaconear.model.TaggedThing;

/**
 * Created by User on 23/09/2015.
 */
public class Mesa extends TaggedThing {

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
