package com.noveogroup.preferenceentity;

import com.noveogroup.preferenceentity.api.PreferenceEntity;
import com.noveogroup.preferenceentity.api.RxPreferenceEntity;
import com.noveogroup.preferenceentity.mock.TestSharedPreferences;
import com.noveogroup.preferenceentity.param.Constants;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java8.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("PMD.AvoidFinalLocalVariable")
public class AllPreferenceEntityTest {

    private NoveoPreferences noveoPreferencesWrapper;
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

        noveoPreferencesWrapper = new NoveoPreferences(preferences);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void save() throws IOException {
        final PreferenceEntity<Map<String, ?>> all = noveoPreferencesWrapper.getAll();
        all.save(new HashMap<>());
    }

    @Test
    public void remove() throws IOException {
        final long defaultValue = 400L;
        final PreferenceEntity<Map<String, ?>> all = noveoPreferencesWrapper.getAll();
        final PreferenceEntity<Long> one = noveoPreferencesWrapper.getLong(Constants.KEY_LONG, defaultValue);
        final PreferenceEntity<String> some = noveoPreferencesWrapper.getString(Constants.KEY_STRING);

        assertEquals("initial set - 5 items", all.read().get().size(), preferences.getAll().size());

        all.remove();
        assertEquals("after remove - 4 items", all.read().get().size(), preferences.getAll().size());
        assertFalse("some string entity not present", some.read().isPresent());
        assertEquals("removed value get returns default value",
                one.read().get().longValue(),
                defaultValue);
    }

    @Test
    public void read() {
        final float defaultFloat = 45F;
        final PreferenceEntity<Map<String, ?>> all = noveoPreferencesWrapper.getAll();
        assertContainsAll(all.read().get());
        preferences.edit().putFloat("test", defaultFloat).commit();
        assertContainsAll(all.read().get());
    }

    private void assertContainsAll(final Map<String, ?> stringMap) {
        boolean containsAll = true;
        for (final String key : stringMap.keySet()) {
            containsAll &= preferences.getAll().containsKey(key);
        }
        assertTrue("getAll read contains the same set as preferences", containsAll);
    }

    @Test
    public void rx() throws IOException {
        final PreferenceEntity<Map<String, ?>> all = noveoPreferencesWrapper.getAll();
        final RxPreferenceEntity<Map<String, ?>> allRx = all.rx();

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
        final Consumer<Optional<Map<String, ?>>> consumerFive = mapOptional -> {
            assertEquals("preferences set is 5 items",
                    mapOptional.get().size(),
                    preferences.getAll().size());
        };
        final Consumer<Optional<Map<String, ?>>> consumerSix = mapOptional -> {
            assertEquals("preferences set is 6 items",
                    mapOptional.get().size(),
                    preferences.getAll().size());
            assertTrue("preferences contains additional 6 item",
                    mapOptional.get().containsKey(additionalKey));
        };

        final PreferenceEntity<Map<String, ?>> all = noveoPreferencesWrapper.getAll();

        disposable = all.rx().provider().observe(consumerFive);
        all.provider().addListener(consumerFive);
        noveoPreferencesWrapper.getString(Constants.KEY_STRING).save("another");
        noveoPreferencesWrapper.getLong(Constants.KEY_LONG).save(newValue);
        disposable.dispose();
        all.provider().removeListener(consumerFive);

        disposable = all.rx().provider().observe(consumerSix);
        all.provider().addListener(consumerSix);
        noveoPreferencesWrapper.getLong(additionalKey).save(newValue);
        disposable.dispose();
        all.provider().removeListener(consumerSix);
    }

}
