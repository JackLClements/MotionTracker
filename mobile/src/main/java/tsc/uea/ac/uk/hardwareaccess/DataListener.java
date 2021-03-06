package tsc.uea.ac.uk.hardwareaccess;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import tsc.uea.ac.uk.shared.DataLayer;
import tsc.uea.ac.uk.wearablemotiontracker.mainscreen;

/**
 * Created by Jack L. Clements on 08/04/2017.
 * Listens on data layer for change in data.
 * Running state set in build settings
 */

public class DataListener extends WearableListenerService {

    private DataLayer dataLayer;

    private static mainscreen activity;
    private static float [] accelorometer = new float[3];
    private static float [] gyroscope = new float[3];
    private static boolean running = false; //would rather have a notification rather than whatever else
    private static final String FILEPATH = "/watchValues";
    private static final String BEGIN = "/begin";

    /**
     * Default constructor
     */
    public DataListener(){
    }

    /**
     * Initialises object and opens data layer
     * @param context
     */
    public DataListener(Context context){
        dataLayer = new DataLayer(context); //connects to data layer
    }

    /**
     * Called upon creation, initialises data layer
     */
    @Override
    public void onCreate(){
        super.onCreate();
        dataLayer = new DataLayer(this);
    }

    /**
     * Whenever a data item object is created, deleted, or changed, the system triggers this callback on all connected nodes.
     * @param dataEvents
     */
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        DataMap map;
        for(DataEvent event : dataEvents){
            if(event.getType() == DataEvent.TYPE_CHANGED){
                if(event.getDataItem().getUri().getPath().equals(FILEPATH)){
                    map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                    accelorometer = map.getFloatArray("ACCEL");
                    gyroscope = map.getFloatArray("GYRO");
                }
                if(event.getDataItem().getUri().getPath().equals(BEGIN)){
                    map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                    running = map.getBoolean("START");
                }
            }
        }
    }

    /**
     * A message sent from a node triggers this callback on the target node.
     * Remote Procedure Call, one way communication.
     * @param messageEvent
     */
    @Override
    public void onMessageReceived(MessageEvent messageEvent){
        try{
            String text = new String(messageEvent.getData(), "UTF-8");
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            toast.show();
            activity.beginCapture();
        }
        catch (Exception e){
            Log.d("ERROR", e.toString());
        }
    }

    /**
     * When a capability that an instance of your app advertises becomes available on the network, that event triggers this callback.
     * If you're looking for a nearby node you can query the isNearby() method of the nodes provided in the callback.
     * @param capabilityInfo
     */
    @Override
    public void onCapabilityChanged(CapabilityInfo capabilityInfo){

    }

    /**
     * Returns accelorometer values
     * @return returns accelorometer values
     */
    public static float [] getAccelData(){
        return accelorometer;
    }

    /**
     * Returns gyroscope values
     * @return gyroscope values
     */
    public static float [] getGyroData(){
        return gyroscope;
    }

    /**
     * sets context listener is attached to
     * @param screen reference to application's main screen
     */
    public static void setActivity(mainscreen screen){
        activity = screen;
    }
}
