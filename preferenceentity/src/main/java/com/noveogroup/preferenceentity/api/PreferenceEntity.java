package com.noveogroup.preferenceentity.api;

import java.io.IOException;

import java8.util.Optional;

@SuppressWarnings("unused")
public interface PreferenceEntity<T> {

    void save(T value) throws IOException;

    void remove() throws IOException;

    Optional<T> read();

    RxPreferenceEntity<T> rx();

    ValueProvider<T> provider();

}
