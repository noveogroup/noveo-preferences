package com.noveogroup.preferences.rx;

import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.guava.Optional;
import com.noveogroup.preferences.lambda.Consumer;
import com.noveogroup.preferences.rx.api.RxPreference;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.schedulers.TestScheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("PMD.AvoidFinalLocalVariable")
public class NoveoRxPreferenceProviderTest {

    private NoveoRxPreferences noveoPreferencesWrapper;

    @Before
    public void before() {
        final com.noveogroup.preferences.rx.mock.TestSharedPreferences preferences = new com.noveogroup.preferences.rx.mock.TestSharedPreferences();
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
    public void observe() throws IOException {
        final long firstValue = 1L;
        final long defaultValue = 2341234L;
        final int delayTime = 10;
        final RxPreference<Long> rx = noveoPreferencesWrapper.getLong(com.noveogroup.preferences.rx.param.Constants.KEY_LONG, defaultValue);
        final Preference<Long> simple = rx.toBlocking();

        final Consumer<Optional<Long>> firstValueListener = longOptional ->
                assertEquals("rx value provider catch first value", longOptional.get().longValue(), firstValue);
        final TestScheduler scheduler = new TestScheduler();

        assertNotNull("Provider not null", rx.provider());

        rx.provider().observe(firstValueListener);
        rx.provider().observe(scheduler, firstValueListener);
        rx.provider().asObservable().subscribe(firstValueListener::accept);
        simple.provider().addListener(firstValueListener);
        simple.save(firstValue);

        scheduler.advanceTimeBy(delayTime, TimeUnit.SECONDS);
    }

}
