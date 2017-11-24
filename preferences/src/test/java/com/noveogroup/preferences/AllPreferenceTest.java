package com.noveogroup.preferences;

import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.guava.Optional;
import com.noveogroup.preferences.lambda.Consumer;
import com.noveogroup.preferences.mock.TestSharedPreferences;
import com.noveogroup.preferences.param.Constants;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("PMD.AvoidFinalLocalVariable")
public class AllPreferenceTest {

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
        final Preference<Map<String, ?>> all = noveoPreferencesWrapper.getAll();
        all.save(new HashMap<>());
    }

    @Test
    public void remove() throws IOException {
        final long defaultValue = 400L;
        final Preference<Map<String, ?>> all = noveoPreferencesWrapper.getAll();
        final Preference<Long> one = noveoPreferencesWrapper.getLong(Constants.KEY_LONG, defaultValue);
        final Preference<String> some = noveoPreferencesWrapper.getString(Constants.KEY_STRING);

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
        final Preference<Map<String, ?>> all = noveoPreferencesWrapper.getAll();
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
    public void provider() throws IOException {
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

        final Preference<Map<String, ?>> all = noveoPreferencesWrapper.getAll();

        all.provider().addListener(consumerFive);
        noveoPreferencesWrapper.getString(Constants.KEY_STRING).save("another");
        noveoPreferencesWrapper.getLong(Constants.KEY_LONG).save(newValue);
        all.provider().removeListener(consumerFive);

        all.provider().addListener(consumerSix);
        noveoPreferencesWrapper.getLong(additionalKey).save(newValue);
        all.provider().removeListener(consumerSix);
    }

    @Test(expected = IOException.class)
    public void errors() {
        preferences.setBadFileSystem();
        noveoPreferencesWrapper.getAll().remove();
    }

}
