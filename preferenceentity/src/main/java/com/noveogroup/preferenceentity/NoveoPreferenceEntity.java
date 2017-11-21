package com.noveogroup.preferenceentity;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.noveogroup.preferenceentity.api.PreferenceEntity;
import com.noveogroup.preferenceentity.api.ValueProvider;

import java.io.IOException;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java8.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings({"unused,WeakerAccess", "SameParameterValue"})
public class NoveoPreferenceEntity<T> implements PreferenceEntity<T> {

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
            log.debug('(' + key + ')' + ' ' + message, args);
        }
    }

    @Override
    public Optional<T> read() {
        return Optional.ofNullable(strategy.get(preferences, key, defaultValue));
    }

    @Override
    public void remove() throws IOException {
        save(null);
    }

    @Override
    public void save(@Nullable final T value) throws IOException {
        if (editPreferences(editor -> strategy.set(editor, key, value))) {
            log(key, "changed to {}", value);
        } else {
            throw new IOException("Something went really wrong. SharedPreferences.editor.commit() returns false. Check Your FS");
        }
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

    @SneakyThrows
    private boolean editPreferences(final Consumer<SharedPreferences.Editor> action) {
        final SharedPreferences.Editor editor = preferences.edit();
        action.accept(editor);
        return editor.commit();
    }
}
