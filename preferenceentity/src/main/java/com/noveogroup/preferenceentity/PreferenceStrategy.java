package com.noveogroup.preferenceentity;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
public class PreferenceStrategy<T> {

    public static final PreferenceStrategy<Boolean> BOOLEAN = PreferenceStrategy.<Boolean>builder()
            .setAction(SharedPreferences.Editor::putBoolean)
            .getAction(SharedPreferences::getBoolean)
            .build();

    public static final PreferenceStrategy<String> STRING = PreferenceStrategy.<String>builder()
            .setAction(SharedPreferences.Editor::putString)
            .getAction(SharedPreferences::getString)
            .canBeNull(true)
            .build();

    public static final PreferenceStrategy<Float> FLOAT = PreferenceStrategy.<Float>builder()
            .setAction(SharedPreferences.Editor::putFloat)
            .getAction(SharedPreferences::getFloat)
            .build();

    public static final PreferenceStrategy<Integer> INTEGER = PreferenceStrategy.<Integer>builder()
            .setAction(SharedPreferences.Editor::putInt)
            .getAction(SharedPreferences::getInt)
            .build();

    public static final PreferenceStrategy<Long> LONG = PreferenceStrategy.<Long>builder()
            .setAction(SharedPreferences.Editor::putLong)
            .getAction(SharedPreferences::getLong)
            .build();

    public final KeyFilter keyFilter;
    public final boolean canBeNull;
    private final SetAction<T> setAction;
    private final GetAction<T> getAction;
    private final RemoveAction removeAction;

    public PreferenceStrategy(final SetAction<T> setAction, final GetAction<T> getAction, final RemoveAction removeAction, final KeyFilter keyFilter, final boolean canBeNull) {
        this.keyFilter = keyFilter;
        this.setAction = setAction;
        this.getAction = getAction;
        this.removeAction = removeAction;
        this.canBeNull = canBeNull;
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

    public void remove(final SharedPreferences.Editor editor, final String key) {
        removeAction.call(editor, key);
    }

    public interface SetAction<T> {
        void call(SharedPreferences.Editor editor, String key, @Nullable T value);
    }

    public interface GetAction<T> {
        T call(SharedPreferences preferences, String key, @Nullable T value);
    }

    public interface RemoveAction {
        RemoveAction REMOVE_BY_KEY = SharedPreferences.Editor::remove;

        void call(SharedPreferences.Editor editor, String key);
    }

    public interface KeyFilter {
        @SuppressWarnings("StringEquality")
        KeyFilter EQUALS = (a, b) -> a == b || a != null && b != null && a.length() == b.length() && a.equals(b);

        boolean compare(String storedKey, String entityKey);
    }

    @SuppressWarnings({"SameParameterValue", "PMD.AvoidFieldNameMatchingMethodName"})
    public static class Builder<T> {
        private SetAction<T> setAction;
        private GetAction<T> getAction;
        private RemoveAction removeAction;
        private KeyFilter keyFilter;
        private boolean canBeNull;

        Builder() {
            this.canBeNull = false;
            this.keyFilter = KeyFilter.EQUALS;
            this.removeAction = RemoveAction.REMOVE_BY_KEY;
            this.setAction = (a, b, c) -> {
                throw new IllegalArgumentException("set action not defined");
            };
            this.getAction = (a, b, c) -> {
                throw new IllegalArgumentException("get action not defined");
            };
        }

        public Builder<T> removeAction(final RemoveAction removeAction) {
            this.removeAction = removeAction;
            return this;
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

        public Builder<T> canBeNull(final boolean canBeNull) {
            this.canBeNull = canBeNull;
            return this;
        }

        public PreferenceStrategy<T> build() {
            return new PreferenceStrategy<>(setAction, getAction, removeAction, keyFilter, canBeNull);
        }
    }
}
