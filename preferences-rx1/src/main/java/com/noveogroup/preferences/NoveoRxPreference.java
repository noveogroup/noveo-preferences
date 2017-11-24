package com.noveogroup.preferences;

import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.api.RxPreference;
import com.noveogroup.preferences.api.RxPreferenceProvider;
import com.noveogroup.preferences.guava.Optional;

import rx.Completable;
import rx.Single;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
class NoveoRxPreference<T> implements RxPreference<T> {

    private final Preference<T> preference;
    private RxPreferenceProvider<T> rxProvider;

    NoveoRxPreference(final Preference<T> preference) {
        this.preference = preference;
    }

    @Override
    public Single<Optional<T>> read() {
        return Single.fromCallable(preference::read);
    }

    @Override
    public Completable remove() {
        return Completable.fromAction(preference::remove);
    }

    @Override
    public Completable save(final T value) {
        return Completable.fromAction(() -> preference.save(value));
    }

    @Override
    public RxPreferenceProvider<T> provider() {
        if (rxProvider == null) {
            rxProvider = new NoveoRxPreferenceProvider<>(preference.provider());
        }
        return rxProvider;
    }

    @Override
    public Preference<T> toBlocking() {
        return preference;
    }
}
