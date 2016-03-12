package com.wamel.beaconear.model;

import com.wamel.beaconear.callbacks.TaggedThingsRegionCallback;
import com.wamel.beaconear.core.WMBeaconManager;

import java.util.List;

/**
 * Created by mreverter on 11/3/16.
 */
public class TaggedThingsRegionMonitor {

    private final List<String> monitoredTypes;
    private final TaggedThingsRegionCallback callback;
    private final String regionName;
    private boolean inRegion;
    private TaggedThingBuilder lastMonitoredThingFound;
    private WMBeaconManager beaconManager;
    private org.altbeacon.beacon.Region currentMonitoredRegion;

    public TaggedThingsRegionMonitor(List<String> monitoredTypes, String regionName, TaggedThingsRegionCallback callback) {
        this.regionName = regionName;
        this.inRegion = false;
        this.monitoredTypes = monitoredTypes;
        this.callback = callback;
    }

    public void handleThingFound(TaggedThingBuilder thingBuilder) {
        if(this.isMonitoringType(thingBuilder.getType()))
        {
            if(!inRegion) {
                inRegion = true;
                callback.whenEntered(this.regionName);
            } else {
                updateRegionForThing(thingBuilder);
            }
        }
    }

    private void updateRegionForThing(TaggedThingBuilder thingBuilder) {
        if(hasLastThingFound() && monitoredThingChanged(thingBuilder)) {
            Region regionToStopMonitoring = new Region(this.regionName, lastMonitoredThingFound.getUuid(), lastMonitoredThingFound.getMajor(), lastMonitoredThingFound.getMinor());
            this.beaconManager.stopMonitoringRegion(regionToStopMonitoring);
        }
        if(monitoredThingChanged(thingBuilder)) {
            Region regionToStartMonitoring = new Region(this.regionName, thingBuilder.getUuid(), thingBuilder.getMajor(), thingBuilder.getMinor());
            this.currentMonitoredRegion = this.beaconManager.startMonitoringRegion(regionToStartMonitoring);
            this.lastMonitoredThingFound = thingBuilder;
        }
    }

    private boolean monitoredThingChanged(TaggedThingBuilder foundThing) {
        return lastMonitoredThingFound == null
                || !lastMonitoredThingFound.getUuid().equals(foundThing.getUuid())
                || !lastMonitoredThingFound.getMajor().equals(foundThing.getMajor())
                || !lastMonitoredThingFound.getMinor().equals(foundThing.getMinor());
    }

    private boolean hasLastThingFound() {
        return lastMonitoredThingFound != null;
    }

    public void bindBeaconManager(WMBeaconManager beaconManager) {
        this.beaconManager = beaconManager;
    }

    public void handleExitedRegion(org.altbeacon.beacon.Region region) {
        if(isMonitoringRegion(region)) {
            String uuid = region.getId1() != null ? region.getId1().toString() : "";
            String major = region.getId2() != null ? region.getId2().toString() : "";
            String minor = region.getId3() != null ? region.getId3().toString() : "";
            this.beaconManager.stopMonitoringRegion(new Region(this.regionName, uuid, major, minor));
            this.inRegion = false;
            callback.whenExited(this.regionName);
        }
    }

    private boolean isMonitoringType(String type) {
        return monitoredTypes.contains(type);
    }

    private boolean isMonitoringRegion(org.altbeacon.beacon.Region region) {

        if(region != null && currentMonitoredRegion != null) {
            String id1 = region.getId1() != null ? region.getId1().toString() : "";
            String id2 = region.getId2() != null ? region.getId2().toString() : "";
            String id3 = region.getId3() != null ? region.getId3().toString() : "";

            String currentId1 = currentMonitoredRegion.getId1() != null ? currentMonitoredRegion.getId1().toString() : "";
            String currentId2 = currentMonitoredRegion.getId2() != null ? currentMonitoredRegion.getId2().toString() : "";
            String currentId3 = currentMonitoredRegion.getId3() != null ? currentMonitoredRegion.getId3().toString() : "";

            return (id1.equals(currentId1)
                    && id2.equals(currentId2)
                    && id3.equals(currentId3));
        } else {
            return false;
        }
    }
}
