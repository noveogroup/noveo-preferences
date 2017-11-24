/**
 * Copyright (C) 2011 The Guava Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Modified:
 * 1. package.
 * 2. added 3 apply*() methods.
 * 3. Usage of Utility methods and interfaces.
 */
package com.noveogroup.preferences.guava;

import android.support.annotation.Nullable;

import com.noveogroup.preferences.lambda.Function;

import java.util.Collections;
import java.util.Set;

import static com.noveogroup.preferences.guava.Utils.checkNotNull;

/**
 * Implementation of an {@link Optional} containing a reference.
 *
 * <p><b>This class contains code derived from <a href="https://github.com/google/guava">Google
 * Guava</a></b>
 *
 * <p>Modified:
 * <ul>
 *     <li>package</li>
 *     <li>Usage of Utility methods and condition interfaces</li>
 * </ul>
 * </p>
 */
@SuppressWarnings("PMD")
final class Present<T> extends Optional<T> {
    private static final long serialVersionUID = 0;

    private final T reference;

    Present(T reference) {
        this.reference = reference;
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public T get() {
        return reference;
    }

    @Override
    public T or(T defaultValue) {
        checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
        return reference;
    }

    @Override
    public Optional<T> or(Optional<? extends T> secondChoice) {
        checkNotNull(secondChoice);
        return this;
    }

    @Override
    public T orNull() {
        return reference;
    }

    @Override
    public Set<T> asSet() {
        return Collections.singleton(reference);
    }

    @Override
    public <V> Optional<V> transform(Function<? super T, V> function) {
        return new Present<>(checkNotNull(function.apply(reference),
                "the Function passed to Optional.transform() must not return null."));
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof Present) {
            Present<?> other = (Present<?>) object;
            return reference.equals(other.reference);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Absent.HASH_CODE + reference.hashCode();
    }

    @Override
    public String toString() {
        return "Optional.of(" + reference + ")";
    }
}
