package com.noveogroup.preferenceentity;

import android.content.SharedPreferences;

import com.noveogroup.preferenceentity.api.PreferenceEntity;
import com.noveogroup.preferenceentity.api.ValueProvider;

import java.io.IOException;
import java.util.Map;

import io.reactivex.functions.Consumer;
import java8.util.Optional;
import lombok.SneakyThrows;

@SuppressWarnings("UnusedReturnValue")
public class AllPreferenceEntity implements PreferenceEntity<Map<String, ?>> {

    private final SharedPreferences preferences;
    private NoveoRxPreferenceEntity<Map<String, ?>> rxPreferenceEntity;
    private NoveoValueProvider<Map<String, ?>> valueProvider;

    AllPreferenceEntity(final SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void save(final Map<String, ?> value) throws IOException {
        throw new UnsupportedOperationException("You can't save All preference. It is read/remove only");
    }

    @Override
    public void remove() throws IOException {
        // 1. Remove VALUES by keys to invoke observer notifications.
        editPreferences(editor -> read().ifPresent(map -> {
            for (final String key : map.keySet()) {
                editor.remove(key);
            }
        }));

        // 2. Remove KEYS (won't notify observers)
        editPreferences(SharedPreferences.Editor::clear);
    }

    @Override
    public Optional<Map<String, ?>> read() {
        return Optional.ofNullable(preferences.getAll());
    }

    @Override
    public NoveoRxPreferenceEntity<Map<String, ?>> rx() {
        if (rxPreferenceEntity == null) {
            rxPreferenceEntity = new NoveoRxPreferenceEntity<>(this);
        }
        return rxPreferenceEntity;
    }

    @Override
    public ValueProvider<Map<String, ?>> provider() {
        if (valueProvider == null) {
            valueProvider = new NoveoValueProvider<>(this, preferences, key -> true);
        }
        return valueProvider;
    }

    @SneakyThrows
    private boolean editPreferences(final Consumer<SharedPreferences.Editor> action) {
        final SharedPreferences.Editor editor = preferences.edit();
        action.accept(editor);
        return editor.commit();
    }
}
