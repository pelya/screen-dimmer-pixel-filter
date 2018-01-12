package screen.dimmer.pixelfilter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public final class StartOnBootReceiver extends BroadcastReceiver
{
    public static final String LOG = "Pixel Filter"; //NON-NLS

    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        Cfg.Init(context);
        if (Cfg.WasEnabled || Cfg.PersistentNotification) {
            Intent service = new Intent(context, FilterService.class);
            if (!Cfg.WasEnabled) {
                service.setAction(Intent.ACTION_DELETE);
            }
            context.startService(service);
            return;
        }
        System.exit(0);
    }
}
