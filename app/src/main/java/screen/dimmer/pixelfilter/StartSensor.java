package screen.dimmer.pixelfilter;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class StartSensor {

    public static StartSensor get() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            return StartSensorKitkat.Holder.sInstance;
        } else {
            return StartSensorCompat.Holder.sInstance;
        }
    }
    public abstract void registerListener(SensorManager manager, SensorEventListener listener, Sensor sensor, int samplingPeriodUs, int maxReportLatencyUs);

    private static class StartSensorKitkat extends StartSensor {
        private static class Holder
        {
            private static final StartSensorKitkat sInstance = new StartSensorKitkat();
        }
        public void registerListener(SensorManager manager, SensorEventListener listener, Sensor sensor, int samplingPeriodUs, int maxReportLatencyUs)
        {
            manager.registerListener(listener, sensor, samplingPeriodUs, maxReportLatencyUs);
        }
    }
    private static class StartSensorCompat extends StartSensor {
        private static class Holder
        {
            private static final StartSensorCompat sInstance = new StartSensorCompat();
        }
        public void registerListener(SensorManager manager, SensorEventListener listener, Sensor sensor, int samplingPeriodUs, int maxReportLatencyUs)
        {
            manager.registerListener(listener, sensor, samplingPeriodUs);
        }
    }
}
