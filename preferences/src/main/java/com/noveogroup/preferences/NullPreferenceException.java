package com.noveogroup.preferences;

@SuppressWarnings("WeakerAccess")
public class NullPreferenceException extends NullPointerException {

    NullPreferenceException(final PreferenceStrategy strategy) {
        super("Preference value can't be null with strategy(" + strategy.getClass() + ") that can't be null\n" +
                "Please ensure that you really want to save null\n" +
                "or call PreferenceStrategy.builder().canBeNull(true) - it's false by default.");
    }
}
