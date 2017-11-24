package com.noveogroup.preferences.api;

import java.io.IOException;

import java8.util.Optional;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */

/**
 * Preference Entity wraps key connected to the stored value.
 * <p>{@link Preference} provides you synchronous IO operations.</p>
 * <p>If you need async styled code - use RxJava2 extension {@link RxPreference} via {@link #rx()} method</p>
 * <p>Instantiate it using {@link com.noveogroup.preferences.NoveoPreferences} class.</p>
 * <p>In addition wrapper provides RxJava2 extension and change listeners.</p>
 *
 * @param <T> type of preference.
 */
public interface Preference<T> {

    /**
     * <p>{@link android.content.SharedPreferences.Editor#commit()} is under the hood.</p>
     * <p>If you won't wait for IO operations end - use {@link RxPreference} via {@link #rx()} method</p>
     *
     * @param value your new preference value.
     * @throws IOException if {@link android.content.SharedPreferences.Editor#commit()} returns false.
     */
    void save(T value) throws IOException;

    /**
     * <p>{@link android.content.SharedPreferences.Editor#commit()} is under the hood.</p>
     * <p>If you won't wait for IO operations end - use {@link RxPreference} via {@link #rx()} method</p>
     *
     * @throws IOException if {@link android.content.SharedPreferences.Editor#commit()} returns false.
     */
    void remove() throws IOException;

    /**
     * @return {@link Optional} since preference may be null and we want write code null-safe.
     * Whereas T is generic type of Preference
     */
    Optional<T> read();

    /**
     * Get RxJava2 extension.
     *
     * @return {@link RxPreference} wrapper with RxJava2 asynchronous methods.
     */
    RxPreference<T> rx();

    /**
     * <p>Get Callback-styled Provider.</p>
     * <p>If you need RxJava observer - use {@link RxPreference#provider()} on {@link #rx()} wrapper.</p>
     *
     * @return {@link PreferenceProvider} that accepts and removes listeners for current preference.
     */
    PreferenceProvider<T> provider();

}
