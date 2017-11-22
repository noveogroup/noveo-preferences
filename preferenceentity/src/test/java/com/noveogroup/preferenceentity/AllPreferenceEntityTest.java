package com.noveogroup.preferenceentity;

import com.noveogroup.preferenceentity.api.PreferenceEntity;
import com.noveogroup.preferenceentity.api.RxPreferenceEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java8.util.Optional;

import static com.noveogroup.preferenceentity.Constants.KEY_LONG;
import static com.noveogroup.preferenceentity.Constants.KEY_STRING;
import static com.noveogroup.preferenceentity.Constants.VALUE_BOOL;
import static com.noveogroup.preferenceentity.Constants.VALUE_FLOAT;
import static com.noveogroup.preferenceentity.Constants.VALUE_INT;
import static com.noveogroup.preferenceentity.Constants.VALUE_LONG;
import static com.noveogroup.preferenceentity.Constants.VALUE_STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AllPreferenceEntityTest {

    private NoveoPreferences noveoPreferencesWrapper;
    private TestSharedPreferences preferences;

    @Before
    public void before() {
        preferences = new TestSharedPreferences();
        preferences.edit()
                .putBoolean("boolean", VALUE_BOOL)
                .putLong("long", VALUE_LONG)
                .putString("string", VALUE_STRING)
                .putInt("int", VALUE_INT)
                .putFloat("float", VALUE_FLOAT)
                .apply();

        noveoPreferencesWrapper = new NoveoPreferences(preferences);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void save() throws Exception {
        final PreferenceEntity<Map<String, ?>> all = noveoPreferencesWrapper.getAll();
        all.save(new HashMap<>());
    }

    @Test
    public void remove() throws Exception {
        final PreferenceEntity<Map<String, ?>> all = noveoPreferencesWrapper.getAll();

        assertEquals(all.read().get().size(), 5);
        all.remove();
        assertEquals(all.read().get().size(), 0);
        assertFalse(noveoPreferencesWrapper.getString(KEY_STRING).read().isPresent());

        final long defaultValue = 400L;
        assertEquals(noveoPreferencesWrapper.getLong(KEY_LONG, defaultValue).read().get().longValue(), defaultValue);
    }

    @Test
    public void read() throws Exception {
        final PreferenceEntity<Map<String, ?>> all = noveoPreferencesWrapper.getAll();
        assertContainsAll(all.read().get());
        preferences.edit().putFloat("test", 45F).commit();
        assertContainsAll(all.read().get());
    }

    private void assertContainsAll(final Map<String, ?> stringMap) {
        boolean containsAll = true;
        for (final String key : stringMap.keySet()) {
            containsAll &= preferences.getAll().containsKey(key);
        }
        assertTrue(containsAll);
    }

    @Test
    public void rx() throws Exception {
        final PreferenceEntity<Map<String, ?>> all = noveoPreferencesWrapper.getAll();
        final RxPreferenceEntity<Map<String, ?>> allRx = all.rx();

        assertEquals(all.read().get(), allRx.read().blockingGet().get());

        allRx.remove().subscribe();
        assertEquals(all.read().get(), allRx.read().blockingGet().get());

        allRx.save(new HashMap<>()).subscribe(
                () -> {
                },
                throwable -> assertTrue(throwable instanceof UnsupportedOperationException));
    }

    @Test
    public void provider() throws Exception {
        Disposable disposable;
        final String additionalKey = "new";
        final Consumer<Optional<Map<String, ?>>> consumerFive = mapOptional -> {
            assertEquals(mapOptional.get().size(), 5);
        };
        final Consumer<Optional<Map<String, ?>>> consumerSix = mapOptional -> {
            assertEquals(mapOptional.get().size(), 6);
            assertTrue(mapOptional.get().containsKey(additionalKey));
        };

        final PreferenceEntity<Map<String, ?>> all = noveoPreferencesWrapper.getAll();

        disposable = all.rx().provider().observe(consumerFive);
        all.provider().addListener(consumerFive);
        noveoPreferencesWrapper.getString(KEY_STRING).save("another");
        noveoPreferencesWrapper.getLong(KEY_LONG).save(5L);
        disposable.dispose();
        all.provider().removeListener(consumerFive);

        disposable = all.rx().provider().observe(consumerSix);
        all.provider().addListener(consumerSix);
        noveoPreferencesWrapper.getLong(additionalKey).save(5L);
        disposable.dispose();
        all.provider().removeListener(consumerSix);
    }

}