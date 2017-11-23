package com.noveogroup.preferenceentity;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.noveogroup.preferenceentity.api.PreferenceEntity;

import java.util.Map;

@SuppressWarnings({"SameParameterValue", "WeakerAccess", "unused"})
public class NoveoPreferences {

    private static boolean debug;
    private final SharedPreferences preferences;

    /**
     * Create SharedPreferences wrapper.
     *
     * <p>Uses default application shared preferences with package name.</p>
     *
     * @param context to get access to SharedPreferences.
     */
    public NoveoPreferences(final Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    /**
     * Create SharedPreferences wrapper.
     *
     * <p>Uses custom shared preferences file.</p>
     *
     * @param context to get access to SharedPreferences.
     * @param fileName postfix for preference file name.
     */
    public NoveoPreferences(final Context context, final String fileName) {
        preferences = context.getSharedPreferences(context.getPackageName() + '.' + fileName, Context.MODE_PRIVATE);
    }

    /**
     * Create SharedPreferences wrapper.
     *
     * <p>Use these exact shared preferences.</p>
     *
     * @param preferences to be wrapped.
     */
    public NoveoPreferences(final SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static boolean isDebugEnabled() {
        return debug;
    }

    public static void setDebug(final boolean debug) {
        NoveoPreferences.debug = debug;
    }

    public PreferenceEntity<Boolean> getBoolean(final String key) {
        return getBoolean(key, false);
    }

    public PreferenceEntity<Boolean> getBoolean(final String key, final boolean defaultValue) {
        return new NoveoPreferenceEntity<>(key, PreferenceStrategy.BOOLEAN, defaultValue, preferences);
    }

    public PreferenceEntity<String> getString(final String key) {
        return getString(key, null);
    }

    public PreferenceEntity<String> getString(final String key, @Nullable final String defaultValue) {
        return new NoveoPreferenceEntity<>(key, PreferenceStrategy.STRING, defaultValue, preferences);
    }

    public PreferenceEntity<Float> getFloat(final String key) {
        return getFloat(key, Float.MIN_VALUE);
    }

    public PreferenceEntity<Float> getFloat(final String key, final float defaultValue) {
        return new NoveoPreferenceEntity<>(key, PreferenceStrategy.FLOAT, defaultValue, preferences);
    }

    public PreferenceEntity<Integer> getInt(final String key) {
        return getInt(key, Integer.MIN_VALUE);
    }

    public PreferenceEntity<Integer> getInt(final String key, final int defaultValue) {
        return new NoveoPreferenceEntity<>(key, PreferenceStrategy.INTEGER, defaultValue, preferences);
    }

    public PreferenceEntity<Long> getLong(final String key) {
        return getLong(key, Long.MIN_VALUE);
    }

    public PreferenceEntity<Long> getLong(final String key, final long defaultValue) {
        return new NoveoPreferenceEntity<>(key, PreferenceStrategy.LONG, defaultValue, preferences);
    }

    public <T> PreferenceEntity<T> getObject(final String key, final PreferenceStrategy<T> strategy) {
        return getObject(key, strategy, null);
    }

    public <T> PreferenceEntity<T> getObject(final String key, final PreferenceStrategy<T> strategy, @Nullable final T defaultValue) {
        return new NoveoPreferenceEntity<>(key, strategy, defaultValue, preferences);
    }

    public PreferenceEntity<Map<String, ?>> getAll() {
        return new AllPreferenceEntity(preferences);
    }

}
