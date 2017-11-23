package com.noveogroup.preferences.api;

import io.reactivex.functions.Consumer;
import java8.util.Optional;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings("unused")
public interface PreferenceProvider<T> {

    void addListener(Consumer<Optional<T>> consumer);

    void removeListener(Consumer<Optional<T>> consumer);

}
