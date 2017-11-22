package com.noveogroup.preferenceentity;

import com.noveogroup.preferenceentity.api.PreferenceEntity;
import com.noveogroup.preferenceentity.api.RxPreferenceEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.TestScheduler;
import java8.util.Optional;

import static com.noveogroup.preferenceentity.Constants.KEY_LONG;
import static com.noveogroup.preferenceentity.Constants.VALUE_BOOL;
import static com.noveogroup.preferenceentity.Constants.VALUE_FLOAT;
import static com.noveogroup.preferenceentity.Constants.VALUE_INT;
import static com.noveogroup.preferenceentity.Constants.VALUE_LONG;
import static com.noveogroup.preferenceentity.Constants.VALUE_STRING;
import static org.junit.Assert.assertEquals;

public class NoveoRxValueProviderTest {

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
    public void observe() throws Exception {
        final long firstValue = 1L;
        final long defaultValue = 2341234L;
        final PreferenceEntity<Long> simple = noveoPreferencesWrapper.getLong(KEY_LONG, defaultValue);
        final RxPreferenceEntity<Long> rx = simple.rx();

        final Consumer<Optional<Long>> firstValueListener = longOptional ->
                assertEquals(longOptional.get().longValue(), firstValue);
        final TestScheduler scheduler = new TestScheduler();

        rx.provider().observe(firstValueListener);
        rx.provider().observe(scheduler, firstValueListener);
        rx.provider().asFlowable().subscribe(firstValueListener);
        simple.provider().addListener(firstValueListener);
        simple.save(firstValue);

        scheduler.advanceTimeBy(10, TimeUnit.SECONDS);
    }

    @Test
    public void asFlowable() throws Exception {
    }

}