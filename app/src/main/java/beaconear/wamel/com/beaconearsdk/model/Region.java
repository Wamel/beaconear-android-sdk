package beaconear.wamel.com.beaconearsdk.model;

/**
 * Created by Mauro on 24/09/2015.
 */
public class Region {

    private String uuid;

    private String major;

    private String minor;

    private String name;

    public Region(String name, String uuid, String major, String minor){
        this.name = name;
        this.uuid = uuid;
        this.major = major;
        this. minor = minor;
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

    public String getName() {
        return name;
    }
}
