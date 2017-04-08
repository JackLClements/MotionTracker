package tsc.uea.ac.uk.wearablemotiontracker;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import tsc.uea.ac.uk.hardwareaccess.DataListener;

public class mainscreen extends AppCompatActivity {

    private DataListener listener;
    private TextView mTextView, mX, mY, mZ;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        listener = new DataListener();
        handler = new Handler();
        mTextView = (TextView) findViewById(R.id.text);
        mX = (TextView) findViewById(R.id.textX);
        mY = (TextView) findViewById(R.id.textY);
        mZ = (TextView) findViewById(R.id.textZ);
        handler.postDelayed(updateValuesThread, 500);
    }

    private Runnable updateValuesThread = new Runnable(){
        @Override
        public void run(){
            //get thing
            //set text
            float [] values = listener.getAccelData();
            mX.setText("X - " + values[0]);
            mY.setText("Y - " + values[1]);
            mZ.setText("Z - " + values[2]);
            handler.postDelayed(this, 500); //calling this inside run essentially ensures run() will run again
            //there has to be a better way of looping this, surely?
        }
    };

}
