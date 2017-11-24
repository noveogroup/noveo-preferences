package com.noveogroup.preferences.api;

import io.reactivex.Completable;
import io.reactivex.Single;
import java8.util.Optional;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */

/**
 * RxJava2 Extension for read/remove/save operations above {@link Preference}.
 * <p>Provides you asynchronous methods to be used in your Rx chains.</p>
 *
 * @param <T> generic type of preference.
 */
@SuppressWarnings("unused")
public interface RxPreference<T> {

    /**
     * Read async.
     * @return {@link Single} with {@link Optional} in case preference is null.
     */
    Single<Optional<T>> read();

    /**
     * Remove async.
     * @return {@link Completable} since remove operation may be finish with success or error.
     */
    Completable remove();

    /**
     * Save async.
     * @param value new value of preference.
     * @return {@link Completable} since save operation may be finish with success or error.
     */
    Completable save(T value);

    /**
     * Get Rx-styled preference in opposite to {@link Preference#provider()} callback-styled one.
     * @return {@link RxPreferenceProvider}.
     */
    RxPreferenceProvider<T> provider();
}
