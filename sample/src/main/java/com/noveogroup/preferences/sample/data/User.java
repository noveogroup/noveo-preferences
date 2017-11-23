package com.noveogroup.preferences.sample.data;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.noveogroup.preferences.PreferenceStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
public class User {
    public static final PreferenceStrategy<User> USER_PREFERENCE_STRATEGY = new UserStrategy();
    public static final long NOT_AN_AGE = -1L;
    private String name;
    private long age;

    @SuppressWarnings("WeakerAccess")
    public static class UserStrategy extends PreferenceStrategy<User> {

        private static final String POSTFIX_AGE = ".int";
        private static final String POSTFIX_NAME = ".string";

        private UserStrategy() {
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
                            preferences.getLong(fieldIntKey, defaultNotNull ? value.getAge() : 0));
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
                    editor.putLong(fieldIntKey, value.getAge());
                    editor.putString(fieldStringKey, value.getName());
                } else {
                    editor.remove(fieldIntKey);
                    editor.remove(fieldStringKey);
                }
            };
        }
    }

}
