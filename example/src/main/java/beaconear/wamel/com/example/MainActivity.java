package beaconear.wamel.com.example;

import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONException;

import java.util.Collection;

import beaconear.wamel.com.beaconearsdk.core.Beaconear;
import beaconear.wamel.com.beaconearsdk.model.PaymentBeacon;


public class MainActivity extends ActionBarActivity implements BeaconConsumer {
    private BeaconManager mBeaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beaconear_manager_test);
    }
    public void onResume() {
        super.onResume();
        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        // Detect the main Eddystone-UID frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));
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
                    for(Beacon beacon : beacons)
                        showToast("The next beacon I see is " + beacon.getId1());
                }
            }
        });
        try {


            mBeaconManager.startMonitoringBeaconsInRegion(new Region("uniqueid1", null, null, null));
            mBeaconManager.startRangingBeaconsInRegion(new Region("uniqueid2", null, null, null));
        } catch (RemoteException e) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBeaconManager.unbind(MainActivity.this);
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable(){
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
