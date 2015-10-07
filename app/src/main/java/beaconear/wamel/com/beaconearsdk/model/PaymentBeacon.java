package beaconear.wamel.com.beaconearsdk.model;

import com.google.gson.JsonObject;

/**
 * Created by Mauro on 19/09/2015.
 */
public class PaymentBeacon extends Beacon {

    private Double amount;
    private String reference;

    public PaymentBeacon(BuilderBeacon builder) {
        super(builder);
        this.amount = metadata.getAsJsonPrimitive("amount").getAsDouble();
        this.reference = metadata.getAsJsonPrimitive("reference").getAsString();
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

}
