package com.noveogroup.preferenceentity.api;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java8.util.Optional;

@SuppressWarnings("unused")
public interface RxValueProvider<T> {

    Disposable observe(Consumer<Optional<T>> consumer);

    Disposable observe(Scheduler scheduler, Consumer<Optional<T>> consumer);

    Flowable<Optional<T>> asFlowable();

}
