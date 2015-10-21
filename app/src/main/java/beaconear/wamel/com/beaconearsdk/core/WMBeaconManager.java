package beaconear.wamel.com.beaconearsdk.core;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;

import beaconear.wamel.com.beaconearsdk.model.Region;

/**
 * Created by Mauro on 24/09/2015.
 */
class WMBeaconManager implements BeaconConsumer {

    final String EDDYSTONE_LAYOUT = "s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19";
    final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private BeaconManager mBeaconManager;
    private Context context;
    private org.altbeacon.beacon.Region region;

    public WMBeaconManager(MonitorNotifier monitorNotifier, RangeNotifier rangeNotifier, Context context, Region region)
    {
        this.context = context;
        mBeaconManager = BeaconManager.getInstanceForApplication(this.context);
        // Detect the main Eddystone-UID frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(EDDYSTONE_LAYOUT));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(IBEACON_LAYOUT));
        mBeaconManager.setRangeNotifier(rangeNotifier);
        mBeaconManager.setMonitorNotifier(monitorNotifier);

        Identifier id1 = null;
        Identifier id2 = null;
        Identifier id3 = null;

        if(region!= null) {
            if (region.getUuid() != null && !region.getUuid().isEmpty())
                id1 = Identifier.parse(region.getUuid());
            if (region.getMajor() != null && !region.getMajor().isEmpty())
                id2 = Identifier.parse(region.getMajor());
            if (region.getMinor() != null && !region.getMinor().isEmpty())
                id3 = Identifier.parse(region.getMinor());
        }
        this.region = new org.altbeacon.beacon.Region(region.getName(), id1, id2, id3);

    }

    public void startScan() {
        mBeaconManager.bind(this);
    }

    public void stopScanning() {
        if(mBeaconManager != null && mBeaconManager.isBound(this))
            mBeaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        try {
            mBeaconManager.startRangingBeaconsInRegion(new org.altbeacon.beacon.Region("uniqueid2", null, null, null));
            if(this.region!= null && this.mBeaconManager.getMonitoringNotifier() != null) {
                if(this.region.getId1() != null)
                mBeaconManager.startMonitoringBeaconsInRegion(new org.altbeacon.beacon.Region(this.region.getUniqueId(), this.region.getId1(), this.region.getId2(), this.region.getId3()));
            }
        } catch (RemoteException e) {

        }
    }

    @Override
    public Context getApplicationContext() {
        return this.context;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        this.context.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return this.context.bindService(intent, serviceConnection, i);

    }

}
