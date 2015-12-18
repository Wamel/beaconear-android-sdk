package beaconear.wamel.com.beaconearsdk.model;

/**
 * Created by Mauro on 19/09/2015.
 */
public class PaymentBeacon extends Beacon {

    private Double amount;
    private String reference;

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
