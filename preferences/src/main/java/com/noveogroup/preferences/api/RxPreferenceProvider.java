package com.noveogroup.preferences.api;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java8.util.Optional;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings("unused")
public interface RxPreferenceProvider<T> {

    Disposable observe(Consumer<Optional<T>> consumer);

    Disposable observe(Scheduler scheduler, Consumer<Optional<T>> consumer);

    Flowable<Optional<T>> asFlowable();

}
