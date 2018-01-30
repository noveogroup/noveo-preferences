package com.noveogroup.preferences.rx.api;

import com.noveogroup.preferences.guava.Optional;
import com.noveogroup.preferences.lambda.Consumer;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;

/**
 * That class helps you observe preference changes.
 *
 * @param <T> preference type.
 *
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface RxPreferenceProvider<T> {

    /**
     * Subscribe onto preference changes.
     * @param consumer rxJava2 OnNext listener.
     * @return {@link Subscription} to stop watching. That's enough to call {@link Subscription#unsubscribe()}.
     */
    Subscription observe(Consumer<Optional<T>> consumer);

    /**
     * Similar to {@link #observe(Consumer)}, but with Scheduler.
     * @param scheduler thread to deliver event on.
     * @param consumer rxJava2 OnNext listener.
     * @return {@link Subscription} to stop watching. That's enough to call {@link Subscription#unsubscribe()}.
     */
    Subscription observe(Scheduler scheduler, Consumer<Optional<T>> consumer);

    /**
     * If you want preference changes as part of your rx chain.
     * @return {@link Observable} that emit event similar to onNext.
     */
    Observable<Optional<T>> asObservable();

}
