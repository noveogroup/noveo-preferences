package com.noveogroup.preferenceentity;

import android.content.SharedPreferences;

import com.noveogroup.preferenceentity.api.PreferenceEntity;
import com.noveogroup.preferenceentity.api.ValueProvider;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java8.util.Optional;

public class NoveoValueProvider<T> implements ValueProvider<T> {

    private final PreferenceEntity<T> entity;
    private final SharedPreferences preferences;

    private final Map<Consumer, SharedPreferences.OnSharedPreferenceChangeListener> changeListeners = new HashMap<>();
    private final Function<String, Boolean> filter;

    NoveoValueProvider(final PreferenceEntity<T> entity, final SharedPreferences preferences, final Function<String, Boolean> filter) {
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
