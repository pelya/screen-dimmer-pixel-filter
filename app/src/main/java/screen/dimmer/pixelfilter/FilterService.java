package screen.dimmer.pixelfilter;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


public class FilterService extends Service implements SensorEventListener {
    public static final String LOG = "Pixel Filter";

    public static boolean DEBUG = false; // true;

    private WindowManager windowManager;
    private ImageView view;
    private Bitmap bmp;
    //private Timer timer;
    private boolean destroyed = false;
    private boolean intentProcessed = false;
    public static boolean running = false;
    public static MainActivity gui = null;

    private SensorManager sensors = null;
    private Sensor lightSensor = null;
    private ScreenOffReceiver screenOffReceiver = null;

    @Override
    public IBinder onBind(Intent intent) {
    return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        running = true;
        MainActivity guiCopy = gui;
        if (guiCopy != null)
            guiCopy.updateCheckbox();

        Log.d(LOG, "Service started");
        Cfg.Init(this);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        view = new ImageView(this);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        bmp = Bitmap.createBitmap(metrics, Grids.GridSideSize, Grids.GridSideSize, Bitmap.Config.ARGB_4444);
        if (DEBUG) {
            bmp = Bitmap.createBitmap(metrics, Grids.GridSideSize * 4, Grids.GridSideSize * 4, Bitmap.Config.ARGB_4444);
        }

        updatePattern();
        BitmapDrawable draw = new BitmapDrawable(bmp);
        draw.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        draw.setFilterBitmap(false);
        draw.setAntiAlias(false);
        view.setBackground(draw);
        //view.setImageDrawable(getResources().getDrawable(R.drawable.test));

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                PixelFormat.TRANSPARENT
        );

        windowManager.addView(view, params);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updatePattern();
                view.invalidate();
                if (!destroyed) {
                    handler.postDelayed(this, Grids.ShiftTimeouts[Cfg.ShiftTimeoutIdx]);
                }
            }
        }, Grids.ShiftTimeouts[Cfg.ShiftTimeoutIdx]);

        if (Cfg.UseLightSensor) {
            sensors = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            lightSensor = sensors.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (lightSensor != null) {
                StartSensor.get().registerListener(sensors, this, lightSensor, 1200000, 1000000);
            }

            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            screenOffReceiver = new ScreenOffReceiver();
            registerReceiver(screenOffReceiver, filter);
        }
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        if (intent != null && (Intent.ACTION_DELETE.equals(intent.getAction()) ||
                Intent.ACTION_INSERT.equals(intent.getAction()) && intentProcessed)) {
            Log.d(LOG, "Service got shutdown intent");
            Ntf.show(this, false);
            stopSelf();
            intentProcessed = true;
            return START_NOT_STICKY;
        }

        Ntf.show(this, true);
        intentProcessed = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyed = true;
        if (lightSensor != null) {
            unregisterReceiver(screenOffReceiver);
            sensors.unregisterListener(this, lightSensor);
        }
        if (view != null) {
            windowManager.removeView(view);
        }
        Ntf.show(this, false);
        Log.d(LOG, "Service stopped");
        running = false;
        MainActivity guiCopy = gui;
        if (guiCopy != null)
            guiCopy.updateCheckbox();
    }

    int getShift() {
        long shift = (System.currentTimeMillis() / Grids.ShiftTimeouts[Cfg.ShiftTimeoutIdx]) % Grids.GridSize;
        return Grids.GridShift[(int)shift];
    }

    void updatePattern() {
        int shift = getShift();
        int shiftX = shift % Grids.GridSideSize;
        int shiftY = shift / Grids.GridSideSize;
        for (int i = 0; i < Grids.GridSize; i++) {
            int x = (i + shiftX) % Grids.GridSideSize;
            //shiftY = 0;
            int y = ((i / Grids.GridSideSize) + shiftY) % Grids.GridSideSize;
            int color = (Grids.Patterns[Cfg.Pattern][i] == 0) ? Color.TRANSPARENT : Color.BLACK;
            if (DEBUG) {
                bmp.setPixel(x * 4,     y * 4,     color);
                bmp.setPixel(x * 4 + 1, y * 4,     color);
                bmp.setPixel(x * 4 + 2, y * 4,     color);
                bmp.setPixel(x * 4 + 3, y * 4,     color);
                bmp.setPixel(x * 4,     y * 4 + 1, color);
                bmp.setPixel(x * 4 + 1, y * 4 + 1, color);
                bmp.setPixel(x * 4 + 2, y * 4 + 1, color);
                bmp.setPixel(x * 4 + 3, y * 4 + 1, color);
                bmp.setPixel(x * 4,     y * 4 + 2, color);
                bmp.setPixel(x * 4 + 1, y * 4 + 2, color);
                bmp.setPixel(x * 4 + 2, y * 4 + 2, color);
                bmp.setPixel(x * 4 + 3, y * 4 + 2, color);
                bmp.setPixel(x * 4,     y * 4 + 3, color);
                bmp.setPixel(x * 4 + 1, y * 4 + 3, color);
                bmp.setPixel(x * 4 + 2, y * 4 + 3, color);
                bmp.setPixel(x * 4 + 3, y * 4 + 3, color);
            } else {
                bmp.setPixel(x, y, color);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] > Cfg.LightSensorValue) {
            stopSelf();
        }
    }

    class ScreenOffReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (lightSensor != null) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    //Log.d(LOG, "Service received screen off, disabling light sensor");
                    sensors.unregisterListener(FilterService.this, lightSensor);
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    //Log.d(LOG, "Service received screen on, enabling light sensor");
                    StartSensor.get().registerListener(sensors, FilterService.this, lightSensor, 1200000, 1000000);
                }
            }
        }
    }
}
