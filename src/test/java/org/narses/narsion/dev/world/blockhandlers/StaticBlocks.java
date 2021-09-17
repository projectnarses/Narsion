package org.narses.narsion.dev.world.blockhandlers;

import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTList;

import java.util.Collection;
import java.util.List;

public enum StaticBlocks {
    SKULL("minecraft:skull", Tag.NBT("SkullOwner")),
    BED("minecraft:bed"),
    FURNACE("minecraft:furnace"),
    CHEST("minecraft:chest"),
    BARREL("minecraft:barrel"),
    BANNER("minecraft:banner", Tag.String("CustomName"), Tag.NBT("Patterns")),
    SIGN("minecraft:sign", Tag.Byte("GlowingText"), Tag.String("Color"), Tag.String("Text1"), Tag.String("Text2"), Tag.String("Text3"), Tag.String("Text4")),
    CAMPFIRE("minecraft:campfire"),
    ENCHANTING_TABLE("minecraft:enchanting_table"),
    BEEHIVE("minecraft:beehive"),
    BELL("minecraft:bell"),
    BREWING_STAND("minecraft:brewing_stand"),
    CAULDRON("minecraft:cauldron"),
    ;

    private final @NotNull String namespace;
    private final @NotNull Collection<Tag<?>> tags;

    StaticBlocks(@NotNull String namespace, @NotNull Tag<?>... tags) {
        this.namespace = namespace;
        this.tags = List.of(tags);
    }

    public BlockHandler create() {
        return new StaticBlock(namespace, tags);
    }

    public @NotNull String getNamespace() {
        return namespace;
    }
}
