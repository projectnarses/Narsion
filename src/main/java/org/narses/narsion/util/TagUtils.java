package org.narses.narsion.util;

import net.minestom.server.tag.Tag;
import net.minestom.server.tag.TagReadable;
import net.minestom.server.tag.TagSerializer;
import net.minestom.server.tag.TagWritable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class TagUtils {
    public static <T extends Enum<T>> Tag<T> Enum(String key, Class<T> enumClass) {
        TagSerializer<T> serializer = new StandardGenerator<>(
            Tag.String(key),
            (string) -> Enum.valueOf(enumClass, string),
            Enum::name
        );

        return Tag.Structure(key, serializer);
    }

    public static <T extends Serializable> Tag<T> Serializable(String key) {
        TagSerializer<T> serializer = new StandardGenerator<>(
                Tag.ByteArray(key),
                TagUtils::convertToObject,
                TagUtils::convertToBytes
        );

        return Tag.Structure(key, serializer);
    }

    // Byte conversion
    // TODO: Move this to a math util
    public static <T extends Serializable> byte[] convertToBytes(T obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] bytes = bos.toByteArray();
        return bytes;
    }
    public static <T extends Serializable> T convertToObject(byte[] byteArray) {
        ByteArrayInputStream bos = new ByteArrayInputStream(byteArray);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bos);
            T output =  (T) ois.readObject();
            ois.close();
            return output;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Generator
    private record StandardGenerator<K, V>(
        Tag<K> tag,
        Function<K, V> loader,
        Function<V, K> saver
    ) implements TagSerializer<V> {

        @Override
        public @Nullable V read(@NotNull TagReadable reader) {
            K value = Objects.requireNonNull(reader.getTag(tag), "TagReadble#getTag returned a null value while trying to convert");
            return loader.apply(value);
        }

        @Override
        public void write(@NotNull TagWritable writer, @Nullable V value) {
            if (value != null) {
                writer.setTag(tag, saver.apply(value));
                return;
            }

            writer.setTag(tag, null);
        }
    }
}
