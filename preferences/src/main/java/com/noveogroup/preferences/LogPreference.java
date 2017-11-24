package com.noveogroup.preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by avaytsekhovskiy on 24/11/2017.
 */

@SuppressWarnings("SameParameterValue")
class LogPreference {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String key;

    LogPreference(final String key) {
        this.key = key;
    }

    void logOrThrowSneaky(final boolean log, final String message, final Object... args) {
        if (log) {
            if (NoveoPreferences.isDebugEnabled()) {
                logger.debug('(' + key + ')' + ' ' + message, args);
            }
        } else {
            Utils.sneakyThrow(new IOException("Something went really wrong. SharedPreferences.editor.commit() returns false. Check Your FS"));
        }
    }
}
