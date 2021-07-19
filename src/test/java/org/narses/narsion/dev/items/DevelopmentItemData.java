package org.narses.narsion.dev.items;

import com.google.common.collect.ImmutableMap;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.item.Material;
import org.itemize.data.ItemData;
import org.itemize.data.ItemDataProvider;
import org.itemize.data.ItemRarity;
import org.itemize.data.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DevelopmentItemData implements ItemDataProvider {

    private final Map<String, ItemData> itemDataMap =
        Collections.unmodifiableMap(
            Arrays.stream(DevelopmentItems.values())
                .collect(
                    Collectors.toMap(
                        Enum::name,
                        DevelopmentItems::get
                    )
                )
        );

    public DevelopmentItemData() {}

    @Override
    public @NotNull Map<String, ItemData> getItemData() {
        return itemDataMap;
    }

    @Override
    public @Nullable ItemData get(String ID) {
        return itemDataMap.get(ID);
    }

    private enum DevelopmentItems implements Supplier<ItemData> {
        TEST_ITEM(
                Component.text("This is TEST ITEM!!!"),
                Material.AXOLOTL_SPAWN_EGG,
                new ItemType(Component.text("SUPER_AXE"), new NBTCompound()),
                new ItemRarity(Component.text("MEGA_RARE"), new NBTCompound()),
                new Component[] {Component.text("Lore")},
                0,
                new NBTCompound()
        );

        private final ItemData itemData;

        DevelopmentItems(
                Component displayName,
                Material display,
                ItemType type,
                ItemRarity rarity,
                Component[] lore,
                int cmd,
                NBTCompound data
        ) {
            this.itemData = new ItemData(name(), displayName, display, type, rarity, lore, cmd, data);
        }

        @Override
        public @NotNull ItemData get() {
            return itemData;
        }
    }
}
