package com.noveogroup.preferences.mock;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.noveogroup.preferences.guava.Optional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class TestSharedPreferences implements SharedPreferences {

    static final Object TO_BE_REMOVED = new Object();
    final List<OnSharedPreferenceChangeListener> listenerList = new ArrayList<>();
    final Map<String, Object> storage = new HashMap<>();
    boolean badFileSystem;

    public void setBadFileSystem() {
        this.badFileSystem = true;
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
        return new TestEditor();
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
            final Optional<T> secondChoice = Optional.fromNullable(defaultValue);
            return Optional.fromNullable((T) storage.get(key))
                    .or(secondChoice)
                    .orNull();
        }
        return defaultValue;
    }

    class TestEditor implements Editor {

        final Map<String, Object> temp = new HashMap<>();

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
                if (TO_BE_REMOVED.equals(value)) {
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
    }
}
