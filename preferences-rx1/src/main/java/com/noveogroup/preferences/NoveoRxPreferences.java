package com.noveogroup.preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.api.RxPreference;

import java.util.Map;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess", "unused"})
public class NoveoRxPreferences {

    private final NoveoPreferences preferences;

    /**
     * Create SharedPreferences wrapper.
     * <p>
     * <p>Uses default application shared preferences with package name.</p>
     *
     * @param context to get access to SharedPreferences.
     */
    public NoveoRxPreferences(final Context context) {
        preferences = new NoveoPreferences(context);
    }

    /**
     * Create SharedPreferences wrapper.
     * <p>
     * <p>Uses custom shared preferences file.</p>
     *
     * @param context  to get access to SharedPreferences.
     * @param fileName postfix for preference file name.
     */
    public NoveoRxPreferences(final Context context, final String fileName) {
        preferences = new NoveoPreferences(context, fileName);
    }

    /**
     * Create SharedPreferences wrapper.
     * <p>
     * <p>Use these exact shared preferences.</p>
     *
     * @param preferences to be wrapped.
     */
    public NoveoRxPreferences(final SharedPreferences preferences) {
        this.preferences = new NoveoPreferences(preferences);
    }

    /**
     * Create SharedPreferences wrapper.
     * <p>
     * <p>Use NoveoPreferences as source.</p>
     *
     * @param preferences to be wrapped.
     */
    public NoveoRxPreferences(final NoveoPreferences preferences) {
        this.preferences = preferences;
    }

    /**
     * Wrap synchronous {@link Preference} to {@link RxPreference} variant.
     *
     * @param preference source.
     * @param <T>        generic type.
     * @return {@link RxPreference} wrapper.
     */
    public static <T> RxPreference<T> rx(final Preference<T> preference) {
        return new NoveoRxPreference<>(preference);
    }

    /**
     * if true - library will use @Slf4j {@link org.slf4j.Logger} to write logs.
     *
     * @return true if enabled. false otherwise.
     */
    public static boolean isDebugEnabled() {
        return NoveoPreferences.isDebugEnabled();
    }

    /**
     * Enabled logging with @Slf4j {@link org.slf4j.Logger}
     *
     * @param debug boolean to enable.
     */
    public static void setDebug(final boolean debug) {
        NoveoPreferences.setDebug(debug);
    }

    /**
     * Get non-rx NoveoPreferences.
     *
     * @return {@link NoveoPreferences} instance.
     */
    public NoveoPreferences blocking() {
        return preferences;
    }

    /**
     * Get Boolean preference. Can't be null. Default Value is false.
     * If you want to override it - use {@link #getBoolean(String, boolean)}.
     *
     * @param key to be stored under.
     * @return {@link RxPreference} to perform operations.
     */
    public RxPreference<Boolean> getBoolean(final String key) {
        return rx(preferences.getBoolean(key));
    }

    /**
     * Get Boolean preference. Can't be null.
     *
     * @param key          to be stored under.
     * @param defaultValue return that value if not present.
     * @return {@link RxPreference} to perform operations.
     */
    public RxPreference<Boolean> getBoolean(final String key, final boolean defaultValue) {
        return rx(preferences.getBoolean(key, defaultValue));
    }

    /**
     * Get String preference. Nullable. Default Value is null.
     * If you want to override default value - use {@link #getString(String, String)}.
     *
     * @param key to be stored under.
     * @return {@link RxPreference} to perform operations.
     */
    public RxPreference<String> getString(final String key) {
        return rx(preferences.getString(key));
    }

    /**
     * Get String preference. Nullable.
     *
     * @param key          to be stored under.
     * @param defaultValue return that value if not present.
     * @return {@link RxPreference} to perform operations.
     */
    public RxPreference<String> getString(final String key, @Nullable final String defaultValue) {
        return rx(preferences.getString(key, defaultValue));
    }

