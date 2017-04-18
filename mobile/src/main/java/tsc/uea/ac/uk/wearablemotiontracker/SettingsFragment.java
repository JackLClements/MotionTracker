package tsc.uea.ac.uk.wearablemotiontracker;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Jack L. Clements on 17/04/2017.
 */

public class SettingsFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
        addPreferencesFromResource(R.xml.preferences);
    }

}
