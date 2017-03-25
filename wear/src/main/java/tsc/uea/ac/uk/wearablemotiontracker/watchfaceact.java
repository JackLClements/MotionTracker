package tsc.uea.ac.uk.wearablemotiontracker;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.wearable.view.ConfirmationOverlay;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import tsc.uea.ac.uk.hardwareaccess.WatchHardware;

public class watchfaceact extends Activity {

    private TextView mTextView;
    private WatchHardware mHardware;

    //activity when updating UI on mainscreen, 2 options
    //anonymous class implementation of runnable
    // final Handler mHandler = new Handler();
    // Runnable updateUI = new Runnable() {

    //OR

    //new thread entirely, attatch thread to process, may leave flow control to be a tad wacky

    //watchviewstub deprecated in 23+, use version check to create obj stub
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchfaceact2);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //boxinsetlayout creation
            mTextView.setAllCaps(true); //test
            mTextView = (TextView) findViewById(R.id.text);
        }
        else{ //deprecated for older watches
            final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
            stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
                @Override
                public void onLayoutInflated(WatchViewStub stub) {
                    mTextView = (TextView) stub.findViewById(R.id.text);
                }
            });
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        mHardware.unregisterListener();
    }

    @Override
    public void onResume(){
        super.onResume();
        //wipe file
        mHardware.registerListener();
    }

}
