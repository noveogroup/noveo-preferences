package com.noveogroup.preferenceentity.api;

import io.reactivex.Completable;
import io.reactivex.Single;
import java8.util.Optional;

@SuppressWarnings("unused")
public interface RxPreferenceEntity<T> {

    Single<Optional<T>> read();

    Completable remove();

    Completable save(T value);

    RxValueProvider<T> provider();
}
