package com.noveogroup.preferences.rx;

import com.noveogroup.preferences.api.PreferenceProvider;
import com.noveogroup.preferences.guava.Optional;
import com.noveogroup.preferences.lambda.Consumer;
import com.noveogroup.preferences.rx.api.RxPreferenceProvider;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
class NoveoRxPreferenceProvider<T> implements RxPreferenceProvider<T> {

    private final Flowable<Optional<T>> flowable;

    NoveoRxPreferenceProvider(final PreferenceProvider<T> preferenceProvider) {
        flowable = Flowable.create(emitter -> {
            final Consumer<Optional<T>> consumer = preferenceProvider.addListener(value -> {
                emitter.onNext(value);
                emitter.requested();
            });
            emitter.setCancellable(() -> preferenceProvider.removeListener(consumer));
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Disposable observe(final Consumer<Optional<T>> consumer) {
        return flowable.subscribe(consumer::accept);
    }

    @Override
    public Disposable observe(final Scheduler scheduler, final Consumer<Optional<T>> consumer) {
        return flowable.observeOn(scheduler).subscribe(consumer::accept);
    }

    @Override
    public Flowable<Optional<T>> asFlowable() {
        return flowable;
    }
}
