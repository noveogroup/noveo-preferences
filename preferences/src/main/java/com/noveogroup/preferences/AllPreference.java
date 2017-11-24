package com.noveogroup.preferences;

import android.content.SharedPreferences;

import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.api.PreferenceProvider;
import com.noveogroup.preferences.api.RxPreference;

import java.util.Map;

import java8.util.Optional;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings("UnusedReturnValue")
class AllPreference extends LogPreference implements Preference<Map<String, ?>> {

    private final SharedPreferences preferences;
    private RxPreference<Map<String, ?>> rxPreference;
    private PreferenceProvider<Map<String, ?>> preferenceProvider;

    AllPreference(final SharedPreferences preferences) {
        super("");
        this.preferences = preferences;
    }

    @Override
    public void save(final Map<String, ?> value) {
        throw new UnsupportedOperationException("You can't save All preference. It is read/remove only");
    }

    @Override
    public void remove() {
        // 1. Remove VALUES by keys to invoke observer notifications.
        boolean result = Utils.editPreferences(preferences, editor -> read().ifPresent(map -> {
            for (final String key : map.keySet()) {
                editor.remove(key);
            }
        }));

        // 2. Remove KEYS (won't notify observers)
        result &= Utils.editPreferences(preferences, SharedPreferences.Editor::clear);

        // 3. Throw error if problems with commit.
        logOrThrowSneaky(result, "remove all");
    }

    @Override
    public Optional<Map<String, ?>> read() {
        return Optional.ofNullable(preferences.getAll());
    }

    @Override
    public RxPreference<Map<String, ?>> rx() {
        if (rxPreference == null) {
            rxPreference = new NoveoRxPreference<>(this);
        }
        return rxPreference;
    }

    @Override
    public PreferenceProvider<Map<String, ?>> provider() {
        if (preferenceProvider == null) {
            preferenceProvider = new NoveoPreferenceProvider<>(this, preferences, key -> true);
        }
        return preferenceProvider;
    }
}
