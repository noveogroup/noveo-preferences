package com.noveogroup.preferences.rx.api;

import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.guava.Optional;
import com.noveogroup.preferences.rx.NoveoRxPreferences;

import rx.Completable;
import rx.Single;

/**
 * RxJava2 Extension for read/remove/save operations above {@link Preference}.
 * <p>Provides you asynchronous methods to be used in your Rx chains.</p>
 *
 * @param <T> generic type of preference.
 *
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings("unused")
public interface RxPreference<T> {

    /**
     * Wrap synchronous {@link Preference} to {@link RxPreference} variant.
     *
     * @param preference source.
     * @param <T>        generic type.
     * @return {@link RxPreference} wrapper.
     */
    static <T> RxPreference<T> rx(final Preference<T> preference) {
        return NoveoRxPreferences.rx(preference);
    }

    /**
     * Read async.
     *
     * @return {@link Single} with {@link Optional} in case preference is null.
     */
    Single<Optional<T>> read();

    /**
     * Remove async.
     *
     * @return {@link Completable} since remove operation may be finish with success or error.
     */
    Completable remove();

    /**
     * Save async.
     *
     * @param value new value of preference.
     * @return {@link Completable} since save operation may be finish with success or error.
     */
    Completable save(T value);

    /**
     * Get Rx-styled preference in opposite to {@link Preference#provider()} callback-styled one.
     *
     * @return {@link RxPreferenceProvider}.
     */
    RxPreferenceProvider<T> provider();

    Preference<T> toBlocking();
}
