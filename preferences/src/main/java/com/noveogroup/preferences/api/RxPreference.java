package com.noveogroup.preferences.api;

import io.reactivex.Completable;
import io.reactivex.Single;
import java8.util.Optional;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings("unused")
public interface RxPreference<T> {

    Single<Optional<T>> read();

    Completable remove();

    Completable save(T value);

    RxPreferenceProvider<T> provider();
}
