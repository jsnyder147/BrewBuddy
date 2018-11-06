package com.jsnyder147.brewbuddy;

import android.os.Bundle;
import android.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Load preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
