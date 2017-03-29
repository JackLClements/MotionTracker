package tsc.uea.ac.uk.wearablemotiontracker;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.view.WindowManager;
import android.widget.TextView;

//debug
import android.util.Log;

import tsc.uea.ac.uk.hardwareaccess.WatchHardware;

public class watchfaceact extends Activity {

    private TextView mTextView;
    private TextView mX;
    private TextView mY;
    private TextView mZ;
    private WatchHardware mHardware;
    private Handler handler;

    private Runnable updateValuesThread = new Runnable(){
        @Override
        public void run(){
            //get thing
            //set text
            float [] values = mHardware.getAccelValues();
            mX.setText("X - " + values[0]);
            mY.setText("Y - " + values[1]);
            mZ.setText("Z - " + values[2]);
            handler.postDelayed(this, 500); //calling this inside run essentially ensures run() will run again
                                            //there has to be a better way of looping this, surely?
        }
    };

    //activity when updating UI on mainscreen, 2 options
    //anonymous class implementation of runnable
    // final Handler mHandler = new Handler();
    // Runnable updateUI = new Runnable() {

    //OR

    //new thread entirely, attatch thread to process, may leave flow control to be a tad wacky

    //watchviewstub deprecated in 23+, use version check to create obj stub
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("CREATION", "Created!");
        super.onCreate(savedInstanceState);
        mHardware = new WatchHardware(getApplicationContext()); //pass activity
        handler = new Handler(); //ensures safety, but not entirely happy with how this runs
        setContentView(R.layout.activity_watchfaceact2);

        //Keep screen on
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //boxinsetlayout creation
            mTextView = (TextView) findViewById(R.id.text);
            mX = (TextView) findViewById(R.id.textX);
            mY = (TextView) findViewById(R.id.textY);
            mZ = (TextView) findViewById(R.id.textZ);
        }
        else{ //deprecated for older watches
            System.out.println("Old.");
            final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
            stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
                @Override
                public void onLayoutInflated(WatchViewStub stub) {
                    mTextView = (TextView) stub.findViewById(R.id.text);
                }
            });
        }
        handler.post(updateValuesThread);
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