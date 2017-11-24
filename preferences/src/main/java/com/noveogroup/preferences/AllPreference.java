package com.noveogroup.preferences;

import android.content.SharedPreferences;

import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.api.PreferenceProvider;
import com.noveogroup.preferences.api.RxPreference;

import java.io.IOException;
import java.util.Map;

import java8.util.Optional;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings("UnusedReturnValue")
class AllPreference implements Preference<Map<String, ?>> {

    private final SharedPreferences preferences;
    private RxPreference<Map<String, ?>> rxPreference;
    private PreferenceProvider<Map<String, ?>> preferenceProvider;

    AllPreference(final SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void save(final Map<String, ?> value) throws IOException {
        throw new UnsupportedOperationException("You can't save All preference. It is read/remove only");
    }

    @Override
    public void remove() throws IOException {
        // 1. Remove VALUES by keys to invoke observer notifications.
        Utils.editPreferences(preferences, editor -> read().ifPresent(map -> {
            for (final String key : map.keySet()) {
                editor.remove(key);
            }
        }));

        // 2. Remove KEYS (won't notify observers)
        Utils.editPreferences(preferences, SharedPreferences.Editor::clear);
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
