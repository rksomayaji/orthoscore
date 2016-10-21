package com.rksomayaji.work.orthoscores2;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by sushanth on 10/15/16.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
