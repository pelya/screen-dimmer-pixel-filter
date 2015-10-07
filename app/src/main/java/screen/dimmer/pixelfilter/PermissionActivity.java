package screen.dimmer.pixelfilter;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class PermissionActivity extends Activity {
    public static final String LOG = "Pixel Filter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Log.d(LOG, "Permission activity started");
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(LOG, "Requesting permission " + Manifest.permission.SYSTEM_ALERT_WINDOW);
            requestPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 1);
        } else {
            finish();
        }
        /*
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            public void run() {
            }
        }, 1000);
        */
        // com.android.settings/.applications.ManageApplications
    }
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0].equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            Log.d(LOG, "Permission granted, launching service");
            Intent intent = new Intent(this, FilterService.class);
            startService(intent);
        } else {
            Log.d(LOG, "Permission denied!");
            Toast.makeText(this, R.string.permission_denied_text, Toast.LENGTH_LONG).show();
        }
        finish();
    }
    */
    public void openSettings(View v) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.applications.ManageApplications"));

        startActivity(intent);
        finish();
    }
}
