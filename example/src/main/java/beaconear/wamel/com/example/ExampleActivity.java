package beaconear.wamel.com.example;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wamel.beaconear.callbacks.TaggedThingsRegionCallback;
import com.wamel.beaconear.core.Beaconear;
import com.wamel.beaconear.callbacks.OnDetectionCallback;
import com.wamel.beaconear.model.TaggedThingBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import model.BlueSource;
import model.GreenSource;
import model.MyTaggedTypes;
import model.RedSource;


public class ExampleActivity extends ActionBarActivity {

    private Beaconear beaconear;
    private int red = 0;
    private int green = 0;
    private int blue = 0;

    private RelativeLayout layout;
    private TextView redText;
    private TextView greenText;
    private TextView blueText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        layout = (RelativeLayout) findViewById(R.id.colorLayout);
        redText = (TextView) findViewById(R.id.redText);
        greenText = (TextView) findViewById(R.id.greenText);
        blueText = (TextView) findViewById(R.id.blueText);

        String publicKey = "1a2b3c4d5e6f7g";

        List<String> typesToRange = new ArrayList<>();
        typesToRange.add(MyTaggedTypes.RED);
        typesToRange.add(MyTaggedTypes.GREEN);

        beaconear = new Beaconear.Builder()
                .setContext(this)
                .setPublicKey(publicKey)
                .enableLocalDataUpdate()
                .addTaggedThingsRegionMonitoring(typesToRange, "Paleta de colores", new TaggedThingsRegionCallback() {
                    @Override
                    public void whenEntered(String regionName) {
                        showToast("¡Bienvenido a la paleta de colores!");
                    }

                    @Override
                    public void whenExited(String regionName) {
                        showToast("Has salido de la paleta de colores");
                    }
                })
                .addTaggedThingsRegionMonitoring(MyTaggedTypes.RED, "Fuente roja", new TaggedThingsRegionCallback() {
                    @Override
                    public void whenEntered(String regionName) {
                    }

                    @Override
                    public void whenExited(String regionName) {
                        red = 0;
                    }
                })
                .addTaggedThingsRegionMonitoring(MyTaggedTypes.BLUE, "Fuente azul", new TaggedThingsRegionCallback() {
                    @Override
                    public void whenEntered(String regionName) {}

                    @Override
                    public void whenExited(String regionName) {
                        blue = 0;
                    }
                })
                .addTaggedThingsRegionMonitoring(MyTaggedTypes.GREEN, "Fuente verde", new TaggedThingsRegionCallback() {
                    @Override
                    public void whenEntered(String regionName) {}

                    @Override
                    public void whenExited(String regionName) {
                        green = 0;
                    }
                })
                .addOnDetectionCallbackForType(MyTaggedTypes.RED, new OnDetectionCallback<TaggedThingBuilder>() {
                    @Override
                    public void onDetected(TaggedThingBuilder thingBuilder) {
                        RedSource redSource = thingBuilder.buildTaggedThing(RedSource.class);
                        BigDecimal bigDecimal = BigDecimal.valueOf(thingBuilder.getDistance() * redSource.getFactor());
                        red = 255 - bigDecimal.intValue();
                        draw();
                    }
                })
                .addOnDetectionCallbackForType(MyTaggedTypes.GREEN, new OnDetectionCallback<TaggedThingBuilder>() {
                    @Override
                    public void onDetected(TaggedThingBuilder thingBuilder) {
                        GreenSource greenSource = thingBuilder.buildTaggedThing(GreenSource.class);
                        BigDecimal bigDecimal = BigDecimal.valueOf(thingBuilder.getDistance() * greenSource.getFactor());
                        green = 255-bigDecimal.intValue();
                        draw();
                    }
                })
                .addOnDetectionCallbackForType(MyTaggedTypes.BLUE, new OnDetectionCallback<TaggedThingBuilder>() {
                    @Override
                    public void onDetected(TaggedThingBuilder thingBuilder) {
                        BlueSource blueSource = thingBuilder.buildTaggedThing(BlueSource.class);
                        BigDecimal bigDecimal = BigDecimal.valueOf(thingBuilder.getDistance() * blueSource.getFactor());
                        blue = 255-bigDecimal.intValue();
                        draw();
                    }
                })
                .build();
    }

    private void draw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (red < 0) {
                    red = 0;
                }
                if (blue < 0) {
                    blue = 0;
                }
                if (green < 0) {
                    green = 0;
                }

                redText.setText("R: " + red);
                greenText.setText("G: " + green);
                blueText.setText("B: " + blue);

                layout.setBackgroundColor(Color.rgb(red, green, blue));
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
