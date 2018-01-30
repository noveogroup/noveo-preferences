package com.noveogroup.preferences.rx;

import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.guava.Optional;
import com.noveogroup.preferences.lambda.Consumer;
import com.noveogroup.preferences.rx.api.RxPreference;
import com.noveogroup.preferences.rx.mock.TestSharedPreferences;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import rx.Subscription;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("PMD.AvoidFinalLocalVariable")
public class AllPreferenceTest {

    private NoveoRxPreferences noveoPreferencesWrapper;
    private TestSharedPreferences preferences;

    @Before
    public void before() {
        preferences = new TestSharedPreferences();
        preferences.edit()
                .putBoolean("boolean", com.noveogroup.preferences.rx.param.Constants.VALUE_BOOL)
                .putLong("long", com.noveogroup.preferences.rx.param.Constants.VALUE_LONG)
                .putString("string", com.noveogroup.preferences.rx.param.Constants.VALUE_STRING)
                .putInt("int", com.noveogroup.preferences.rx.param.Constants.VALUE_INT)
                .putFloat("float", com.noveogroup.preferences.rx.param.Constants.VALUE_FLOAT)
                .apply();

        noveoPreferencesWrapper = new NoveoRxPreferences(preferences);
    }

    @Test
    public void rx() throws IOException {
        final RxPreference<Map<String, ?>> allRx = noveoPreferencesWrapper.getAll();
        final Preference<Map<String, ?>> all = allRx.toBlocking();

        assertEquals("rx.get same as simple get",
                all.read().get(), allRx.read().toBlocking().value().get());

        allRx.remove().subscribe();
        assertEquals("rx.get same as simple get",
                all.read().get(), allRx.read().toBlocking().value().get());

        allRx.save(new HashMap<>()).subscribe(
                () -> {
                },
                throwable -> assertTrue("rx on save returns error",
                        throwable instanceof UnsupportedOperationException));
    }

    @Test
    public void provider() throws IOException {
        Subscription subscription;
        final long newValue = 5L;
        final String additionalKey = "new";
        final Consumer<Optional<Map<String, ?>>> consumerFive = mapOptional ->
                assertEquals("preferences set is 5 items",
                        mapOptional.get().size(),
                        preferences.getAll().size());
        final Consumer<Optional<Map<String, ?>>> consumerSix = mapOptional -> {
            assertEquals("preferences set is 6 items",
                    mapOptional.get().size(),
                    preferences.getAll().size());
            assertTrue("preferences contains additional 6 item",
                    mapOptional.get().containsKey(additionalKey));
        };

        final RxPreference<Map<String, ?>> allRx = noveoPreferencesWrapper.getAll();
        final Preference<Map<String, ?>> all = allRx.toBlocking();

        subscription = allRx.provider().observe(consumerFive);
        all.provider().addListener(consumerFive);
        noveoPreferencesWrapper.getString(com.noveogroup.preferences.rx.param.Constants.KEY_STRING).save("another");
        noveoPreferencesWrapper.getLong(com.noveogroup.preferences.rx.param.Constants.KEY_LONG).save(newValue);
        subscription.unsubscribe();
        all.provider().removeListener(consumerFive);

        subscription = allRx.provider().observe(consumerSix);
        all.provider().addListener(consumerSix);
        noveoPreferencesWrapper.getLong(additionalKey).save(newValue);
        subscription.unsubscribe();
        all.provider().removeListener(consumerSix);
    }

    @Test
    public void errors() {
        preferences.setBadFileSystem();
        final RxPreference<Map<String, ?>> preference = noveoPreferencesWrapper.getAll();
        assertNotNull("not null", preference);
        preference.remove().subscribe(
                () -> {
                },
                error -> assertTrue("Bad file system exception", error instanceof IOException));
    }

}