    /**
     * Get Float preference. Can't be null. Default Value is {@link Float#MIN_VALUE}.
     * If you want to override default value - use {@link #getFloat(String, float)}.
     *
     * @param key to be stored under.
     * @return {@link RxPreference} to perform operations.
     */
    public RxPreference<Float> getFloat(final String key) {
        return rx(preferences.getFloat(key));
    }

    /**
     * Get Float preference. Can't be null.
     *
     * @param key          to be stored under.
     * @param defaultValue return that value if not present.
     * @return {@link RxPreference} to perform operations.
     */
    public RxPreference<Float> getFloat(final String key, final float defaultValue) {
        return rx(preferences.getFloat(key, defaultValue));
    }

    /**
     * Get Int preference. Can't be null. Default Value is {@link Integer#MIN_VALUE}.
     * If you want to override default value - use {@link #getInt(String, int)}.
     *
     * @param key to be stored under.
     * @return {@link RxPreference} to perform operations.
     */
    public RxPreference<Integer> getInt(final String key) {
        return rx(preferences.getInt(key));
    }

    /**
     * Get Integer preference. Can't be null.
     *
     * @param key          to be stored under.
     * @param defaultValue return that value if not present.
     * @return {@link RxPreference} to perform operations.
     */
    public RxPreference<Integer> getInt(final String key, final int defaultValue) {
        return rx(preferences.getInt(key, defaultValue));
    }

    /**
     * Get Long preference. Can't be null. Default Value is {@link Long#MIN_VALUE}.
     * If you want to override default value - use {@link #getLong(String, long)}.
     *
     * @param key to be stored under.
     * @return {@link RxPreference} to perform operations.
     */
    public RxPreference<Long> getLong(final String key) {
        return rx(preferences.getLong(key));
    }

    /**
     * Get Long preference. Can't be null.
     *
     * @param key          to be stored under.
     * @param defaultValue return that value if not present.
     * @return {@link RxPreference} to perform operations.
     */
    public RxPreference<Long> getLong(final String key, final long defaultValue) {
        return rx(preferences.getLong(key, defaultValue));
    }

    /**
     * Get Any Generic preference. Nullability depends on used {@link PreferenceStrategy}.
     * Default Value is null.
     * If you want to override default value - use {@link #getObject(String, PreferenceStrategy, Object)}.
     * <p>throws {@link NullPointerException} if strategy doesn't allow nulls.</p>
     *
     * @param <T>      RxPreference generic.
     * @param key      to be stored under.
     * @param strategy {@link PreferenceStrategy} that describes how preference should be
     *                 serialized/deserialized/removed or even notified on changes or its nullability.
     * @return {@link RxPreference} to perform operations.
     */
    public <T> RxPreference<T> getObject(final String key, final PreferenceStrategy<T> strategy) {
        return rx(preferences.getObject(key, strategy));
    }

    /**
     * Get Any Generic preference. Nullability depends on used {@link PreferenceStrategy}.
     * <p>throws {@link NullPointerException} if strategy doesn't allow nulls.</p>
     *
     * @param <T>          RxPreference generic.
     * @param key          to be stored under.
     * @param strategy     {@link PreferenceStrategy} that describes how preference should be
     *                     serialized/deserialized/removed or even notified on changes or its nullability.
     * @param defaultValue return that value if not present.
     * @return {@link RxPreference} to perform operations.
     */
    public <T> RxPreference<T> getObject(final String key, final PreferenceStrategy<T> strategy, @Nullable final T defaultValue) {
        return rx(preferences.getObject(key, strategy, defaultValue));
    }

    /**
     * Get Any RxPreferences watcher.
     * <p>Be careful, this {@link RxPreference} implementation doesn't support {@link RxPreference#save(Object)} operation</p>
     * <p>
     * But you still able to use
     * <ul>
     * <li>{@link RxPreference#remove()}</li>
     * <li>{@link RxPreference#read()}</li>
     * <li>{@link RxPreference#provider()}</li>
     * </ul>
     *
     * @return {@link RxPreference} to perform operations.
     */
    public RxPreference<Map<String, ?>> getAll() {
        return rx(preferences.getAll());
    }

}
