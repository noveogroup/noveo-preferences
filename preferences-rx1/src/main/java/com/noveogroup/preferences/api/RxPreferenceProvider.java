package com.noveogroup.preferences.api;

import com.noveogroup.preferences.guava.Optional;
import com.noveogroup.preferences.lambda.Consumer;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */

/**
 * That class helps you observe preference changes.
 *
 * @param <T> preference type.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface RxPreferenceProvider<T> {

    /**
     * Subscribe onto preference changes.
     * @param consumer rxJava2 OnNext listener.
     * @return {@link Disposable} to stop watching. That's enough to call {@link Disposable#dispose()}.
     */
    Subscription observe(Consumer<Optional<T>> consumer);

    /**
     * Similar to {@link #observe(Consumer)}, but with Scheduler.
     * @param scheduler thread to deliver event on.
     * @param consumer rxJava2 OnNext listener.
     * @return {@link Disposable} to stop watching. That's enough to call {@link Disposable#dispose()}.
     */
    Subscription observe(Scheduler scheduler, Consumer<Optional<T>> consumer);

    /**
     * If you want preference changes as part of your rx chain.
     * @return {@link Flowable} that emit event similar to onNext in {@link #observe(Consumer)} and {@link #observe(Scheduler, Consumer)} methods.
     */
    Observable<Optional<T>> asObservable();

}
