package com.noveogroup.preferenceentity;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.noveogroup.preferenceentity.api.PreferenceEntity;
import com.noveogroup.preferenceentity.api.ValueProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import io.reactivex.functions.Function;
import java8.util.Optional;

@SuppressWarnings({"unused,WeakerAccess", "SameParameterValue"})
public class NoveoPreferenceEntity<T> implements PreferenceEntity<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoveoPreferenceEntity.class);

    private final SharedPreferences preferences;
    private final String key;
    private final PreferenceStrategy<T> strategy;

    @Nullable
    private final T defaultValue;
    @Nullable
    private NoveoRxPreferenceEntity<T> rxEntity;
    @Nullable
    private ValueProvider<T> providerDelegate;

    NoveoPreferenceEntity(final String key, final PreferenceStrategy<T> strategy, final SharedPreferences preferences) {
        this(key, strategy, null, preferences);
    }

    NoveoPreferenceEntity(final String key, final PreferenceStrategy<T> strategy, @Nullable final T defaultValue, final SharedPreferences preferences) {
        this.key = key;
        this.strategy = strategy;
        this.defaultValue = defaultValue;
        this.preferences = preferences;
    }

    private static void log(final String key, final String message, final Object... args) {
        if (NoveoPreferences.isDebug()) {
            LOGGER.debug('(' + key + ')' + ' ' + message, args);
        }
    }

    @Override
    public Optional<T> read() {
        return Optional.ofNullable(strategy.get(preferences, key, defaultValue));
    }

    @Override
    public void remove() throws IOException {
        logOrThrow(
                Utils.editPreferences(preferences, editor -> strategy.remove(editor, key)),
                "removed");
    }

    @Override
    public void save(@Nullable final T value) throws IOException {
        logOrThrow(
                Utils.editPreferences(preferences, editor -> strategy.set(editor, key, value)),
                "changed to {}", value);
    }

    @Override
    public NoveoRxPreferenceEntity<T> rx() {
        if (rxEntity == null) {
            rxEntity = new NoveoRxPreferenceEntity<>(this);
        }
        return rxEntity;
    }

    @Override
    public ValueProvider<T> provider() {
        if (providerDelegate == null) {
            final Function<String, Boolean> currentKeyFilter = compareKey -> strategy.keyFilter.compare(compareKey, key);
            providerDelegate = new NoveoValueProvider<>(this, preferences, currentKeyFilter);
        }
        return providerDelegate;
    }

    private void logOrThrow(final boolean log, final String message, final Object... args) throws IOException {
        if (log) {
            log(key, "removed", args);
        } else {
            throw new IOException("Something went really wrong. SharedPreferences.editor.commit() returns false. Check Your FS");
        }
    }
}
