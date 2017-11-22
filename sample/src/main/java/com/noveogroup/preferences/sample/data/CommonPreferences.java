package com.noveogroup.preferences.sample.data;

import android.content.Context;
import android.text.TextUtils;

import com.noveogroup.preferenceentity.NoveoPreferences;
import com.noveogroup.preferenceentity.PreferenceStrategy;
import com.noveogroup.preferenceentity.api.PreferenceEntity;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

// Usually you want to make such class @Singleton
@Slf4j
public class CommonPreferences {

    private static final PreferenceStrategy<User> USER_PREFERENCES_STRATEGY = PreferenceStrategy.<User>builder()
            .setAction((editor, key, user) -> {
                final boolean presented = user != null;
                editor.putBoolean(key, presented);
                if (presented) {
                    editor.putString(key + ".name", user.getName());
                    editor.putLong(key + ".age", user.getAge());
                }
            })
            .getAction((sharedPreferences, key, defaultValue) -> {
                if (sharedPreferences.contains(key)) {
                    final String name = sharedPreferences.getString(key + ".name", "");
                    final Long age = sharedPreferences.getLong(key + ".age", 0L);
                    return new User(name, age);
                }
                return defaultValue;
            })
            .keyFilter((key, entityKey) -> TextUtils.equals(entityKey + ".name", key) || TextUtils.equals(entityKey + ".age", key))
            .canBeNull(true)
            .build();
    // Keep strong references to entity. Otherwise observers/listeners may be lost (due to SP Android implementation)
    public final PreferenceEntity<Boolean> enabled;
    public final PreferenceEntity<User> user;
    public final PreferenceEntity<Map<String, ?>> all;

    // Depends on Context.
    public CommonPreferences(final Context context) {
        final NoveoPreferences preferences = new NoveoPreferences(context);
        all = preferences.getAll();

        enabled = preferences.getBoolean("boolean", false);
        user = preferences.getObject("user", USER_PREFERENCES_STRATEGY);

        all.rx().provider().observe(snapshot -> snapshot.ifPresentOrElse(
                map -> {
                    log.debug("==== PREFERENCES SNAPSHOT ====");
                    for (final String key : map.keySet()) {
                        log.debug("{} -> {}", key, map.get(key));
                    }
                    log.debug("==============================");
                },
                () -> log.debug("==== PREFERENCES SNAPSHOT (EMPTY) ====")));
    }

}
