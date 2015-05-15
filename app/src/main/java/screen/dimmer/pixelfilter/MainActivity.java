package screen.dimmer.pixelfilter;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener, SensorEventListener {
    public static final String LOG = "Pixel Filter";

    private SensorManager sensors;
    private Sensor lightSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Cfg.Init(this);

        final Boolean[] patternSelection = new Boolean[1];
        patternSelection[0] = true;

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, Grids.PatternNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                patternSelection[0] = true;
                for (int i = 0; i < Grids.Id.length; i++) {
                    CheckBox c = (CheckBox) findViewById(Grids.Id[i]);
                    c.setChecked(Grids.Patterns[(int) id][i] != (byte) 0);
                    c.setEnabled(id >= Grids.PatternIdCustom);
                }
                Cfg.Pattern = (int) id;
                ((Spinner) findViewById(R.id.spinner)).setSelection((int) id, true);
                final CheckBox c = ((CheckBox) findViewById(R.id.enableFilter));
                boolean wasChecked = c.isChecked();
                Log.d(LOG, "GUI: setting new pattern, checkbox was " + wasChecked);
                c.setChecked(false);
                if (wasChecked) {
                    new Thread(new Runnable() {
                        public void run() {
                            while (FilterService.running) {
                                try { Thread.sleep(20); } catch (Exception e) {}
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    c.setChecked(true);
                                }
                            });
                        }
                    }).start();
                }
                patternSelection[0] = false;
                Cfg.Save(MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setSelection(Cfg.Pattern);

        for (int i = 0; i < Grids.Id.length; i++) {
            CheckBox c = (CheckBox)findViewById(Grids.Id[i]);
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (patternSelection[0] || Cfg.Pattern < Grids.PatternIdCustom) {
                        return;
                    }
                    //Log.d(LOG, "GUI: Clicked grid, idx " + idx + " checked " + isChecked);
                    ((CheckBox) findViewById(R.id.enableFilter)).setChecked(false);
                    for (int i = 0; i < Grids.Id.length; i++) {
                        CheckBox c = (CheckBox) findViewById(Grids.Id[i]);
                        Grids.Patterns[Cfg.Pattern][i] = (byte)(c.isChecked() ? 1 : 0);
                    }
                    Cfg.Save(MainActivity.this);
                }
            });
        }

        patternSelection[0] = false;

        CheckBox c = (CheckBox)findViewById(R.id.enableFilter);
        if (FilterService.running) {
            Log.d(LOG, "GUI: FilterService is running, setting checkbox");
            c.setChecked(true);
        }
        c.setOnCheckedChangeListener(this);
        if (getIntent() != null && Intent.ACTION_DELETE.equals(getIntent().getAction())) {
            Log.d(LOG, "GUI: got shutdown intent, stopping service");
            c.setChecked(false);
        }

        Spinner shiftTimer = (Spinner) findViewById(R.id.shift_timer);
        shiftTimer.setSelection(Cfg.ShiftTimeoutIdx);
        shiftTimer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cfg.ShiftTimeoutIdx = (int) id;
                Cfg.Save(MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sensors = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensors.getDefaultSensor(Sensor.TYPE_LIGHT);

        final EditText lightLevel = (EditText) findViewById(R.id.triggerLightLevel);
        lightLevel.setText(String.valueOf((int)Cfg.LightSensorValue));
        lightLevel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Cfg.LightSensorValue = Integer.parseInt(editable.toString());
                    Cfg.Save(MainActivity.this);
                } catch (Exception eeeee) {
                }
            }
        });

        CheckBox lightLevelCheckbox = (CheckBox) findViewById(R.id.enableLightSensor);
        lightLevelCheckbox.setChecked(Cfg.UseLightSensor);
        lightLevelCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Cfg.UseLightSensor = b;
                Cfg.Save(MainActivity.this);
                final CheckBox c = ((CheckBox) findViewById(R.id.enableFilter));
                boolean wasChecked = c.isChecked();
                Log.d(LOG, "GUI: setting new light sensor value, checkbox was " + wasChecked);
                c.setChecked(false);
                if (wasChecked) {
                    new Thread(new Runnable() {
                        public void run() {
                            while (FilterService.running) {
                                try { Thread.sleep(20); } catch (Exception e) {}
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    c.setChecked(true);
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        Button lightSensorUseCurrent = (Button) findViewById(R.id.lightLevelUseCurrent);
        lightSensorUseCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView currentLightLevel = (TextView)findViewById(R.id.currentLightLevel);
                lightLevel.setText(currentLightLevel.getText());
                try {
                    Cfg.LightSensorValue = Integer.parseInt(currentLightLevel.getText().toString());
                    Cfg.Save(MainActivity.this);
                } catch (Exception eeeee) {
                }
            }
        });

        FilterService.gui = this;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (FilterService.running == isChecked) {
            Log.d(LOG, "GUI: Service already started, no need to enable it");
            return;
        }

        Intent intent = new Intent(this, FilterService.class);
        if (isChecked) {
            startService(intent);
        } else {
            stopService(intent);
        }

        Log.d(LOG, "GUI: Enabling screen filter: " + isChecked);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateCheckbox();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lightSensor != null) {
            sensors.unregisterListener(this, lightSensor);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCheckbox();
        if (lightSensor != null) {
            StartSensor.get().registerListener(sensors, this, lightSensor, 2000000, 2000000);
        }
    }

    public void updateCheckbox() {
        CheckBox c = (CheckBox)findViewById(R.id.enableFilter);
        Log.d(LOG, "GUI: update checkbox: " + FilterService.running);
        c.setChecked(FilterService.running);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (FilterService.gui == this)
            FilterService.gui = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView lightLevel = (TextView) findViewById(R.id.currentLightLevel);
        lightLevel.setText(String.valueOf((int)event.values[0]));
    }
}
