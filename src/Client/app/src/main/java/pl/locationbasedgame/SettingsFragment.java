package pl.locationbasedgame;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Patryk Ligenza on 03-May-17.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
