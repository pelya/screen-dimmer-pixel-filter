package screen.dimmer.pixelfilter;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

public class FilterService extends Service {
    public static final String LOG = "Pixel Filter";

    private WindowManager windowManager;
    private ImageView view;

    @Override
    public IBinder onBind(Intent intent) {
    return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(LOG, "Service started");
        Cfg.Init(this);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        view = new ImageView(this);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        Bitmap bmp = Bitmap.createBitmap(metrics, Grids.GridSideSize, Grids.GridSideSize, Bitmap.Config.ARGB_4444);
        for (int i = 0; i < Grids.GridSize; i++) {
            bmp.setPixel(i % Grids.GridSideSize, i / Grids.GridSideSize, (Grids.Patterns[Cfg.Pattern][i] == 0) ? Color.TRANSPARENT : Color.BLACK);
        }
        BitmapDrawable draw = new BitmapDrawable(bmp);
        draw.setTileModeXY(Shader.TileMode.REPEAT,Shader.TileMode.REPEAT);
        draw.setFilterBitmap(false);
        draw.setAntiAlias(false);
        view.setImageDrawable(draw);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, // TYPE_SYSTEM_ALERT TYPE_TOAST
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                PixelFormat.TRANSPARENT //PixelFormat.RGBA_4444 //PixelFormat.TRANSLUCENT
        );

        windowManager.addView(view, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null) {
            windowManager.removeView(view);
            view = null;
        }
        Log.i(LOG, "Service stopped");
    }
}
