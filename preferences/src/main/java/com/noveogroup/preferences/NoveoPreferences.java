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

    /**
     * if true - library will use @Slf4j {@link org.slf4j.Logger} to write logs.
     *
     * @return true if enabled. false otherwise.
     */
    public static boolean isDebugEnabled() {
        return debug;
    }

    /**
     * Enabled logging with @Slf4j {@link org.slf4j.Logger}
     * @param debug boolean to enable.
     */
    public static void setDebug(final boolean debug) {
        NoveoPreferences.debug = debug;
    }

    /**
     * Get Boolean preference. Can't be null. Default Value is false.
     * If you want to override it - use {@link #getBoolean(String, boolean)}.
     * @param key to be stored under.
     * @return {@link Preference} to perform operations.
     */
    public Preference<Boolean> getBoolean(final String key) {
        return getBoolean(key, false);
    }

    /**
     * Get Boolean preference. Can't be null.
     * @param key to be stored under.
     * @param defaultValue return that value if not present.
     * @return {@link Preference} to perform operations.
     */
    public Preference<Boolean> getBoolean(final String key, final boolean defaultValue) {
        return new ItemPreference<>(key, PreferenceStrategy.BOOLEAN, defaultValue, preferences);
    }

    /**
     * Get String preference. Nullable. Default Value is null.
     * If you want to override default value - use {@link #getString(String, String)}.
     * @param key to be stored under.
     * @return {@link Preference} to perform operations.
     */
    public Preference<String> getString(final String key) {
        return getString(key, null);
    }

    /**
     * Get String preference. Nullable.
     * @param key to be stored under.
     * @param defaultValue return that value if not present.
     * @return {@link Preference} to perform operations.
     */
    public Preference<String> getString(final String key, @Nullable final String defaultValue) {
        return new ItemPreference<>(key, PreferenceStrategy.STRING, defaultValue, preferences);
    }

    /**
     * Get Float preference. Can't be null. Default Value is {@link Float#MIN_VALUE}.
     * If you want to override default value - use {@link #getFloat(String, float)}.
     * @param key to be stored under.
     * @return {@link Preference} to perform operations.
     */
    public Preference<Float> getFloat(final String key) {
        return getFloat(key, Float.MIN_VALUE);
    }

    /**
     * Get Float preference. Can't be null.
     * @param key to be stored under.
     * @param defaultValue return that value if not present.
     * @return {@link Preference} to perform operations.
     */
    public Preference<Float> getFloat(final String key, final float defaultValue) {
        return new ItemPreference<>(key, PreferenceStrategy.FLOAT, defaultValue, preferences);
    }

    /**
     * Get Int preference. Can't be null. Default Value is {@link Integer#MIN_VALUE}.
     * If you want to override default value - use {@link #getInt(String, int)}.
     * @param key to be stored under.
     * @return {@link Preference} to perform operations.
     */
    public Preference<Integer> getInt(final String key) {
        return getInt(key, Integer.MIN_VALUE);
    }

    /**
     * Get Integer preference. Can't be null.
     * @param key to be stored under.
     * @param defaultValue return that value if not present.
     * @return {@link Preference} to perform operations.
     */
    public Preference<Integer> getInt(final String key, final int defaultValue) {
        return new ItemPreference<>(key, PreferenceStrategy.INTEGER, defaultValue, preferences);
    }

    /**
     * Get Long preference. Can't be null. Default Value is {@link Long#MIN_VALUE}.
     * If you want to override default value - use {@link #getLong(String, long)}.
     * @param key to be stored under.
     * @return {@link Preference} to perform operations.
     */
    public Preference<Long> getLong(final String key) {
        return getLong(key, Long.MIN_VALUE);
    }

    /**
     * Get Long preference. Can't be null.
     * @param key to be stored under.
     * @param defaultValue return that value if not present.
     * @return {@link Preference} to perform operations.
     */
    public Preference<Long> getLong(final String key, final long defaultValue) {
        return new ItemPreference<>(key, PreferenceStrategy.LONG, defaultValue, preferences);
    }

    /**
     * Get Any Generic preference. Nullability depends on used {@link PreferenceStrategy}.
     * Default Value is null.
     * If you want to override default value - use {@link #getObject(String, PreferenceStrategy, Object)}.
     * <p>throws {@link NullPointerException} if strategy doesn't allow nulls.</p>
     * @param <T> Preference generic.
     * @param key to be stored under.
     * @param strategy {@link PreferenceStrategy} that describes how preference should be
     * serialized/deserialized/removed or even notified on changes or its nullability.
     * @return {@link Preference} to perform operations.
     */
    public <T> Preference<T> getObject(final String key, final PreferenceStrategy<T> strategy) {
        return getObject(key, strategy, null);
    }

    /**
     * Get Any Generic preference. Nullability depends on used {@link PreferenceStrategy}.
     * <p>throws {@link NullPointerException} if strategy doesn't allow nulls.</p>
     * @param <T> Preference generic.
     * @param key to be stored under.
     * @param strategy {@link PreferenceStrategy} that describes how preference should be
     * serialized/deserialized/removed or even notified on changes or its nullability.
     * @param defaultValue return that value if not present.
     * @return {@link Preference} to perform operations.
     */
    public <T> Preference<T> getObject(final String key, final PreferenceStrategy<T> strategy, @Nullable final T defaultValue) {
        return new ItemPreference<>(key, strategy, defaultValue, preferences);
    }

    /**
     * Get Any Preferences watcher.
     * <p>Be careful, this {@link Preference} implementation doesn't support {@link Preference#save(Object)} operation</p>
     *
     * But you still able to use
     * <ul>
     *     <li>{@link Preference#remove()}</li>
     *     <li>{@link Preference#read()}</li>
     *     <li>{@link Preference#provider()}</li>
     * </ul>
     * @return {@link Preference} to perform operations.
     */
    public Preference<Map<String, ?>> getAll() {
        return new AllPreference(preferences);
    }

}
