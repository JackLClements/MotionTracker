package tsc.uea.ac.uk.hardwareaccess;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;
import static android.content.Context.*; //may need to change this too

/**
 * Created by Jack L. Clements on 25/03/2017.
 */


/*
Best practices to follow when working w/ sensors
- Unregister sensor listeners (avoid battery drain)
- Don't block the onSensorChanged() method
- Avoid using deprecated methods or sensor types
- Perform any data modification outside of onSensorChanged()
- Avoid using deprecated methods or sensor types
- Verify sensors before you use them
- Choose sensor delays carefully
 */
public class WatchHardware {
    private final SensorManager wSensorManager;
    private final Sensor wAccelorometer;
    private final Sensor wGyroscope;
    private float [] accelValues;
    private float [] gyroValues;

    /**
     * Anonymous inner class implementation of EventListener interface.
     * Interestingly (will remove before I show this to anyone lol), this anonymous etc. from
     * the interface is required for all listeners. I'm unaware of any default listener behavior.
     * Wondering what to call right now though.
     * Obviously due to spec, all method calls in containing class must refer to final variables
     *
     *
     * Note 2 - accel values include gravity by default, a low pass filter isolates gravity,
     * whereas a high pass removes it. Removing gravity as a constant will provide linear acceleration.
     * Look into physics of this.
     *
     */
    private final SensorEventListener MotionListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                for(int i = 0; i < 3; i++){
                    accelValues[i] = event.values[i];
                }
            }
            if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
                for(int i = 0; i < 3; i++){
                    gyroValues[i] = event.values[i];
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //on accuracy changed
        }
    };




    /**
     * Constructor that initialises sensors using passed activity - seems best way to do it, will check
     * @param context system context to call hardare
     */
    public WatchHardware(Context context){
        //an activity represents a single screen - when passing between activities is hardware
        //kept the same?
        wSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        wAccelorometer = wSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        wGyroscope = wSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelValues = new float[3];
        gyroValues = new float[3];
    }


    public float [] getAccelValues(){
        return accelValues;
    }

    public float [] getGyroValues(){
        return gyroValues;
    }

    /**
     * Registers listener object to track values
     */
    public void registerListener(){
        wSensorManager.registerListener(MotionListener, wAccelorometer, SensorManager.SENSOR_DELAY_FASTEST); //may need to manually set sample rate to something... managable
        wSensorManager.registerListener(MotionListener, wGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregisterListener(){
        wSensorManager.unregisterListener(MotionListener, wAccelorometer);
        wSensorManager.unregisterListener(MotionListener, wGyroscope);
    }


    public float[] lowPass(float [] input, float [] output){
        for(int i = 0; i < input.length; i++){
            output[i] = output[i] + 0.5f * (input[i] - output[i]); //0.5 represents an alpha value, this needs calculating properly in future
        }
        return output;
    }








}
