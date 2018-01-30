package com.noveogroup.preferences.sample.data;

import android.content.Context;

import com.noveogroup.preferences.rx.NoveoRxPreferences;
import com.noveogroup.preferences.rx.api.RxPreference;

import java.util.Map;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
// Usually you want to make such class @Singleton
public class CommonPreferences {

    /* Keep strong references to entity.
     * Otherwise observers/listeners may be lost (due to SharePreferences implementation) */
    public final RxPreference<Boolean> enabled;
    public final RxPreference<User> user;
    public final RxPreference<Map<String, ?>> all;

    // Depends on Context.
    public CommonPreferences(final Context context) {
        final NoveoRxPreferences preferences = new NoveoRxPreferences(context);
        all = preferences.getAll();

        enabled = preferences.getBoolean("boolean", false);
        user = preferences.getObject("user", User.USER_PREFERENCE_STRATEGY);
    }

}
