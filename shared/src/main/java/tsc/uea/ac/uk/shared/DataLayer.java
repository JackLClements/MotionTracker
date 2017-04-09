package tsc.uea.ac.uk.shared;

/**
 * Created by Jack L. Clements on 05/04/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class DataLayer implements Runnable {
    private static GoogleApiClient client;

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


    public void send(float [] payload){
        final float [] payloadCopy = payload;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(client.isConnected()){
                    PutDataMapRequest mapRequest = PutDataMapRequest.create("/watchValues");
                    mapRequest.getDataMap().putFloatArray("GET_TO_THIS", payloadCopy);
                    PutDataRequest dataRequest = mapRequest.asPutDataRequest();
                    dataRequest.setUrgent();
                    PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(client, dataRequest); //not quite?
                }
            }
        }).start(); //done in its own thread to avoid program blocking
    }

    //note thread to intercept settings updates from phone?

    @Override
    public void run(){

    }


}
