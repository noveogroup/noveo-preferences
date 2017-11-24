package com.noveogroup.preferences;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class PreferenceStrategy<T> {

    /**
     * Strategy to serialize/deserialize {@link Boolean} value.
     * <p>
     * <p>uses:</p>
     * <ul>
     * <li>{@link SharedPreferences.Editor#putBoolean(String, boolean)}</li>
     * <li>{@link SharedPreferences#getBoolean(String, boolean)}</li>
     * <li>Can't be null</li>
     * </ul>
     */
    public static final PreferenceStrategy<Boolean> BOOLEAN = PreferenceStrategy.<Boolean>builder()
            .setAction(SharedPreferences.Editor::putBoolean)
            .getAction(SharedPreferences::getBoolean)
            .build();

    /**
     * Strategy to serialize/deserialize {@link String} value.
     *
     * <p>uses:</p>
     * <ul>
     *     <li>{@link SharedPreferences.Editor#putString(String, String)}</li>
     *     <li>{@link SharedPreferences#getString(String, String)}</li>
     *     <li>Nullable</li>
     * </ul>
     */
    public static final PreferenceStrategy<String> STRING = PreferenceStrategy.<String>builder()
            .setAction(SharedPreferences.Editor::putString)
            .getAction(SharedPreferences::getString)
            .canBeNull(true)
            .build();

    /**
     * Strategy to serialize/deserialize {@link Float} value.
     *
     * <p>uses:</p>
     * <ul>
     *     <li>{@link SharedPreferences.Editor#putFloat(String, float)}</li>
     *     <li>{@link SharedPreferences#getFloat(String, float)}</li>
     *     <li>Can't be null</li>
     * </ul>
     */
    public static final PreferenceStrategy<Float> FLOAT = PreferenceStrategy.<Float>builder()
            .setAction(SharedPreferences.Editor::putFloat)
            .getAction(SharedPreferences::getFloat)
            .build();

    /**
     * Strategy to serialize/deserialize {@link Integer} value.
     *
     * <p>uses:</p>
     * <ul>
     *     <li>{@link SharedPreferences.Editor#putInt(String, int)}</li>
     *     <li>{@link SharedPreferences#getInt(String, int)}</li>
     *     <li>Can't be null</li>
     * </ul>
     */
    public static final PreferenceStrategy<Integer> INTEGER = PreferenceStrategy.<Integer>builder()
            .setAction(SharedPreferences.Editor::putInt)
            .getAction(SharedPreferences::getInt)
            .build();

    /**
     * Strategy to serialize/deserialize {@link Long} value.
     *
     * <p>uses:</p>
     * <ul>
     *     <li>{@link SharedPreferences.Editor#putLong(String, long)}</li>
     *     <li>{@link SharedPreferences#getLong(String, long)}</li>
     *     <li>Can't be null</li>
     * </ul>
     */
    public static final PreferenceStrategy<Long> LONG = PreferenceStrategy.<Long>builder()
            .setAction(SharedPreferences.Editor::putLong)
            .getAction(SharedPreferences::getLong)
            .build();

    public final KeyFilter keyFilter;
    public final boolean canBeNull;
    private final SetAction<T> setAction;
    private final GetAction<T> getAction;
    private final RemoveAction removeAction;

    /**
     * Strategy constructor with All params.
     * <strong>Better use {@link PreferenceStrategy.Builder} via static {@link #builder()} method.</strong>
     * @param setAction serialize your entity.
     * @param getAction deserialize your entity.
     * @param removeAction remove your entity. Use by default {@link RemoveAction#REMOVE_BY_KEY}. If your preference takes from one key.
     * @param keyFilter compare if key related to your entity. Use by default {@link KeyFilter#EQUALS}. If your preference consists from one key.
     * @param canBeNull fail earlier. Disable null values for your preference!
     */
    public PreferenceStrategy(final SetAction<T> setAction, final GetAction<T> getAction, final RemoveAction removeAction, final KeyFilter keyFilter, final boolean canBeNull) {
        this.keyFilter = keyFilter;
        this.setAction = setAction;
        this.getAction = getAction;
        this.removeAction = removeAction;
        this.canBeNull = canBeNull;
    }

    /**
     * Get {@link PreferenceStrategy.Builder} instance.
     * @param <T> Preference type.
     * @return {@link PreferenceStrategy.Builder} instance.
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    void set(final SharedPreferences.Editor editor, final String key, @Nullable final T value) {
        setAction.call(editor, key, value);
    }

    T get(final SharedPreferences preferences, final String key, @Nullable final T value) {
        return getAction.call(preferences, key, value);
    }

    void remove(final SharedPreferences.Editor editor, final String key) {
        removeAction.call(editor, key);
    }

    void checkNullOrThrow(final T value) {
        if (canBeNull) {
            return;
        }

        if (value == null) {
            throw new NullPreferenceException(this);
        }
    }

    /**
     * Serialization Action, put your params into editor.
     * <p>you can use Gson/Jackson to serialize entity into json-{@link String}</p>
     * <p>or you can disassemble your object into fields under modified key value</p>
     *
     * @param <T> {@link com.noveogroup.preferences.api.Preference} type.
     */
    public interface SetAction<T> {
        void call(SharedPreferences.Editor editor, String key, @Nullable T value);
    }

    /**
     * Deserialization Action, get your params from preferences and assemble object.
     * <p>Should be symmetrical to {@link GetAction}</p>
     *
     * @param <T> {@link com.noveogroup.preferences.api.Preference} type.
     */
    public interface GetAction<T> {
        T call(SharedPreferences preferences, String key, @Nullable T value);
    }

    /**
     * Remove Action, remove your params with editor.
     * <p>If you store entity under single key - you can use predefined {@link #REMOVE_BY_KEY} instance.</p>
     */
    public interface RemoveAction {
        RemoveAction REMOVE_BY_KEY = SharedPreferences.Editor::remove;

        void call(SharedPreferences.Editor editor, String key);
    }

    /**
     * KeyFilter matches keys in {@link SharedPreferences} with your entity.
     * <p>If you store entity under single key - you can use predefined {@link #EQUALS} instance.</p>
     * <p>KeyFilter required for correct {@link com.noveogroup.preferences.api.PreferenceProvider} matching.</p>
     */
    public interface KeyFilter {
        @SuppressWarnings("StringEquality")
        KeyFilter EQUALS = (a, b) -> a == b || a != null && b != null && a.length() == b.length() && a.equals(b);

        boolean compare(String storedKey, String entityKey);
    }

    /**
     * Builder for {@link PreferenceStrategy} with default values.
     * <ul>
     *     <li>{@link SetAction} - <strong>mandatory</strong>, or you'll get IllegalArgumentException</li>
     *     <li>{@link GetAction} - <strong>mandatory</strong>, or you'll get IllegalArgumentException</li>
     *     <li>{@link RemoveAction} - optional. {@link RemoveAction#REMOVE_BY_KEY} will be used as default</li>
     *     <li>{@link KeyFilter} - optional. {@link KeyFilter#EQUALS} will be used as default</li>
     *     <li>canBeNull - optional. false by default</li>
     * </ul>
     * @param <T> Preference type.
     */
    @SuppressWarnings({"SameParameterValue", "PMD.AvoidFieldNameMatchingMethodName"})
    public static class Builder<T> {
        private SetAction<T> setAction;
        private GetAction<T> getAction;
        private RemoveAction removeAction;
        private KeyFilter keyFilter;
        private boolean canBeNull;

        Builder() {
            this.canBeNull = false;
            this.keyFilter = KeyFilter.EQUALS;
            this.removeAction = RemoveAction.REMOVE_BY_KEY;
            this.setAction = (a, b, c) -> {
                throw new IllegalArgumentException("set action not defined");
            };
            this.getAction = (a, b, c) -> {
                throw new IllegalArgumentException("get action not defined");
            };
        }

        /**
         * Set {@link RemoveAction}.
         * @param removeAction - optional, {@link RemoveAction#REMOVE_BY_KEY} by default.
         * @return {@link Builder} instance.
         */
        public Builder<T> removeAction(final RemoveAction removeAction) {
            this.removeAction = removeAction;
            return this;
        }

        /**
         * Set {@link SetAction}.
         * @param setAction - mandatory.
         * @return {@link Builder} instance.
         */
        public Builder<T> setAction(final SetAction<T> setAction) {
            this.setAction = setAction;
            return this;
        }

        /**
         * Set {@link GetAction}.
         * @param getAction - mandatory.
         * @return {@link Builder} instance.
         */
        public Builder<T> getAction(final GetAction<T> getAction) {
            this.getAction = getAction;
            return this;
        }

        /**
         * Set {@link KeyFilter}.
         * @param keyFilter - optional. {@link KeyFilter#EQUALS} by default.
         * @return {@link Builder} instance.
         */
        public Builder<T> keyFilter(final KeyFilter keyFilter) {
            this.keyFilter = keyFilter;
            return this;
        }

        /**
         * Setup nullability to fail earlier with Nulls.
         * @param canBeNull - optional. false by default.
         * @return {@link Builder} instance.
         */
        public Builder<T> canBeNull(final boolean canBeNull) {
            this.canBeNull = canBeNull;
            return this;
        }

        /**
         * @return {@link PreferenceStrategy}.
         */
        public PreferenceStrategy<T> build() {
            return new PreferenceStrategy<>(setAction, getAction, removeAction, keyFilter, canBeNull);
        }
    }
}
