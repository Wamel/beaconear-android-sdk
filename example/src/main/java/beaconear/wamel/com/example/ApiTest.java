package beaconear.wamel.com.example;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import beaconear.wamel.com.beaconearsdk.core.Beaconear;
import beaconear.wamel.com.beaconearsdk.model.Beacon;
import beaconear.wamel.com.beaconearsdk.model.BeaconType;
import beaconear.wamel.com.beaconearsdk.model.PaymentBeacon;


public class ApiTest extends ActionBarActivity {

    private Beaconear beaconear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_api_test);
    }

    @Override
    public void onResume() {
        super.onResume();
        beaconear = new Beaconear.Builder()
                .setContext(this)
                .setPublicKey("1a2b3c4d5e6f7g")
                .build();

        PaymentBeacon b = null;

        Toast.makeText(this, b.getAmount().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_api_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
