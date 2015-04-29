package screen.dimmer.pixelfilter;

import android.content.Context;

public class Cfg {

    public static boolean Initialized = false;

    public static int Pattern = 0;
    //public static byte[] CustomPattern = new byte[Grids.GridSize];

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
