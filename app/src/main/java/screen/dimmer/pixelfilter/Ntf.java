package screen.dimmer.pixelfilter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

public class Ntf {
    public static final String LOG = "Pixel Filter"; //NON-NLS
    public static final int NTF_ID = 3321;

    public static void show(Service ctx, boolean enable) {
        NotificationManager ntfMgr = (NotificationManager)ctx.getSystemService(Service.NOTIFICATION_SERVICE);

        if (enable) {
           // PendingIntent show = PendingIntent.getActivity(ctx, 0, new Intent(Intent.ACTION_DELETE, null, ctx, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent edit = PendingIntent.getActivity(ctx, 0, new Intent(Intent.ACTION_EDIT, null, ctx, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent cancel = PendingIntent.getService(ctx, 0, new Intent(Intent.ACTION_DELETE, null, ctx, FilterService.class), PendingIntent.FLAG_CANCEL_CURRENT);
            RemoteViews ntfView = new RemoteViews(ctx.getPackageName(), R.layout.notification);
            ntfView.setOnClickPendingIntent(R.id.notificationConfigure, edit);
            ntfView.setOnClickPendingIntent(R.id.notificationIcon, cancel);
            ntfView.setOnClickPendingIntent(R.id.notificationText, cancel);

            Notification.Builder builder = new Notification.Builder(ctx)
                    .setContent(ntfView)
                    .setDeleteIntent(cancel)
                    .setPriority(Notification.PRIORITY_LOW)
                    .setSmallIcon(R.drawable.notification)
                    .setSound(null)
                    .setOngoing(true)
                    .setTicker(null);

            // Using reflection so the app will not crash on lower API devices
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                builder.setLocalOnly(true);
                builder.setVisibility(Notification.VISIBILITY_PUBLIC);
            }

            Notification ntf = builder.build();
            ctx.startForeground(NTF_ID, ntf);
        } else {
            ctx.stopForeground(true);
            ntfMgr.cancel(NTF_ID);
        }
    }
}
