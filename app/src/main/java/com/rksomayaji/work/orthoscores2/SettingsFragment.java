package com.rksomayaji.work.orthoscores2;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * Created by sushanth on 10/15/16.
 */

public class SettingsFragment extends PreferenceFragment {
    CheckBoxPreference autoUpdate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
