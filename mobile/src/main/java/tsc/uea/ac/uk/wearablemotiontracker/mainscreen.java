package tsc.uea.ac.uk.wearablemotiontracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import tsc.uea.ac.uk.hardwareaccess.DataListener;
import tsc.uea.ac.uk.shared.SettingsBundle;

public class mainscreen extends AppCompatActivity {

    //private DataListener listener;
    private TextView mTextView, mX, mY, mZ, gX, gY, gZ;
    private Handler handler;
    private ImageButton settingsButton;

    //
    ARFFConverter converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        //listener = new DataListener(getApplicationContext());
        handler = new Handler();
        mX = (TextView) findViewById(R.id.textX);
        mY = (TextView) findViewById(R.id.textY);
        mZ = (TextView) findViewById(R.id.textZ);
        gX = (TextView) findViewById(R.id.gyroX);
        gY = (TextView) findViewById(R.id.gyroY);
        gZ = (TextView) findViewById(R.id.gyroZ);
        settingsButton = (ImageButton) findViewById(R.id.settings);
        converter = new ARFFConverter(getApplicationContext());
        converter.write("Test phrase");
        converter.close();
        //converter.close();
        //data to send over the wire - needs to be attached to a listener really
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //SettingsBundle bundle = new SettingsBundle(prefs.getBoolean("accelOrGrav", false), prefs.getBoolean("gyroOrRotation", false));
        handler.postDelayed(updateValuesThread, 500);
    }

    public void settingsMenu(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
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
            //converter.write("" + values[0]);
            //converter.write("" + values[1]);
            //converter.write("" + values[2]);
            //converter.close();
            handler.postDelayed(this, 500); //calling this inside run essentially ensures run() will run again
            //there has to be a better way of looping this, surely?
        }
    };

}
