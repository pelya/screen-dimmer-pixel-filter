package screen.dimmer.pixelfilter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.Tile;


@TargetApi(Build.VERSION_CODES.N)
public class QuickSettingsTileService extends android.service.quicksettings.TileService {


    @Override
    public void onClick() {
        final Context context = getApplicationContext();
        startActivity(context);
        hideStatusBar(context);
    }

    private void startActivity(final Context context) {
        final Intent activityIntent = new Intent(context, MainActivity.class);
        context.startActivity(activityIntent);
    }

    private void hideStatusBar(final Context context) {
        final Intent closeStatusBarIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeStatusBarIntent);
    }

    private int getServiceState() {
        return FilterService.running ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE;
    }

    @Override
    public void onStartListening() {
        updateTileState(getServiceState());
    }

    private void updateTileState(final int state) {
        final Tile tile = getQsTile();

        if (tile == null)
            return;

        tile.setState(state);
        tile.updateTile();
    }
}
