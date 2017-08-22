package br.com.endcraft.me.endcraft;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Created by JonasXPX on 14.ago.2017.
 */

public class Settings extends PreferenceActivity  {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_settings);
        PreferenceManager.setDefaultValues(this, R.xml.user_settings, false);
    }
}
