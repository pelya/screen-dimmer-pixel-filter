package screen.dimmer.pixelfilter;

import android.content.Context;

public class Cfg {

    public static boolean Initialized = false;

    public static int Pattern = 3;
    public static int ShiftTimeoutIdx = 4;

    public static void Init(Context ctx) {
        if (Initialized) {
            return;
        }
    }

    public static void Save(Context ctx) {
        if (!Initialized) {
            return;
        }

    }
}
