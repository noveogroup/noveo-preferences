package com.noveogroup.preferences;

import com.noveogroup.preferences.rx.mock.TestSharedPreferences;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by avaytsekhovskiy on 24/11/2017.
 */
public class PreferenceStrategyTest {

    @Test(expected = NullPreferenceException.class)
    public void checkStrategyParams() {
        final NoveoPreferences preferences = new NoveoPreferences(new TestSharedPreferences());

        final PreferenceStrategy<Object> nonNullStrategy = PreferenceStrategy.builder().canBeNull(false).build();
        final PreferenceStrategy<Object> nullableStrategy = PreferenceStrategy.builder().canBeNull(true).build();
        assertNotNull("nullable preference created", preferences.getObject("key", nonNullStrategy, null));
        preferences.getObject("key", nullableStrategy, null);
    }
}
