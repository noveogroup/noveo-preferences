package com.noveogroup.preferenceentity;

import com.noveogroup.preferenceentity.api.PreferenceEntity;
import com.noveogroup.preferenceentity.api.RxPreferenceEntity;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java8.util.Optional;

import static com.noveogroup.preferenceentity.Constants.KEY_BOOL;
import static com.noveogroup.preferenceentity.Constants.KEY_FLOAT;
import static com.noveogroup.preferenceentity.Constants.KEY_INT;
import static com.noveogroup.preferenceentity.Constants.KEY_LONG;
import static com.noveogroup.preferenceentity.Constants.KEY_STRING;
import static com.noveogroup.preferenceentity.Constants.VALUE_BOOL;
import static com.noveogroup.preferenceentity.Constants.VALUE_FLOAT;
import static com.noveogroup.preferenceentity.Constants.VALUE_INT;
import static com.noveogroup.preferenceentity.Constants.VALUE_LONG;
import static com.noveogroup.preferenceentity.Constants.VALUE_STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class NoveoPreferenceEntityTest {

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

    @Test
    public void constructor() {
        final PreferenceEntity<String> stringEntity = new NoveoPreferenceEntity<>(KEY_STRING, PreferenceStrategy.STRING, preferences);
        final PreferenceEntity<String> stringEntity2 = noveoPreferencesWrapper.getString(KEY_STRING, null);

        assertEquals(stringEntity.read().get(), stringEntity2.read().get());
    }

    @Test
    public void read() throws Exception {
        assertEquals("read boolean", noveoPreferencesWrapper.getBoolean(KEY_BOOL).read().get(), preferences.getBoolean("boolean", false));
        assertEquals("read long", (noveoPreferencesWrapper.getLong(KEY_LONG).read().get().longValue()), preferences.getLong("long", 0L));
        assertEquals("read string", noveoPreferencesWrapper.getString(KEY_STRING).read().get(), preferences.getString("string", "none"));
        assertEquals("read int", (noveoPreferencesWrapper.getInt(KEY_INT).read().get().intValue()), preferences.getInt("int", 0));
        assertEquals("read float", noveoPreferencesWrapper.getFloat(KEY_FLOAT).read().get(), preferences.getFloat("float", 2222f), 1f);
    }

    @Test
    public void remove() throws Exception {
        noveoPreferencesWrapper.getBoolean(KEY_BOOL).remove();
        noveoPreferencesWrapper.getLong(KEY_LONG).remove();
        noveoPreferencesWrapper.getString(KEY_STRING).remove();
        noveoPreferencesWrapper.getInt(KEY_INT).remove();
        noveoPreferencesWrapper.getFloat(KEY_FLOAT).remove();

        for (final String key : preferences.getAll().keySet()) {
            assertFalse("remove(" + key + ")", preferences.contains(key));
        }
    }

    @Test
    public void save() throws Exception {
        final long local_value_long = 10234L;
        final String local_value_string = "local string";
        final int local_value_int = 563;
        final float local_value_float = 2345.235f;
        final boolean local_value_bool = true;

        noveoPreferencesWrapper.getBoolean(KEY_BOOL).save(local_value_bool);
        noveoPreferencesWrapper.getLong(KEY_LONG).save(local_value_long);
        noveoPreferencesWrapper.getString(KEY_STRING).save(local_value_string);
        noveoPreferencesWrapper.getInt(KEY_INT).save(local_value_int);
        noveoPreferencesWrapper.getFloat(KEY_FLOAT).save(local_value_float);

        assertEquals("save boolean", local_value_bool, preferences.getBoolean("boolean", false));
        assertEquals("save long", local_value_long, preferences.getLong("long", 0L));
        assertEquals("save string", local_value_string, preferences.getString("string", "none"));
        assertEquals("save int", local_value_int, preferences.getInt("int", 0));
        assertEquals("save float", local_value_float, preferences.getFloat("float", 2222f), 1f);
    }

    @Test
    public void rx() throws Exception {
        final long defaultValue = 2341234L;
        final PreferenceEntity<Long> simple = noveoPreferencesWrapper.getLong(KEY_LONG, defaultValue);
        final RxPreferenceEntity<Long> rx = simple.rx();
        assertEquals(rx.read().blockingGet().get(), simple.read().get());
        assertEquals(simple.rx(), simple.rx());

        rx.remove().subscribe();
        assertEquals(rx.read().blockingGet().get(), simple.read().get());
        assertEquals((rx.read().blockingGet().get().longValue()), defaultValue);
    }

    @Test
    public void provider() throws Exception {
        final long firstValue = 1L;
        final long secondValue = 2L;
        final long defaultValue = 2341234L;
        final PreferenceEntity<Long> simple = noveoPreferencesWrapper.getLong(KEY_LONG, defaultValue);
        final RxPreferenceEntity<Long> rx = simple.rx();

        final Consumer<Optional<Long>> firstValueListener = longOptional ->
                assertEquals(longOptional.get().longValue(), firstValue);
        final Consumer<Optional<Long>> secondValueListener = longOptional ->
                assertEquals(longOptional.get().longValue(), secondValue);
        final Consumer<Optional<Long>> defaultValueListener = longOptional ->
                assertEquals(longOptional.get().longValue(), defaultValue);


        Disposable disposable;

        disposable = rx.provider().observe(firstValueListener);
        simple.provider().addListener(firstValueListener);
        simple.save(firstValue);
        simple.provider().removeListener(firstValueListener);
        disposable.dispose();

        disposable = rx.provider().observe(defaultValueListener);
        simple.provider().addListener(defaultValueListener);
        simple.remove();
        simple.provider().removeListener(defaultValueListener);
        disposable.dispose();

        disposable = rx.provider().observe(secondValueListener);
        simple.provider().addListener(secondValueListener);
        simple.save(secondValue);
        simple.provider().removeListener(secondValueListener);
        disposable.dispose();
    }

    @Test(expected = IOException.class)
    public void saveBrokenFs() throws IOException {
        preferences.setBadFileSystem(true);
        noveoPreferencesWrapper.getLong(KEY_LONG).save(10L);
    }

    @Test(expected = IOException.class)
    public void removeBrokenFs() throws IOException {
        preferences.setBadFileSystem(true);
        noveoPreferencesWrapper.getLong(KEY_LONG).remove();
    }

    @Test
    public void readBrokenFs() throws IOException {
        preferences.setBadFileSystem(true);
        assertEquals(noveoPreferencesWrapper.getLong(KEY_LONG).read().get().longValue(), VALUE_LONG);
    }

}