package screen.dimmer.pixelfilter;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < Grids.Id.length; i++) {
            final int idx = i;
            CheckBox c = (CheckBox)findViewById(Grids.Id[i]);
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d(LOG, "Clicked grid, idx " + idx + " checked " + isChecked);
                }
            });
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_filler, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, Grids.PatternNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < Grids.Id.length; i++) {
                    CheckBox c = (CheckBox) findViewById(Grids.Id[i]);
                    c.setChecked(Grids.Patterns[(int) id][i] != (byte) 0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        CheckBox c = (CheckBox)findViewById(R.id.enableFilter);
        c.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(LOG, "Enabling screen filter: " + isChecked);
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
