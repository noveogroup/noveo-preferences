package com.noveogroup.preferences;

public class NullPreferenceException extends NullPointerException {

    public NullPreferenceException(final PreferenceStrategy strategy) {
        super("Preference value can't be null with strategy(" + strategy.getClass() + ") that can't be null\n" +
                "Please ensure that you really want to save null\n" +
                "or call PreferenceStrategy.builder().canBeNull(true) - it's false by default.");
    }
}
