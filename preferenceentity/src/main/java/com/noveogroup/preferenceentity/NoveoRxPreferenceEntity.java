package com.noveogroup.preferenceentity;

import com.noveogroup.preferenceentity.api.PreferenceEntity;
import com.noveogroup.preferenceentity.api.RxPreferenceEntity;
import com.noveogroup.preferenceentity.api.RxValueProvider;

import io.reactivex.Completable;
import io.reactivex.Single;
import java8.util.Optional;

public class NoveoRxPreferenceEntity<T> implements RxPreferenceEntity<T> {

    private final PreferenceEntity<T> preferenceEntity;
    private RxValueProvider<T> rxProvider;

    NoveoRxPreferenceEntity(final PreferenceEntity<T> preferenceEntity) {
        this.preferenceEntity = preferenceEntity;
    }

    @Override
    public Single<Optional<T>> read() {
        return Single.fromCallable(preferenceEntity::read);
    }

    @Override
    public Completable remove() {
        return Completable.fromAction(preferenceEntity::remove);
    }

    @Override
    public Completable save(final T value) {
        return Completable.fromAction(() -> preferenceEntity.save(value));
    }

    @Override
    public RxValueProvider<T> provider() {
        if (rxProvider == null) {
            rxProvider = new NoveoRxValueProvider<>(preferenceEntity.provider());
        }
        return rxProvider;
    }
}
