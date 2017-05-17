package tsc.uea.ac.uk.wearablemotiontracker;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Jack L. Clements on 17/04/2017.
 */

/**
 * Fragment used for settings from preferences.
 */
public class SettingsFragment extends PreferenceFragment{
    /**
     * Called on creation of fragment
     * @param savedStateInstance state of application in bundle format
     */
    @Override
    public void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
        addPreferencesFromResource(R.xml.preferences);
    }

}
