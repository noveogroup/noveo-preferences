package com.noveogroup.preferenceentity;

import com.noveogroup.preferenceentity.api.PreferenceEntity;
import com.noveogroup.preferenceentity.api.RxPreferenceEntity;
import com.noveogroup.preferenceentity.mock.TestSharedPreferences;
import com.noveogroup.preferenceentity.param.Constants;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.TestScheduler;
import java8.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("PMD.AvoidFinalLocalVariable")
public class NoveoRxValueProviderTest {

    private NoveoPreferences noveoPreferencesWrapper;

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

        noveoPreferencesWrapper = new NoveoPreferences(preferences);
    }

    @Test
    public void observe() throws IOException {
        final long firstValue = 1L;
        final long defaultValue = 2341234L;
        final int delayTime = 10;
        final PreferenceEntity<Long> simple = noveoPreferencesWrapper.getLong(Constants.KEY_LONG, defaultValue);
        final RxPreferenceEntity<Long> rx = simple.rx();

        final Consumer<Optional<Long>> firstValueListener = longOptional ->
                assertEquals("rx value provider catch first value", longOptional.get().longValue(), firstValue);
        final TestScheduler scheduler = new TestScheduler();

        assertNotNull("Provider not null", rx.provider());

        rx.provider().observe(firstValueListener);
        rx.provider().observe(scheduler, firstValueListener);
        rx.provider().asFlowable().subscribe(firstValueListener);
        simple.provider().addListener(firstValueListener);
        simple.save(firstValue);

        scheduler.advanceTimeBy(delayTime, TimeUnit.SECONDS);
    }

}
