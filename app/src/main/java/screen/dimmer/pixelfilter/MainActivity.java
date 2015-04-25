package screen.dimmer.pixelfilter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;


public class MainActivity extends ActionBarActivity {
    public static final String LOG = "Pixel Filter";

    public static int gridId[] = new int[] {
            R.id.checkBox1 ,R.id.checkBox2, R.id.checkBox3, R.id.checkBox4, R.id.checkBox5, R.id.checkBox6, R.id.checkBox7, R.id.checkBox8,
            R.id.checkBox9, R.id.checkBox10,R.id.checkBox11,R.id.checkBox12,R.id.checkBox13,R.id.checkBox14,R.id.checkBox15,R.id.checkBox16,
            R.id.checkBox17,R.id.checkBox18,R.id.checkBox19,R.id.checkBox20,R.id.checkBox21,R.id.checkBox22,R.id.checkBox23,R.id.checkBox24,
            R.id.checkBox25,R.id.checkBox26,R.id.checkBox27,R.id.checkBox28,R.id.checkBox29,R.id.checkBox30,R.id.checkBox31,R.id.checkBox32,
            R.id.checkBox33,R.id.checkBox34,R.id.checkBox35,R.id.checkBox36,R.id.checkBox37,R.id.checkBox38,R.id.checkBox39,R.id.checkBox40,
            R.id.checkBox41,R.id.checkBox42,R.id.checkBox43,R.id.checkBox44,R.id.checkBox45,R.id.checkBox46,R.id.checkBox47,R.id.checkBox48,
            R.id.checkBox49,R.id.checkBox50,R.id.checkBox51,R.id.checkBox52,R.id.checkBox53,R.id.checkBox54,R.id.checkBox55,R.id.checkBox56,
            R.id.checkBox57,R.id.checkBox58,R.id.checkBox59,R.id.checkBox60,R.id.checkBox61,R.id.checkBox62,R.id.checkBox63,R.id.checkBox64,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_filler, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        for (int i = 0; i < gridId.length; i++) {
            CheckBox c = (CheckBox)findViewById(gridId[i]);
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d(LOG, "Clicked grid, view " + buttonView.getId() + " checked " + isChecked);
                }
            });
        }
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
