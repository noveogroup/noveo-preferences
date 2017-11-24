package com.noveogroup.preferences.guava;

import android.support.annotation.Nullable;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings({"unchecked", "WeakerAccess", "PMD", "unused"})
final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("You can't instantiate Utility classes");
    }

    public static boolean equals(final Object a, final Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static <T> T checkNotNull(final T reference, final @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }
}
