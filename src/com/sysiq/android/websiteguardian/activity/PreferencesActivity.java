package com.sysiq.android.websiteguardian.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.example.WebSiteGuardian.R;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 14.02.13
 * Time: 16:03
 */
public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }
}
