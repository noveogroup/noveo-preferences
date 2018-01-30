package com.noveogroup.preferences.api;

import com.noveogroup.preferences.guava.Optional;

/**
 * Preference Entity wraps key connected to the stored value.
 * <p>{@link Preference} provides you synchronous IO operations.</p>
 * <p>Instantiate it using {@link com.noveogroup.preferences.NoveoPreferences} class.</p>
 * <p>In addition wrapper provides RxJava2 extension and change listeners.</p>
 *
 * @param <T> type of preference.
 *
 * Created by avaytsekhovskiy on 23/11/2017.
 */
public interface Preference<T> {

    /**
     * <p>{@link android.content.SharedPreferences.Editor#commit()} is under the hood.</p>
     *
     * @param value your new preference value.
     * sneaky throws IOException if {@link android.content.SharedPreferences.Editor#commit()} returns false.
     */
    void save(T value);

    /**
     * <p>{@link android.content.SharedPreferences.Editor#commit()} is under the hood.</p>
     *
     * sneaky throws IOException if {@link android.content.SharedPreferences.Editor#commit()} returns false.
     */
    void remove();

    /**
     * @return {@link Optional} since preference may be null and we want write code null-safe.
     * Whereas T is generic type of Preference
     */
    Optional<T> read();

    /**
     * <p>Get Callback-styled Provider.</p>
     *
     * @return {@link PreferenceProvider} that accepts and removes listeners for current preference.
     */
    PreferenceProvider<T> provider();

}
