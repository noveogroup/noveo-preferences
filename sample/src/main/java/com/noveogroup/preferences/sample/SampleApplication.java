package com.noveogroup.preferences.sample;


import android.app.Application;

import com.noveogroup.preferenceentity.NoveoPreferences;
import com.noveogroup.preferences.sample.data.CommonPreferences;

public class SampleApplication extends Application {

    public static CommonPreferences COMMON_PREFERENCES;

    @Override
    public void onCreate() {
        super.onCreate();

        NoveoPreferences.setDebug(true);
        COMMON_PREFERENCES = new CommonPreferences(this);
    }
}
