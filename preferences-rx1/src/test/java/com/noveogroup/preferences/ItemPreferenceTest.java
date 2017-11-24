package com.noveogroup.preferences;

import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.api.RxPreference;
import com.noveogroup.preferences.guava.Optional;
import com.noveogroup.preferences.lambda.Consumer;
import com.noveogroup.preferences.mock.TestSharedPreferences;
import com.noveogroup.preferences.param.Constants;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import rx.Subscription;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("PMD.AvoidFinalLocalVariable")
public class ItemPreferenceTest {

    private NoveoRxPreferences noveoPreferencesWrapper;

    @Before
    public void before() {
        final TestSharedPreferences preferences = new TestSharedPreferences();
        preferences.edit()
                .putBoolean("boolean", Constants.VALUE_BOOL)
                .putLong("long", Constants.VALUE_LONG)
                .putString("string", Constants.VALUE_STRING)
                .putInt("int", Constants.VALUE_INT)
                .putFloat("float", Constants.VALUE_FLOAT)
                .apply();

        noveoPreferencesWrapper = new NoveoRxPreferences(preferences);
    }

    @Test
    public void rx() throws IOException {
        final long defaultValue = 2341234L;
        final RxPreference<Long> rx = noveoPreferencesWrapper.getLong(Constants.KEY_LONG, defaultValue);
        final Preference<Long> simple = rx.toBlocking();
        assertEquals("rx and simple get the same",
                rx.read().toBlocking().value().get(), simple.read().get());
        assertEquals("rx lazy get the same",
                rx.toBlocking(), rx.toBlocking());

        rx.remove().subscribe();
        assertEquals("rx and simple get the same after remove",
                rx.read().toBlocking().value().get(), simple.read().get());
        assertEquals("rx get is default value after get",
                rx.read().toBlocking().value().get().longValue(), defaultValue);
    }

    @Test
    public void provider() throws IOException {
        final long firstValue = 1L;
        final long secondValue = 2L;
        final long defaultValue = 2341234L;
        final RxPreference<Long> rx = noveoPreferencesWrapper.getLong(Constants.KEY_LONG, defaultValue);
        final Preference<Long> simple = rx.toBlocking();

        final Consumer<Optional<Long>> firstValueListener = longOptional ->
                assertEquals("provider got first value", longOptional.get().longValue(), firstValue);
        final Consumer<Optional<Long>> secondValueListener = longOptional ->
                assertEquals("provider got second value", longOptional.get().longValue(), secondValue);
        final Consumer<Optional<Long>> defaultValueListener = longOptional ->
                assertEquals("provider got default value", longOptional.get().longValue(), defaultValue);

        assertNotNull("Provider exists", rx.provider());

        Subscription subscription;

        subscription = rx.provider().observe(firstValueListener);
        simple.provider().addListener(firstValueListener);
        simple.save(firstValue);
        simple.provider().removeListener(firstValueListener);
        subscription.unsubscribe();

        subscription = rx.provider().observe(defaultValueListener);
        simple.provider().addListener(defaultValueListener);
        simple.remove();
        simple.provider().removeListener(defaultValueListener);
        subscription.unsubscribe();

        subscription = rx.provider().observe(secondValueListener);
        simple.provider().addListener(secondValueListener);
        simple.save(secondValue);
        simple.provider().removeListener(secondValueListener);
        subscription.unsubscribe();
    }

    @Test
    public void backCompatibility() {
        final RxPreference<Boolean> rxPref = noveoPreferencesWrapper.getBoolean("boolean");
        assertEquals("same object", rxPref.read().toBlocking().value(), rxPref.toBlocking().read());
    }

}
