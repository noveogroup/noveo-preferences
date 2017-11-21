package com.noveogroup.preferenceentity;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import lombok.AllArgsConstructor;

@SuppressWarnings({"unused", "WeakerAccess"})
@AllArgsConstructor
public class PreferenceStrategy<T> {

    public static final PreferenceStrategy<Boolean> BOOLEAN = new PreferenceStrategy<>(
            SharedPreferences.Editor::putBoolean,
            SharedPreferences::getBoolean
    );
    public static final PreferenceStrategy<String> STRING = new PreferenceStrategy<>(
            SharedPreferences.Editor::putString,
            SharedPreferences::getString
    );
    public static final PreferenceStrategy<Float> FLOAT = new PreferenceStrategy<>(
            SharedPreferences.Editor::putFloat,
            SharedPreferences::getFloat
    );
    public static final PreferenceStrategy<Integer> INTEGER = new PreferenceStrategy<>(
            SharedPreferences.Editor::putInt,
            SharedPreferences::getInt
    );
    public static final PreferenceStrategy<Long> LONG = new PreferenceStrategy<>(
            SharedPreferences.Editor::putLong,
            SharedPreferences::getLong
    );
    public final KeyFilter keyFilter;
    private final SetAction<T> setAction;
    private final GetAction<T> getAction;

    public PreferenceStrategy(final SetAction<T> setAction, final GetAction<T> getAction) {
        this.setAction = setAction;
        this.getAction = getAction;
        this.keyFilter = KeyFilter.EQUALS;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public void set(final SharedPreferences.Editor editor, final String key, @Nullable final T value) {
        setAction.call(editor, key, value);
    }

    public T get(final SharedPreferences preferences, final String key, @Nullable final T value) {
        return getAction.call(preferences, key, value);
    }

    public interface SetAction<T> {
        void call(SharedPreferences.Editor editor, String key, @Nullable T value);
    }

    public interface GetAction<T> {
        T call(SharedPreferences preferences, String key, @Nullable T value);
    }

    public interface KeyFilter {
        KeyFilter EQUALS = TextUtils::equals;

        boolean compare(String storedKey, String entityKey);
    }

    public static class Builder<T> {
        private SetAction<T> setAction;
        private GetAction<T> getAction;
        private KeyFilter keyFilter;

        public Builder() {
            this.keyFilter = KeyFilter.EQUALS;
            this.setAction = (a, b, c) -> {
                throw new IllegalArgumentException("set action not defined");
            };
            this.getAction = (a, b, c) -> {
                throw new IllegalArgumentException("get action not defined");
            };
        }

        public Builder<T> setAction(final SetAction<T> setAction) {
            this.setAction = setAction;
            return this;
        }

        public Builder<T> getAction(final GetAction<T> getAction) {
            this.getAction = getAction;
            return this;
        }

        public Builder<T> keyFilter(final KeyFilter keyFilter) {
            this.keyFilter = keyFilter;
            return this;
        }

        public PreferenceStrategy<T> build() {
            return new PreferenceStrategy<>(setAction, getAction, keyFilter);
        }
    }
}
