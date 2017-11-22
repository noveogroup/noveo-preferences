package com.noveogroup.preferenceentity;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java8.util.Optional;

public class TestSharedPreferences implements SharedPreferences {

    private static final Object TO_BE_REMOVED = new Object();
    private final Map<String, Object> storage = new HashMap<>();
    private final List<OnSharedPreferenceChangeListener> listenerList = new ArrayList<>();
    private boolean badFileSystem = false;

    public void setBadFileSystem(final boolean badFileSystem) {
        this.badFileSystem = badFileSystem;
    }

    @Override
    public Map<String, ?> getAll() {
        return storage;
    }

    @Nullable
    @Override
    public String getString(final String key, @Nullable final String defValue) {
        return uncheckedGet(key, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(final String key, @Nullable final Set<String> defValues) {
        return uncheckedGet(key, defValues);
    }

    @Override
    public int getInt(final String key, final int defValue) {
        return uncheckedGet(key, defValue);
    }

    @Override
    public long getLong(final String key, final long defValue) {
        return uncheckedGet(key, defValue);
    }

    @Override
    public float getFloat(final String key, final float defValue) {
        return uncheckedGet(key, defValue);
    }

    @Override
    public boolean getBoolean(final String key, final boolean defValue) {
        return uncheckedGet(key, defValue);
    }

    @Override
    public boolean contains(final String key) {
        return storage.containsKey(key);
    }

    @Override
    public Editor edit() {
        return new Editor() {

            private final Map<String, Object> temp = new HashMap<>();

            @Override
            public Editor putString(final String key, @Nullable final String value) {
                temp.put(key, value);
                return this;
            }

            @Override
            public Editor putStringSet(final String key, @Nullable final Set<String> values) {
                temp.put(key, values);
                return this;
            }

            @Override
            public Editor putInt(final String key, final int value) {
                temp.put(key, value);
                return this;
            }

            @Override
            public Editor putLong(final String key, final long value) {
                temp.put(key, value);
                return this;
            }

            @Override
            public Editor putFloat(final String key, final float value) {
                temp.put(key, value);
                return this;
            }

            @Override
            public Editor putBoolean(final String key, final boolean value) {
                temp.put(key, value);
                return this;
            }

            @Override
            public Editor remove(final String key) {
                temp.put(key, TO_BE_REMOVED);
                return this;
            }

            @Override
            public Editor clear() {
                for (final String key : storage.keySet()) {
                    temp.put(key, TO_BE_REMOVED);
                }
                return this;
            }

            @Override
            public boolean commit() {
                apply();
                return !badFileSystem;
            }

            @Override
            public void apply() {
                for (final String key : temp.keySet()) {
                    //apply
                    final Object value = temp.get(key);
                    if (value == TO_BE_REMOVED) {
                        storage.remove(key);
                    } else {
                        storage.put(key, value);
                    }

                    //notify
                    for (final OnSharedPreferenceChangeListener listener : listenerList) {
                        listener.onSharedPreferenceChanged(TestSharedPreferences.this, key);
                    }
                }
            }
        };
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener listener) {
        listenerList.remove(listener);
    }

    @SuppressWarnings("unchecked")
    private <T> T uncheckedGet(final String key, final T defaultValue) {
        if (storage.containsKey(key)) {
            return Optional.ofNullable((T) storage.get(key)).orElse(defaultValue);
        }
        return defaultValue;
    }
}
