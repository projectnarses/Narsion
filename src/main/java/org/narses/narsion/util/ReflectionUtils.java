package org.narses.narsion.util;

import java.lang.reflect.Field;

public class ReflectionUtils {
    /**
     * Gets the value of a private field on the specified object
     * @param clazz
     * @param fieldName
     * @param object
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T, V> V getPrivateField(Class<T> clazz, String fieldName, T object) {
        try {
            Field field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            return (V) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
