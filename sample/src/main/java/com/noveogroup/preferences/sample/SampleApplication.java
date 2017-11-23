package com.noveogroup.preferences.sample;


import android.app.Application;

import com.noveogroup.preferences.NoveoPreferences;
import com.noveogroup.preferences.sample.data.CommonPreferences;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@Slf4j
public class SampleApplication extends Application {

    public static CommonPreferences commonPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        NoveoPreferences.setDebug(true);
        commonPreferences = new CommonPreferences(this);

        commonPreferences.all.rx().provider().observe(
                snapshot -> snapshot.ifPresentOrElse(this::logKeys, this::logEmptyKeys));
    }

    private void logEmptyKeys() {
        log.debug("==== PREFERENCES SNAPSHOT (EMPTY) ====");
    }

    private void logKeys(final Map<String, ?> map) {
        log.debug("==== PREFERENCES SNAPSHOT ====");
        for (final String key : map.keySet()) {
            log.debug("{} -> {}", key, map.get(key));
        }
        log.debug("==============================");
    }

}
