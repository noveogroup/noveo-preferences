package com.noveogroup.preferences.mock;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.noveogroup.preferences.PreferenceStrategy;

public class UserStrategy extends PreferenceStrategy<User> {

    public static final String POSTFIX_AGE = ".int";
    public static final String POSTFIX_NAME = ".string";

    public UserStrategy() {
        super(
                createSetAction(),
                createGetAction(),
                createRemoveAction(),
                createKeyComparator(),
                true
        );
    }

    @NonNull
    private static KeyFilter createKeyComparator() {
        return (String storedKey, String entityKey) -> {
            final String fieldIntKey = entityKey + POSTFIX_AGE;
            final String fieldStringKey = entityKey + POSTFIX_NAME;

            return entityKey.equals(storedKey)
                    || fieldIntKey.equals(storedKey)
                    || fieldStringKey.equals(storedKey);
        };
    }

    @NonNull
    private static RemoveAction createRemoveAction() {
        return (SharedPreferences.Editor editor, String key) -> {
            final String fieldIntKey = key + POSTFIX_AGE;
            final String fieldStringKey = key + POSTFIX_NAME;

            editor.remove(key)
                    .remove(fieldIntKey)
                    .remove(fieldStringKey);
        };
    }

    @NonNull
    private static GetAction<User> createGetAction() {
        return (preferences, key, value) -> {
            final String fieldIntKey = key + POSTFIX_AGE;
            final String fieldStringKey = key + POSTFIX_NAME;

            final boolean presented = preferences.getBoolean(key, false);
            if (presented) {
                final boolean defaultNotNull = value != null;
                return new User(
                        preferences.getString(fieldStringKey, defaultNotNull ? value.getName() : null),
                        preferences.getInt(fieldIntKey, defaultNotNull ? value.getAge() : 0));
            }
            return value;
        };
    }

    @NonNull
    private static SetAction<User> createSetAction() {
        return (editor, key, value) -> {
            final boolean presented = value != null;
            editor.putBoolean(key, presented);

            final String fieldIntKey = key + POSTFIX_AGE;
            final String fieldStringKey = key + POSTFIX_NAME;

            if (presented) {
                editor.putInt(fieldIntKey, value.getAge());
                editor.putString(fieldStringKey, value.getName());
            } else {
                editor.remove(fieldIntKey);
                editor.remove(fieldStringKey);
            }
        };
    }
}
