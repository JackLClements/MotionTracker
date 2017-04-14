package tsc.uea.ac.uk.hardwareaccess;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import tsc.uea.ac.uk.shared.DataLayer;

/**
 * Created by Jack L. Clements on 08/04/2017.
 */

public class DataListener extends WearableListenerService {

    private DataLayer dataLayer;

    private static float [] accelorometer = new float[3];
    private static float [] gyroscope = new float[3];
    private static final String FILEPATH = "/watchValues";

    public DataListener(){
    }

    public DataListener(Context context){
        dataLayer = new DataLayer(context); //connects to data layer
    }


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
            }
        }
    }

    /**
     * A message sent from a node triggers this callback on the target node.
     * @param messageEvent
     */
    @Override
    public void onMessageReceived(MessageEvent messageEvent){

    }

    /**
     * When a capability that an instance of your app advertises becomes available on the network, that event triggers this callback.
     * If you're looking for a nearby node you can query the isNearby() method of the nodes provided in the callback.
     * @param capabilityInfo
     */
    @Override
    public void onCapabilityChanged(CapabilityInfo capabilityInfo){

    }


    public static float [] getAccelData(){
        return accelorometer;
    }

    public static float [] getGyroData(){
        return gyroscope;
    }
}
