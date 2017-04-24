package tsc.uea.ac.uk.wearablemotiontracker;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import tsc.uea.ac.uk.hardwareaccess.DataListener;

public class mainscreen extends AppCompatActivity {

    //private DataListener listener;
    private TextView mTextView, mX, mY, mZ, gX, gY, gZ;
    private Handler handler;
    private ImageButton settingsButton;
    private Spinner spinner;
    //
    ARFFConverter converter;
    private boolean running;

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
        populateSpinner(spinner);
        spinner.setOnItemSelectedListener(activitySpinnerListener);
        settingsButton = (ImageButton) findViewById(R.id.settings);
        converter = new ARFFConverter(getApplicationContext());
        converter.write("Test phrase");
        running = false;
        handler.postDelayed(updateValuesThread, 500);
        //data to send over the wire - needs to be attached to a listener really
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //SettingsBundle bundle = new SettingsBundle(prefs.getBoolean("accelOrGrav", false), prefs.getBoolean("gyroOrRotation", false));
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
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.activities, android.R.layout.simple_spinner_item);
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
        running = true;
        handler.postDelayed(updateValuesThread, 500);
    }


    private Runnable updateValuesThread = new Runnable(){
        @Override
        public void run(){
            //get thing
            //set text
            float [] values = DataListener.getAccelData();
            float [] values2 = DataListener.getGyroData();
            mX.setText("X - " + values[0]);
            mY.setText("Y - " + values[1]);
            mZ.setText("Z - " + values[2]);
            gX.setText("X - " + values2[0]);
            gY.setText("Y - " + values2[1]);
            gZ.setText("Z - " + values2[2]);
            converter.write("" + values[0]);
            converter.write("" + values[1]);
            converter.write("" + values[2]);
            converter.close();
            handler.postDelayed(this, 500); //calling this inside run essentially ensures run() will run again
            //there has to be a better way of looping this, surely?
        }
    };

}
