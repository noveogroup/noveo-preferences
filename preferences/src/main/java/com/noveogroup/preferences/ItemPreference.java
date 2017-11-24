package com.noveogroup.preferences;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.api.PreferenceProvider;
import com.noveogroup.preferences.guava.Optional;
import com.noveogroup.preferences.lambda.Function;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings({"unused,WeakerAccess", "SameParameterValue"})
class ItemPreference<T> extends LogPreference implements Preference<T> {

    private final SharedPreferences preferences;
    private final String key;
    private final PreferenceStrategy<T> strategy;

    @Nullable
    private final T defaultValue;
    @Nullable
    private PreferenceProvider<T> providerDelegate;

    ItemPreference(final String key, final PreferenceStrategy<T> strategy, final SharedPreferences preferences) {
        this(key, strategy, null, preferences);
    }

    ItemPreference(final String key, final PreferenceStrategy<T> strategy, @Nullable final T defaultValue, final SharedPreferences preferences) {
        super(key);

        this.key = key;
        this.strategy = strategy;
        this.defaultValue = defaultValue;
        this.preferences = preferences;

        strategy.checkNullOrThrow(defaultValue);
    }

    @Override
    public Optional<T> read() {
        if (strategy.canBeNull) {
            return Optional.fromNullable(strategy.get(preferences, key, defaultValue));
        }
        return Optional.of(strategy.get(preferences, key, defaultValue));
    }

    @Override
    public void remove() {
        logOrThrowSneaky(
                Utils.editPreferences(preferences, editor -> strategy.remove(editor, key)),
                "removed");
    }

    @Override
    public void save(@Nullable final T value) {
        strategy.checkNullOrThrow(value);
        logOrThrowSneaky(
                Utils.editPreferences(preferences, editor -> strategy.set(editor, key, value)),
                "changed to {}", value);
    }

    @Override
    public PreferenceProvider<T> provider() {
        if (providerDelegate == null) {
            final Function<String, Boolean> currentKeyFilter = compareKey -> strategy.keyFilter.compare(compareKey, key);
            providerDelegate = new NoveoPreferenceProvider<>(this, preferences, currentKeyFilter);
        }
        return providerDelegate;
    }
}
