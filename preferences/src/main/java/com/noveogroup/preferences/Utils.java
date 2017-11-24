package com.noveogroup.preferences;

import android.content.SharedPreferences;

import com.noveogroup.preferences.lambda.Consumer;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings({"unchecked", "WeakerAccess", "PMD"})
final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("You can't instantiate Utility classes");
    }

    public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public static boolean editPreferences(final SharedPreferences preferences, final Consumer<SharedPreferences.Editor> consumer) {
        try {
            final SharedPreferences.Editor editor = preferences.edit();
            consumer.accept(editor);
            return editor.commit();
        } catch (final Exception original) {
            Utils.sneakyThrow(original);
            return false;
        }
    }
}
