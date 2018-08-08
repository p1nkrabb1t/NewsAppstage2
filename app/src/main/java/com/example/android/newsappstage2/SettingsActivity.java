package com.example.android.newsappstage2;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class newsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);


            Preference categories = findPreference(getString(R.string.settings_key_categories));
            bindPreferenceSummaryToValue(categories);

            Preference resultsLimit = findPreference(getString(R.string.settings_key_results_limit));
            bindPreferenceSummaryToValue(resultsLimit);
        }

        @Override
        public boolean onPreferenceChange(Preference pref, Object value) {
            // display current user settings
            String stringValue = value.toString();

            if (pref instanceof ListPreference) {
                ListPreference listPref = (ListPreference) pref;
                int prefIndex = listPref.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPref.getEntries();
                    pref.setSummary(labels[prefIndex]);
                }
            } else {
                pref.setSummary(stringValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference pref) {
            pref.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(pref.getContext());
            String preferenceString = sharedPrefs.getString(pref.getKey(), "");
            onPreferenceChange(pref, preferenceString);
        }

    }
}