package screen.dimmer.pixelfilter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class Ntf {
    public static final String LOG = "Pixel Filter"; //NON-NLS
    public static final int NTF_ID = 3321;

    public static void show(Service ctx, boolean enable) {
        NotificationManager ntfMgr = (NotificationManager)ctx.getSystemService(Service.NOTIFICATION_SERVICE);

        if (enable || Cfg.PersistentNotification) {
            PendingIntent edit = PendingIntent.getActivity(ctx, 0, new Intent(Intent.ACTION_EDIT, null, ctx, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent cancel = PendingIntent.getService(ctx, 0, new Intent(enable ? Intent.ACTION_DELETE : Intent.ACTION_RUN, null, ctx, FilterService.class), PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent increase = PendingIntent.getService(ctx, 0, new Intent(ctx.getString(R.string.intent_darker), null, ctx, FilterService.class), PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent decrease = PendingIntent.getService(ctx, 0, new Intent(ctx.getString(R.string.intent_brighter), null, ctx, FilterService.class), PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Action.Builder ntf_act_increase = new NotificationCompat.Action.Builder(R.drawable.ic_add_circle_outline,
                    ctx.getString(R.string.darker), increase);
            NotificationCompat.Action.Builder ntf_act_decrease = new NotificationCompat.Action.Builder(R.drawable.ic_remove_circle_outline,
                    ctx.getString(R.string.brighter), decrease);
            NotificationCompat.Action.Builder ntf_act_configure = new NotificationCompat.Action.Builder(R.drawable.ic_build,
                    ctx.getString(R.string.configure), edit);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
                    .setContentTitle(ctx.getString(R.string.app_name) +
                            (enable ? " - " + Grids.PatternNames[Cfg.Pattern] : ""))
                    .setContentText(enable ? ctx.getString(R.string.tap_to_disable) : ctx.getString(R.string.enable_filter_checkbox))
                    .setContentInfo(ctx.getString(R.string.swipe_to_configure))
                    .setContentIntent(cancel)
                    .addAction(ntf_act_configure.build())
                    .addAction(ntf_act_increase.build())
                    .addAction(ntf_act_decrease.build())
                    .setDeleteIntent(cancel)
                    .setPriority(Cfg.HideNotification ? NotificationCompat.PRIORITY_MIN : NotificationCompat.PRIORITY_LOW)
                    .setSmallIcon(R.drawable.notification)
                    .setSound(null)
                    .setOngoing(true)
                    .setLocalOnly(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setTicker(null)
                    .setShowWhen(false);

            Notification ntf = builder.build();
            ctx.startForeground(NTF_ID, ntf);
        } else {
            ctx.stopForeground(true);
            ntfMgr.cancel(NTF_ID);
        }
    }
}
