package com.noveogroup.preferences;

import android.content.SharedPreferences;

import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.api.PreferenceProvider;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java8.util.Optional;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
public class NoveoPreferenceProvider<T> implements PreferenceProvider<T> {

    private final Preference<T> entity;
    private final SharedPreferences preferences;

    private final Map<Consumer, SharedPreferences.OnSharedPreferenceChangeListener> changeListeners = new HashMap<>();
    private final Function<String, Boolean> filter;

    NoveoPreferenceProvider(final Preference<T> entity, final SharedPreferences preferences, final Function<String, Boolean> filter) {
        this.entity = entity;
        this.preferences = preferences;
        this.filter = filter;
    }

    @Override
    public void addListener(final Consumer<Optional<T>> changeListener) {
        final SharedPreferences.OnSharedPreferenceChangeListener listener =
                (sharedPreferences, changedKey) -> notifyListener(changeListener, changedKey);
        changeListeners.put(changeListener, listener);
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    private void notifyListener(final Consumer<Optional<T>> changeListener, final String changedKey) {
        try {
            if (filter.apply(changedKey)) {
                changeListener.accept(entity.read());
            }
        } catch (final Exception original) {
            Utils.sneakyThrow(original);
        }
    }

    @Override
    public void removeListener(final Consumer<Optional<T>> changeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(changeListeners.remove(changeListener));
    }

}
