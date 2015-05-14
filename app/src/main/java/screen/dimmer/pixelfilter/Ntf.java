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

        if (enable) {
            PendingIntent show = PendingIntent.getActivity(ctx, 0, new Intent(Intent.ACTION_DELETE, null, ctx, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent cancel = PendingIntent.getService(ctx, 0, new Intent(Intent.ACTION_DELETE, null, ctx, FilterService.class), PendingIntent.FLAG_CANCEL_CURRENT);

            Notification ntf = new Notification.Builder(ctx)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(ctx.getString(R.string.filter_active))
                    .setContentText(ctx.getString(R.string.filter_active_2))
                    .setLocalOnly(true)
                    .setDeleteIntent(cancel)
                    .setSound(null)
                    .setContentIntent(show)
                    .setDeleteIntent(cancel)
                    .build();

            ntfMgr.notify(NTF_ID, ntf);
        } else {
            ntfMgr.cancel(NTF_ID);
        }
    }
}
