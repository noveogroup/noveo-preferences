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
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

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
        final RxPreference<Map<String, ?>> allRx = noveoPreferencesWrapper.getAll();
        final Preference<Map<String, ?>> all = allRx.toBlocking();

        assertEquals("rx.get same as simple get",
                all.read().get(), allRx.read().blockingGet().get());

        allRx.remove().subscribe();
        assertEquals("rx.get same as simple get",
                all.read().get(), allRx.read().blockingGet().get());

        allRx.save(new HashMap<>()).subscribe(
                () -> {
                },
                throwable -> assertTrue("rx on save returns error",
                        throwable instanceof UnsupportedOperationException));
    }

    @Test
    public void provider() throws IOException {
        Disposable disposable;
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

        disposable = allRx.provider().observe(consumerFive);
        all.provider().addListener(consumerFive);
        noveoPreferencesWrapper.getString(Constants.KEY_STRING).save("another");
        noveoPreferencesWrapper.getLong(Constants.KEY_LONG).save(newValue);
        disposable.dispose();
        all.provider().removeListener(consumerFive);

        disposable = allRx.provider().observe(consumerSix);
        all.provider().addListener(consumerSix);
        noveoPreferencesWrapper.getLong(additionalKey).save(newValue);
        disposable.dispose();
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
