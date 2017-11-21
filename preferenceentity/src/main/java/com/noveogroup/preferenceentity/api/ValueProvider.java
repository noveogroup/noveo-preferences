package com.noveogroup.preferenceentity.api;

import io.reactivex.functions.Consumer;
import java8.util.Optional;

@SuppressWarnings("unused")
public interface ValueProvider<T> {

    void addListener(Consumer<Optional<T>> consumer);

    void removeListener(Consumer<Optional<T>> consumer);

}
