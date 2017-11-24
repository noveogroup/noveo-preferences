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

    static class UserKeys {

        private static final String POSTFIX_AGE = ".int";
        private static final String POSTFIX_NAME = ".string";

        final String key;
        final String nameKey;
        final String ageKey;

        UserKeys(final String key) {
            this.key = key;
            this.ageKey = key + POSTFIX_AGE;
            this.nameKey = key + POSTFIX_NAME;
        }

        boolean keyEquals(final String storedKey) {
            return key.equals(storedKey)
                    || ageKey.equals(storedKey)
                    || nameKey.equals(storedKey);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static final class UserStrategy extends PreferenceStrategy<User> {

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
            return (String storedKey, String entityKey) ->
                    new UserKeys(entityKey).keyEquals(storedKey);
        }

        @NonNull
        private static RemoveAction createRemoveAction() {
            return (SharedPreferences.Editor editor, String key) -> {
                final UserKeys userKeys = new UserKeys(key);
                editor.remove(key)
                        .remove(userKeys.ageKey)
                        .remove(userKeys.nameKey);
            };
        }

        @NonNull
        private static GetAction<User> createGetAction() {
            return (preferences, key, value) -> {
                final UserKeys userKeys = new UserKeys(key);
                final boolean presented = preferences.getBoolean(key, false);
                if (presented) {
                    final boolean defaultNotNull = value != null;
                    return new User(
                            preferences.getString(userKeys.nameKey, defaultNotNull ? value.getName() : null),
                            preferences.getLong(userKeys.ageKey, defaultNotNull ? value.getAge() : 0));
                }
                return value;
            };
        }

        @NonNull
        private static SetAction<User> createSetAction() {
            return (editor, key, value) -> {
                final boolean presented = value != null;
                editor.putBoolean(key, presented);
                final UserKeys userKeys = new UserKeys(key);

                if (presented) {
                    editor.putLong(userKeys.ageKey, value.getAge());
                    editor.putString(userKeys.nameKey, value.getName());
                } else {
                    editor.remove(userKeys.ageKey);
                    editor.remove(userKeys.nameKey);
                }
            };
        }
    }

}
