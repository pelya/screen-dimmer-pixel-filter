package screen.dimmer.pixelfilter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

public class OnOffWidget extends AppWidgetProvider {
     public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            PendingIntent onOffIntent = PendingIntent.getService(context, 0,
                    new Intent(Intent.ACTION_INSERT, null, context, FilterService.class),
                    PendingIntent.FLAG_CANCEL_CURRENT);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.on_off_widget);
            views.setOnClickPendingIntent(R.id.widget_on_off_button, onOffIntent);
            views.setOnClickPendingIntent(R.id.widget_on_off_text, onOffIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
