package beaconear.wamel.com.example;

import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONException;

import java.util.Collection;

public class BeaconTestActivity extends ActionBarActivity implements BeaconConsumer {
    private BeaconManager mBeaconManager;


    TextView beaconData;
    final String EDDYSTONE_LAYOUT = "s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19";
    final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_test);
        this.beaconData = (TextView) findViewById(R.id.beaconData);
    }
    public void onResume() {
        super.onResume();
        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        // Detect the main Eddystone-UID frame:

        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(EDDYSTONE_LAYOUT));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(IBEACON_LAYOUT));

        mBeaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        mBeaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                showToast("I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                showToast("I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                showToast("I have just switched from seeing/not seeing beacons: " + state);
            }
        });
        mBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    for(Beacon beacon : beacons) {
                        if(beacon.getServiceUuid() != 0xfeaa)
                            writeData("Uuid: "+beacon.getId1()+ " major: "+ beacon.getId2() + " minor: "+ beacon.getId3());

                    }
                }
            }
        });
        try {


            mBeaconManager.startMonitoringBeaconsInRegion(new Region("uniqueid1", null, null, null));
            mBeaconManager.startRangingBeaconsInRegion(new Region("uniqueid2", null, null, null));
        } catch (RemoteException e) {

        }
    }

    private void writeData(final String data) {
        runOnUiThread(new Runnable(){
            public void run() {
                beaconData.setText(data);
                Log.d("TAG!", data);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mBeaconManager.unbind(BeaconTestActivity.this);
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable(){
            public void run() {
                Toast.makeText(BeaconTestActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}