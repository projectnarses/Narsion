package org.narses.narsion.util;

import com.moandjiezana.toml.Toml;

import java.util.Map;
import java.util.function.Function;

public class TomlUtils {

    // Used to generate tomlkeys
    public static class Keys {
        /**
         * Creates a generic version of a toml key
         * @param key
         * @param tomlToJava
         * @param <K>
         * @param <V>
         * @return
         */
        public static <K, V> TomlKey<V> GENERIC(String key, Function<K, V> tomlToJava) {
            return new TomlKey<V>(key, (obj) -> tomlToJava.apply((K) obj));
        }

        /**
         * Generates an enum version of a toml key
         * @param key
         * @param enumClass
         * @param <E>
         * @return
         */
        public static <E extends Enum<E>> TomlKey<E> ENUM(String key, Class<E> enumClass) {
            return new TomlKey<>(key, (obj) -> Enum.valueOf(enumClass, (String) obj));
        }

        public static <K> TomlKey<K> CAST(String key) {
            return new TomlKey<>(key, obj -> (K) obj);
        }
    }

    public record TomlKey<V>(String key, Function<Object, V> tomlToJava) {

        @SuppressWarnings("unchecked")
        public V get(Toml toml) {
            final Map<String, Object> values = ReflectionUtils.getPrivateField(Toml.class, "values", toml);
            return convert(values.get(key));
        }

        @SuppressWarnings("unchecked")
        protected V convert(Object value) {
            return tomlToJava.apply(value);
        }
    }
}
