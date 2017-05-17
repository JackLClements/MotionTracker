package tsc.uea.ac.uk.hardwareaccess;

import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;

import tsc.uea.ac.uk.shared.DataLayer;

/**
 * Created by Jack L. Clements on 09/04/2017.
 * Alt. implementation for potential data listener class, used to save power for when other listener does not need to run 24/7
 */

public class DataListenerPoint2 implements DataApi.DataListener, MessageApi.MessageListener, CapabilityApi.CapabilityListener{
    DataLayer layer;
    float [] accelorometer;
    final static String FILEPATH = "/watchValues";

    /**
     * Called when data has changed. Needs to be filtered by intent.
     * @param dataEvents
     */
    @Override
    public void onDataChanged(DataEventBuffer dataEvents){
        DataMap map;
        for(DataEvent event : dataEvents){
            if(event.getType() == DataEvent.TYPE_CHANGED){
                if(event.getDataItem().getUri().getPath().equals(FILEPATH)){
                    map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                    accelorometer = map.getFloatArray("GET_TO_THIS");
                }
            }
        }
    }

    /**
     * Method stub, needs to be implemented
     * @param messageEvent message event object comprising of message recieved
     */
    @Override
    public void onMessageReceived(MessageEvent messageEvent){

    }

    /**
     * Called when
     * @param capabilityInfo object comrpising of device information
     */
    @Override
    public void onCapabilityChanged(CapabilityInfo capabilityInfo){

    }

}
