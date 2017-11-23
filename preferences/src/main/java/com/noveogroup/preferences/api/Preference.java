package com.noveogroup.preferences.api;

import java.io.IOException;

import java8.util.Optional;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings("unused")
public interface Preference<T> {

    void save(T value) throws IOException;

    void remove() throws IOException;

    Optional<T> read();

    RxPreference<T> rx();

    PreferenceProvider<T> provider();

}
