package com.noveogroup.preferenceentity;

import com.noveogroup.preferenceentity.api.RxValueProvider;
import com.noveogroup.preferenceentity.api.ValueProvider;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java8.util.Optional;

public class NoveoRxValueProvider<T> implements RxValueProvider<T> {

    private final Flowable<Optional<T>> flowable;

    NoveoRxValueProvider(final ValueProvider<T> valueProvider) {
        flowable = Flowable.create(emitter -> {
            final Consumer<Optional<T>> consumer = value -> {
                emitter.onNext(value);
                emitter.requested();
            };

            valueProvider.addListener(consumer);
            emitter.setCancellable(() -> valueProvider.removeListener(consumer));
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Disposable observe(final Consumer<Optional<T>> consumer) {
        return flowable.subscribe(consumer);
    }

    @Override
    public Disposable observe(final Scheduler scheduler, final Consumer<Optional<T>> consumer) {
        return flowable.observeOn(scheduler).subscribe(consumer);
    }

    @Override
    public Flowable<Optional<T>> asFlowable() {
        return flowable;
    }
}
