package tsc.uea.ac.uk.wearablemotiontracker;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//debug
import android.util.Log;
import android.widget.Toast;
import tsc.uea.ac.uk.hardwareaccess.WatchHardware;
import tsc.uea.ac.uk.shared.DataLayer;

import static java.lang.Thread.sleep;

/**
 * Main activity class for the wear component.
 */
public class watchfaceact extends Activity {

    private TextView mTextView;
    private TextView mX;
    private TextView mY;
    private TextView mZ;
    private WatchHardware mHardware;
    private Handler handler;
    private DataLayer dataLayer;
    private ImageButton imageButton;

    /**
     * Thread designed to update the sensor values from the @see tsc.uea.ac.uk.hardwareAccess.WatchHardware instance.
     */
    private Runnable updateValuesThread = new Runnable(){
        @Override
        public void run(){
            //get thing
            //set text
            float [] aValues = mHardware.getAccelValues();
            float [] gValues = mHardware.getGyroValues();
            mX.setText("X - " + aValues[0]);
            mY.setText("Y - " + aValues[1]);
            mZ.setText("Z - " + aValues[2]);
            dataLayer.send(aValues, gValues);
            handler.postDelayed(this, 100); //calling this inside run essentially ensures run() will run again
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

    /**
     * Initialise values and set up sensors.
     * @param savedInstanceState state of application in bundle format
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("CREATION", "Created!");
        super.onCreate(savedInstanceState);
        mHardware = new WatchHardware(getApplicationContext()); //pass activity
        handler = new Handler(); //ensures safety, but not entirely happy with how this runs
        dataLayer = new DataLayer(getApplicationContext());
        setContentView(R.layout.activity_watchfaceact2);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //boxinsetlayout creation
            mTextView = (TextView) findViewById(R.id.text);
            mX = (TextView) findViewById(R.id.textX);
            mY = (TextView) findViewById(R.id.textY);
            mZ = (TextView) findViewById(R.id.textZ);
            imageButton = (ImageButton) findViewById(R.id.startButton);
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

    /**
     * Vibrate in order to alert user of capture, then begin to transmit data.
     * @param view
     */
    public void startCapture(View view){
        Runnable startCapture = new Runnable() {
            @Override
            public void run() {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                try{
                    vibrator.vibrate(250);
                    sleep(500);
                    vibrator.vibrate(250);
                    sleep(500);
                    vibrator.vibrate(500);
                }
                catch (Exception e){
                    Log.e("ERROR - ", e.toString());
                }
                dataLayer.sendBegin();
            }
        };
        handler.post(startCapture);

    }

    /**
     * Unregister listener objects to conserve performance
     */
    @Override
    public void onPause(){
        super.onPause();
        mHardware.unregisterListener();
        handler.removeCallbacks(updateValuesThread);
    }

    /**
     * Reregister listener to resume
     */
    @Override
    public void onResume(){
        super.onResume();
        //wipe file
        mHardware.registerListener();
    }

}
