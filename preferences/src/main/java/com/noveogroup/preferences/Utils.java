package com.noveogroup.preferences;

import android.content.SharedPreferences;

import io.reactivex.functions.Consumer;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings({"unchecked", "WeakerAccess"})
final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("You can't instantiate Utility classes");
    }

    static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
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
