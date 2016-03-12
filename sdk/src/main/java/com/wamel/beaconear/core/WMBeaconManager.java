package com.wamel.beaconear.core;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;

import com.wamel.beaconear.model.Region;

/**
 * Created by Mauro on 24/09/2015.
 */
public class WMBeaconManager implements BeaconConsumer {

    final String EDDYSTONE_LAYOUT = "s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19";
    final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private BeaconManager mBeaconManager;
    private Context context;

    public WMBeaconManager(MonitorNotifier monitorNotifier, RangeNotifier rangeNotifier, Context context)
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
    }

    private org.altbeacon.beacon.Region getAltBeaconRegionFromWMRegion(Region region) {
        if(region != null) {
            Identifier id1 = null;
            Identifier id2 = null;
            Identifier id3 = null;
            if (region.getUuid() != null && !region.getUuid().isEmpty())
                id1 = Identifier.parse(region.getUuid());
            if (region.getMajor() != null && !region.getMajor().isEmpty())
                id2 = Identifier.parse(region.getMajor());
            if (region.getMinor() != null && !region.getMinor().isEmpty())
                id3 = Identifier.parse(region.getMinor());
            return new org.altbeacon.beacon.Region(region.getName(), id1, id2, id3);
        } else {
            return null;
        }
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
            mBeaconManager.startRangingBeaconsInRegion(new org.altbeacon.beacon.Region("all", null, null, null));
        } catch (RemoteException e) {
            Log.println(Log.ERROR, "onBeaconServiceConnect", e.getMessage());
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

    public org.altbeacon.beacon.Region startMonitoringRegion(Region regionToStartMonitoring) {
        org.altbeacon.beacon.Region newMonitoredRegion = getAltBeaconRegionFromWMRegion(regionToStartMonitoring);
        try {
            mBeaconManager.startMonitoringBeaconsInRegion(newMonitoredRegion);
        } catch (RemoteException e) {
            Log.println(Log.ERROR, "startMonitoringRegion", e.getMessage());
        }
        return newMonitoredRegion;
    }

    public void stopMonitoringRegion(Region regionToStopMonitoring) {
        org.altbeacon.beacon.Region stoppedMonitorRegion = getAltBeaconRegionFromWMRegion(regionToStopMonitoring);
        try {
            mBeaconManager.stopRangingBeaconsInRegion(stoppedMonitorRegion);
        } catch (RemoteException e) {
            Log.println(Log.ERROR, "stopMonitoringRegion", e.getMessage());
        }
    }
}
