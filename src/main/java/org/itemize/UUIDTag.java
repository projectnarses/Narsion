package org.itemize;

import net.minestom.server.tag.Tag;
import net.minestom.server.tag.TagReadable;
import net.minestom.server.tag.TagSerializer;
import net.minestom.server.tag.TagWritable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
public class UUIDTag implements TagSerializer<UUID> {

    private final @NotNull Tag<List<Long>> longArrayTag;

    private UUIDTag(@NotNull String key) {
        this.longArrayTag = Tag.Long(key).list();
    }

    public static @NotNull Tag<UUID> get(@NotNull String key) {
        return Tag.Structure(key, new UUIDTag(key));
    }

    @Override
    public @Nullable UUID read(@NotNull TagReadable tagReadable) {
        List<Long> lowHigh = tagReadable.getTag(longArrayTag);
        if (lowHigh == null) {
            return null;
        }
        return new UUID(lowHigh.get(0), lowHigh.get(1));
    }

    @Override
    public void write(@NotNull TagWritable tagWritable, @Nullable UUID uuid) {
        if (uuid == null) {
            tagWritable.removeTag(longArrayTag);
            return;
        }
        List<Long> longs = List.of(uuid.getLeastSignificantBits(), uuid.getMostSignificantBits());
        tagWritable.setTag(longArrayTag, longs);
    }
}
