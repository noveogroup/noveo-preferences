package com.noveogroup.preferenceentity;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

public class TestSharedPreferences implements SharedPreferences {
    @Override
    public Map<String, ?> getAll() {
        return null;
    }

    @Nullable
    @Override
    public String getString(final String key, @Nullable final String defValue) {
        return null;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(final String key, @Nullable final Set<String> defValues) {
        return null;
    }

    @Override
    public int getInt(final String key, final int defValue) {
        return 0;
    }

    @Override
    public long getLong(final String key, final long defValue) {
        return 0;
    }

    @Override
    public float getFloat(final String key, final float defValue) {
        return 0;
    }

    @Override
    public boolean getBoolean(final String key, final boolean defValue) {
        return false;
    }

    @Override
    public boolean contains(final String key) {
        return false;
    }

    @Override
    public Editor edit() {
        return null;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener listener) {

    }
}
