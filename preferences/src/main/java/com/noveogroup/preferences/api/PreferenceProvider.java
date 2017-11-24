package com.noveogroup.preferences.api;

import io.reactivex.functions.Consumer;
import java8.util.Optional;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */

/**
 * That class helps you to watch preference changes.
 *
 * @param <T> is a generic of preference type.
 */
@SuppressWarnings("unused")
public interface PreferenceProvider<T> {

    /**
     * Start watching with your listener.
     *
     * @param consumer assumes Optional to achieve null-safety (since preference may be null after remove).
     * @return exact instance of consumer that was provided as argument. To store it easily.
     */
    Consumer<Optional<T>> addListener(Consumer<Optional<T>> consumer);

    /**
     * Stop watching for the preference.
     * @param consumer listener that were connected via {@link #addListener(Consumer)}.
     */
    void removeListener(Consumer<Optional<T>> consumer);

}
