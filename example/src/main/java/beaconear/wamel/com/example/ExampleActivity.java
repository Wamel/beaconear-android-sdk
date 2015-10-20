package beaconear.wamel.com.example;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import beaconear.wamel.com.beaconearsdk.core.Beaconear;
import beaconear.wamel.com.beaconearsdk.model.BeaconCallback;
import beaconear.wamel.com.beaconearsdk.model.BeaconBuilder;
import beaconear.wamel.com.beaconearsdk.model.InfoBeacon;
import beaconear.wamel.com.beaconearsdk.model.PaymentBeacon;
import beaconear.wamel.com.beaconearsdk.model.Region;
import beaconear.wamel.com.beaconearsdk.model.RegionCallback;
import model.EntradaBeacon;
import model.MesaBeacon;
import model.MyBeaconType;


public class ExampleActivity extends ActionBarActivity {

    private Beaconear beaconear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);


        String publicKey = "1a2b3c4d5e6f7g";
        beaconear = new Beaconear.Builder()
                .setContext(this)
                .setPublicKey(publicKey)
                .setPaymentBeaconCallback(new BeaconCallback<PaymentBeacon>() {
                    @Override
                    public void whenFound(PaymentBeacon beacon) {
                    }
                })
                .setRegionStateMonitoringCallback(new Region("0xf7826da6bc5b71e0893e"), new RegionCallback() {
                    @Override
                    public void whenEntered() {
                        showToast("Entraste a la región");
                    }

                    @Override
                    public void whenExited() {
                        showToast("Saliste de la región");
                    }
                })
                .setPaymentBeaconCallback(new BeaconCallback<PaymentBeacon>() {
                    @Override
                    public void whenFound(PaymentBeacon beacon) {
                        showToast(""+beacon.getAmount());
                    }
                })
                .addCustomizedBeaconCallback(MyBeaconType.MESA, new BeaconCallback<BeaconBuilder>() {
                    @Override
                    public void whenFound(BeaconBuilder beacon) {
                        MesaBeacon mesa = beacon.buildBeacon(MesaBeacon.class);
                        mesa.setNumeroDeMesa(9999);
                        Beaconear.save(mesa);
                    }
                })
                .addCustomizedBeaconCallback(MyBeaconType.ENTRADA, new BeaconCallback<BeaconBuilder>() {
                    @Override
                    public void whenFound(BeaconBuilder beacon) {
                        EntradaBeacon entrada = beacon.buildBeacon(EntradaBeacon.class);
                        showToast(""+entrada.getNombreLocal());
                    }
                })
                .build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        beaconear.startScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconear.stopScanning();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_example, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable(){
            public void run() {
                Toast.makeText(ExampleActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}
