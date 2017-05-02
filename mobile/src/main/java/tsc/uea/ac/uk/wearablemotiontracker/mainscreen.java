package tsc.uea.ac.uk.wearablemotiontracker;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import tsc.uea.ac.uk.hardwareaccess.DataListener;

public class mainscreen extends AppCompatActivity {

    //private DataListener listener;
    private TextView mTextView, mX, mY, mZ, gX, gY, gZ;
    private Handler handler;
    private ImageButton settingsButton;
    private EditText editText;
    private Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    //
    ARFFConverter converter;
    private boolean running;
    private static final long DURATION = 10000;
    private static final long INCREMENT = 100;
    private float [] accel;
    private float [] gyro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        //listener = new DataListener(getApplicationContext());
        DataListener.setActivity(this);
        handler = new Handler();
        mX = (TextView) findViewById(R.id.textX);
        mY = (TextView) findViewById(R.id.textY);
        mZ = (TextView) findViewById(R.id.textZ);
        gX = (TextView) findViewById(R.id.gyroX);
        gY = (TextView) findViewById(R.id.gyroY);
        gZ = (TextView) findViewById(R.id.gyroZ);
        spinner = (Spinner) findViewById(R.id.activitySpinner);
        editText = (EditText) findViewById(R.id.userID);
        populateSpinner(spinner);
        spinner.setOnItemSelectedListener(activitySpinnerListener);
        settingsButton = (ImageButton) findViewById(R.id.settings);
        converter = new ARFFConverter(getApplicationContext(), 100); //hardcoded for now can divide later
        accel = new float[3];
        gyro = new float[3];
        running = false;
        //handler.post(updateValuesThread);
        //data to send over the wire - needs to be attached to a listener really
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //SettingsBundle bundle = new SettingsBundle(prefs.getBoolean("accelOrGrav", false), prefs.getBoolean("gyroOrRotation", false));
        converter.close();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void settingsMenu(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void populateSpinner(final Spinner spinner){
        Runnable spinnerPopulation = new Runnable(){
            @Override
            public void run(){
                adapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.activities, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        };
        handler.post(spinnerPopulation);
    }


    private final AdapterView.OnItemSelectedListener activitySpinnerListener = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView parent, View view, int pos, long id) {
            String name = (String) parent.getItemAtPosition(pos);
            spinner.setSelection(pos);
            Log.d("SELECTION - ", name);
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
        }

        @Override
        public void onNothingSelected(AdapterView parent) {
            // Another interface callback
            spinner.setSelection(0);
        }
    };

    public void beginCapture(){
        converter.open();
        converter.write(editText.getText().toString() + ", ");
        recordValues.start();
    }


    /**
     * NOTE - Thread is currently unused, thanks to a memory leak.
     * As noted below, running a UI-thread
     */
    private Runnable updateValuesThread = new Runnable(){
        @Override
        public void run(){
                mX.setText("X - " + accel[0]);
                mY.setText("Y - " + accel[1]);
                mZ.setText("Z - " + accel[2]);
                gX.setText("X - " + gyro[0]);
                gY.setText("Y - " + gyro[1]);
                gZ.setText("Z - " + gyro[2]);
                //handler.post(this); //Note - any and all Android thread-based loops that don't utilise recursive handler calls need to call android looper
                //side note 2 - ideally we want to either use a handler looper or async task to do constant UI updates, but I'm pressed for time
        }
    };

    private CountDownTimer recordValues = new CountDownTimer(DURATION, INCREMENT){
        public void onTick(long millisUntilFinished) {
            accel = DataListener.getAccelData();
            gyro = DataListener.getGyroData();
            mX.setText("X - " + accel[0]);
            mY.setText("Y - " + accel[1]);
            mZ.setText("Z - " + accel[2]);
            gX.setText("X - " + gyro[0]);
            gY.setText("Y - " + gyro[1]);
            gZ.setText("Z - " + gyro[2]);
            converter.write("" + accel[0] + ", ");
            converter.write("" + accel[1] + ", ");
            converter.write("" + accel[2] + ", ");
        }

        public void onFinish() {
            Toast toast = Toast.makeText(getApplicationContext(), "Countdown Finished!", Toast.LENGTH_SHORT);
            toast.show();
            converter.write(spinner.getSelectedItem().toString() + "\n");
            converter.close();
        }
    };

}
