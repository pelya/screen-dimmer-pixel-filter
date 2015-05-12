package screen.dimmer.pixelfilter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;


public class MainActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener {
    public static final String LOG = "Pixel Filter";
    public static final int NTF_ID = 3321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Cfg.Init(this);

        final Boolean[] patternSelection = new Boolean[1];
        patternSelection[0] = true;

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_filler, android.R.layout.simple_spinner_item);
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
            final int idx = i;
            CheckBox c = (CheckBox)findViewById(Grids.Id[i]);
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (patternSelection[0]) {
                        return;
                    }
                    Log.d(LOG, "GUI: Clicked grid, idx " + idx + " checked " + isChecked);
                    ((CheckBox) findViewById(R.id.enableFilter)).setChecked(false);
                    Cfg.Pattern = Grids.PatternIdCustom;
                    for (int i = 0; i < Grids.Id.length; i++) {
                        CheckBox c = (CheckBox) findViewById(Grids.Id[i]);
                        Grids.Patterns[Cfg.Pattern][i] = (byte)(c.isChecked() ? 1 : 0);
                    }
                    ((Spinner) findViewById(R.id.spinner)).setSelection(Cfg.Pattern, true);
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

        FilterService.gui = this;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (FilterService.running == isChecked) {
            Log.d(LOG, "GUI: Service already started, no need to enable it");
            return;
        }

        Log.d(LOG, "GUI: Enabling screen filter: " + isChecked);
        Intent intent = new Intent(this, FilterService.class);
        NotificationManager ntfMgr = (NotificationManager)getSystemService(Service.NOTIFICATION_SERVICE);

        if (isChecked) {
            PendingIntent show = PendingIntent.getActivity(this, 0, new Intent(Intent.ACTION_DELETE, null, this, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent cancel = PendingIntent.getService(this, 0, new Intent(Intent.ACTION_DELETE, null, this, FilterService.class), PendingIntent.FLAG_CANCEL_CURRENT);

            Notification ntf = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(getString(R.string.filter_active))
                    .setContentText(getString(R.string.filter_active_2))
                    .setLocalOnly(true)
                    .setDeleteIntent(cancel)
                    .setSound(null)
                    .setContentIntent(show)
                    .setDeleteIntent(cancel)
                    .build();

            ntfMgr.notify(NTF_ID, ntf);
            startService(intent);
        } else {
            ntfMgr.cancel(NTF_ID);
            stopService(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateCheckbox();
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
}
