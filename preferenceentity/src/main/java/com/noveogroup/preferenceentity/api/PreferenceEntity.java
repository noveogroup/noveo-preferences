package com.noveogroup.preferenceentity.api;

import com.noveogroup.preferenceentity.NoveoRxPreferenceEntity;

import java.io.IOException;

import java8.util.Optional;

@SuppressWarnings("unused")
public interface PreferenceEntity<T> {

    void save(T value) throws IOException;

    void remove() throws IOException;

    Optional<T> read();

    NoveoRxPreferenceEntity<T> rx();

    ValueProvider<T> provider();

}
