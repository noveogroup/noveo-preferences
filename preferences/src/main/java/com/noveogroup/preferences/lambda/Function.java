package com.noveogroup.preferences.lambda;

public interface Function<T, R> {
    R apply(T param);
}
