package screen.dimmer.pixelfilter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

public class Ntf {
    public static final int NTF_ID = 3321;

    public static void show(Context ctx, boolean enable) {
        NotificationManager ntfMgr = (NotificationManager)ctx.getSystemService(Service.NOTIFICATION_SERVICE);

        if (enable && Cfg.EnableNotification) {
            PendingIntent show = PendingIntent.getActivity(ctx, 0, new Intent(Intent.ACTION_DELETE, null, ctx, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent cancel = PendingIntent.getService(ctx, 0, new Intent(Intent.ACTION_DELETE, null, ctx, FilterService.class), PendingIntent.FLAG_CANCEL_CURRENT);

            Notification.Builder builder = new Notification.Builder(ctx)
                    .setContentIntent(show)
                    .setContentInfo(Cfg.SwipeToDisable ? ctx.getString(R.string.swipe_to_disable) : "")
                    .setContentText(ctx.getString(R.string.tap_to_configure))
                    .setContentTitle(ctx.getString(R.string.filter_active))
                    .setDeleteIntent(cancel)
                    .setPriority(Notification.PRIORITY_LOW)
                    .setSmallIcon(R.drawable.notification)
                    //.setLocalOnly(true) // Lollipop only
                    .setSound(null)
                    .setTicker(ctx.getString(R.string.filter_active));

            if (!Cfg.SwipeToDisable)
                builder.setOngoing(true);

            Notification ntf = builder.build();

            ntfMgr.notify(NTF_ID, ntf);
        } else {
            ntfMgr.cancel(NTF_ID);
        }
    }
}
