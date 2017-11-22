package com.noveogroup.preferenceentity;

import android.content.SharedPreferences;

import io.reactivex.functions.Consumer;

@SuppressWarnings("unchecked")
public class Utils {
    static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }

    static boolean editPreferences(final SharedPreferences preferences, final Consumer<SharedPreferences.Editor> action) {
        try {
            final SharedPreferences.Editor editor = preferences.edit();
            action.accept(editor);
            return editor.commit();
        } catch (final Exception original) {
            Utils.sneakyThrow(original);
            return false;
        }
    }
}
