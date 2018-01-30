package com.noveogroup.preferences.rx;

import com.noveogroup.preferences.api.PreferenceProvider;
import com.noveogroup.preferences.guava.Optional;
import com.noveogroup.preferences.lambda.Consumer;
import com.noveogroup.preferences.rx.api.RxPreferenceProvider;

import rx.Emitter;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
class NoveoRxPreferenceProvider<T> implements RxPreferenceProvider<T> {

    private final Observable<Optional<T>> safeBackpressureObservable;

    NoveoRxPreferenceProvider(final PreferenceProvider<T> preferenceProvider) {
        safeBackpressureObservable = Observable.create(emitter -> {
            final Consumer<Optional<T>> consumer = preferenceProvider.addListener(value -> {
                emitter.onNext(value);
                emitter.requested();
            });
            emitter.setCancellation(() -> preferenceProvider.removeListener(consumer));
        }, Emitter.BackpressureMode.LATEST);
    }

    @Override
    public Subscription observe(final Consumer<Optional<T>> consumer) {
        return safeBackpressureObservable
                .onBackpressureLatest()
                .subscribe(consumer::accept);
    }

    @Override
    public Subscription observe(final Scheduler scheduler, final Consumer<Optional<T>> consumer) {
        return safeBackpressureObservable
                .onBackpressureLatest()
                .observeOn(scheduler)
                .subscribe(consumer::accept);
    }

    @Override
    public Observable<Optional<T>> asObservable() {
        return safeBackpressureObservable
                .onBackpressureLatest();
    }
}
