package tsc.uea.ac.uk.shared;

/**
 * Created by Jack L. Clements on 05/04/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Set;

public class DataLayer implements Runnable {
    private static GoogleApiClient client;
    private static final String MOTION_CAPTURE_CAPABILITY = "motion_capture";

    public DataLayer(Context context){
        final Toast toast = Toast.makeText(context, "Connected to client", Toast.LENGTH_SHORT);
        client = new GoogleApiClient.Builder(context).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                toast.show();
            }

            @Override
            public void onConnectionSuspended(int i) {
                //toast.show();
            }
        }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Wearable.API).build();
        client.connect();
    }


    public void send(float [] accel, float[] gyro){
        final float [] payloadCopy = accel;
        final float [] payLoadCopy2 = gyro;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(client.isConnected()){
                    PutDataMapRequest mapRequest = PutDataMapRequest.create("/watchValues");
                    mapRequest.getDataMap().putFloatArray("ACCEL", payloadCopy);
                    mapRequest.getDataMap().putFloatArray("GYRO", payLoadCopy2);
                    PutDataRequest dataRequest = mapRequest.asPutDataRequest();
                    dataRequest.setUrgent();
                    PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(client, dataRequest); //not quite?
                }
            }
        }).start(); //done in its own thread to avoid program blocking
    }

    public void sendBegin(){
        //if node id is not null
        new Thread(new Runnable() {
            @Override
            public void run() {
                //need node ID
                String hello = "Hello World!";
                byte [] hB = hello.getBytes();
                Set<Node> nodes1 = getNodes().getNodes();
                for(Node node : nodes1){
                    Log.d("DLayerThread", "Node - " + node.getDisplayName());
                }

                String bestNode = pickBestNode(nodes1);
                if(bestNode != null){
                    Log.d("DLayerThread", "Best Node - " + bestNode);
                }

                /*
                NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(client).await();
                final List<Node> nodes2 = result.getNodes();*/
                Wearable.MessageApi.sendMessage(client, bestNode, "/BEGIN", hB).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
                        if(!sendMessageResult.getStatus().isSuccess()){
                            //do something
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * You can add listener, but for now we won't
     * @return
     */
    public CapabilityInfo getNodes(){
        CapabilityApi.GetCapabilityResult result = Wearable.CapabilityApi.getCapability(client, MOTION_CAPTURE_CAPABILITY, CapabilityApi.FILTER_REACHABLE).await();
        return result.getCapability();
    }

    public String pickBestNode(Set<Node> nodes){
        String bestNode = null;
        for(Node node : nodes){
            if(node.isNearby()){ //if close
                return node.getId();
            }
            bestNode = node.getId(); //otherwise arbitrary
        }
        return bestNode;
    }

    //settings must be persisted individually, fetch, add to bundle-wrapper then send/decode
    public void sendPreferences(final SharedPreferences preferences){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(client.isConnected()){
                    PutDataMapRequest mapRequest = PutDataMapRequest.create("/settings");

                }
            }
        }).start();
    }

    //note thread to intercept settings updates from phone?

    @Override
    public void run(){

    }


}
