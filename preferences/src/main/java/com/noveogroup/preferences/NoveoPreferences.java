package com.noveogroup.preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.noveogroup.preferences.api.Preference;

import java.util.Map;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
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

    public Preference<Boolean> getBoolean(final String key) {
        return getBoolean(key, false);
    }

    public Preference<Boolean> getBoolean(final String key, final boolean defaultValue) {
        return new ItemPreference<>(key, PreferenceStrategy.BOOLEAN, defaultValue, preferences);
    }

    public Preference<String> getString(final String key) {
        return getString(key, null);
    }

    public Preference<String> getString(final String key, @Nullable final String defaultValue) {
        return new ItemPreference<>(key, PreferenceStrategy.STRING, defaultValue, preferences);
    }

    public Preference<Float> getFloat(final String key) {
        return getFloat(key, Float.MIN_VALUE);
    }

    public Preference<Float> getFloat(final String key, final float defaultValue) {
        return new ItemPreference<>(key, PreferenceStrategy.FLOAT, defaultValue, preferences);
    }

    public Preference<Integer> getInt(final String key) {
        return getInt(key, Integer.MIN_VALUE);
    }

    public Preference<Integer> getInt(final String key, final int defaultValue) {
        return new ItemPreference<>(key, PreferenceStrategy.INTEGER, defaultValue, preferences);
    }

    public Preference<Long> getLong(final String key) {
        return getLong(key, Long.MIN_VALUE);
    }

    public Preference<Long> getLong(final String key, final long defaultValue) {
        return new ItemPreference<>(key, PreferenceStrategy.LONG, defaultValue, preferences);
    }

    public <T> Preference<T> getObject(final String key, final PreferenceStrategy<T> strategy) {
        return getObject(key, strategy, null);
    }

    public <T> Preference<T> getObject(final String key, final PreferenceStrategy<T> strategy, @Nullable final T defaultValue) {
        return new ItemPreference<>(key, strategy, defaultValue, preferences);
    }

    public Preference<Map<String, ?>> getAll() {
        return new AllPreference(preferences);
    }

}
