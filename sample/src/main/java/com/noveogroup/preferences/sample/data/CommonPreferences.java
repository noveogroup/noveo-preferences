package com.noveogroup.preferences.sample.data;

import android.content.Context;

import com.noveogroup.preferences.NoveoPreferences;
import com.noveogroup.preferences.api.Preference;

import java.util.Map;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
// Usually you want to make such class @Singleton
public class CommonPreferences {

    // Keep strong references to entity. Otherwise observers/listeners may be lost (due to SP Android implementation)
    public final Preference<Boolean> enabled;
    public final Preference<User> user;
    public final Preference<Map<String, ?>> all;

    // Depends on Context.
    public CommonPreferences(final Context context) {
        final NoveoPreferences preferences = new NoveoPreferences(context);
        all = preferences.getAll();

        enabled = preferences.getBoolean("boolean", false);
        user = preferences.getObject("user", User.USER_PREFERENCE_STRATEGY);
    }

}
