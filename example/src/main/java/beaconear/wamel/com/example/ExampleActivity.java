package beaconear.wamel.com.example;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import beaconear.wamel.com.beaconearsdk.core.Beaconear;
import beaconear.wamel.com.beaconearsdk.model.BeaconCallback;
import beaconear.wamel.com.beaconearsdk.model.BuilderBeacon;
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


        beaconear = new Beaconear.Builder()
                .setContext(this)
                .setPublicKey("1a2b3c4d5e6f7g")
                .setPaymentBeaconCallback(new BeaconCallback<PaymentBeacon>() {
                    @Override
                    public void whenImmediate(PaymentBeacon beacon) {

                    }

                    @Override
                    public void whenNear(PaymentBeacon beacon) {

                    }

                    @Override
                    public void whenFar(PaymentBeacon beacon) {

                    }
                })
                .setInfoBeaconCallback(new BeaconCallback<InfoBeacon>() {
                    @Override
                    public void whenImmediate(InfoBeacon beacon) {

                    }

                    @Override
                    public void whenNear(InfoBeacon beacon) {

                    }

                    @Override
                    public void whenFar(InfoBeacon beacon) {

                    }
                })
                .addCustomizedBeaconCallback(MyBeaconType.ENTRADA, new BeaconCallback<BuilderBeacon>() {
                    @Override
                    public void whenImmediate(BuilderBeacon beacon) {
                        EntradaBeacon entrada = new EntradaBeacon(beacon);
                        showToast("Bienvenido a " + entrada.getNombreLocal());
                    }

                    @Override
                    public void whenNear(BuilderBeacon beacon) {
                        EntradaBeacon entrada = new EntradaBeacon(beacon);
                        showToast("Bienvenido a " + entrada.getNombreLocal());
                    }

                    @Override
                    public void whenFar(BuilderBeacon beacon) {
                    }
                })
                .addCustomizedBeaconCallback(MyBeaconType.MESA, new BeaconCallback<BuilderBeacon>() {
                    @Override
                    public void whenImmediate(BuilderBeacon beacon) {
                        MesaBeacon mesa = new MesaBeacon(beacon);
                        showToast("Se encuentra en la mesa " + mesa.getNumeroDeMesa());
                    }

                    @Override
                    public void whenNear(BuilderBeacon beacon) {
                        MesaBeacon mesa = new MesaBeacon(beacon);
                    }

                    @Override
                    public void whenFar(BuilderBeacon beacon) {
                        MesaBeacon mesa = new MesaBeacon(beacon);
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable(){
            public void run() {
                Toast.makeText(ExampleActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
