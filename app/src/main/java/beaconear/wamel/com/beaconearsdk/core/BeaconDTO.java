package beaconear.wamel.com.beaconearsdk.core;

import org.altbeacon.beacon.Beacon;

/**
 * Created by User on 08/10/2015.
 */
class BeaconDTO {

    private static final int EDDYSTONE_SERVICE_UUID = 0xfeaa;
    private static final int IBEACON_TYPE_CODE = 533;

    private String uuid;
    private String major;
    private String minor;
    private double distance;

    public BeaconDTO(Beacon beacon) {

        this.uuid = beacon.getId1().toString();
        this.major = beacon.getId2().toString();
        this.minor = "";
        if(beacon.getServiceUuid() != EDDYSTONE_SERVICE_UUID){
            this.minor = beacon.getId3().toString();
        }
        this.distance = beacon.getDistance();
    }


    public String getUuid() {
        return uuid;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public double getDistance() {
        return distance;
    }
}
