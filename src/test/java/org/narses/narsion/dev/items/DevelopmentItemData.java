package org.narses.narsion.dev.items;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemMetaBuilder;
import net.minestom.server.item.ItemStackBuilder;
import net.minestom.server.item.Material;
import org.itemize.data.ItemData;
import org.itemize.data.ItemRarity;
import org.itemize.data.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.narses.narsion.classes.Archetype;
import org.narses.narsion.item.NarsionItemDataProvider;
import org.narses.narsion.item.type.AttackType;
import org.narses.narsion.item.type.ComponentType;
import org.narses.narsion.item.type.DamageType;
import org.narses.narsion.item.type.ItemTypeData;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class DevelopmentItemData extends NarsionItemDataProvider {

    public DevelopmentItemData() {
        super(
            Arrays.stream(DevelopmentItems.values())
                .collect(
                    Collectors.toMap(
                        Enum::name,
                        DevelopmentItems::get
                    )
                )
        );

    }

    @Override
    public void prepare(@NotNull String ID, @NotNull ItemStackBuilder builder) {
        builder.meta(DevelopmentItems.valueOf(ID));
    }

    private enum DevelopmentItems implements Supplier<ItemData>, UnaryOperator<ItemMetaBuilder> {
        TEST_ITEM(
                Material.AXOLOTL_SPAWN_EGG,
                Component.text("Steel"),
                new Component[] {Component.text("Lore")},
                new ItemTypeData.WEAPON(
                        Archetype.DEVELOPMENT,
                        AttackType.THROWN,
                        DamageType.MAGIC,
                        10.0,
                        5.0,
                        2,
                        new String[][] {

                        }
                )
        ),
        ADVENTURING(
                Material.STONE_SWORD,
                Component.text("Adventuring"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.WEAPON(
                        Archetype.FIGHTER,
                        AttackType.MELEE,
                        DamageType.PHYSICAL,
                        10.0,
                        1.0,
                        2,
                        new String[][] {
                                {"BEGINNING", "BEGINNING"}
                        }
                )
        ),
        BEGINNING(
                Material.WOODEN_SWORD,
                Component.text("Beginning"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.WEAPON(
                        Archetype.FIGHTER,
                        AttackType.MELEE,
                        DamageType.PHYSICAL,
                        5.0,
                        1.0,
                        2,
                        new String[][] {

                        }
                )
        ),
        CONQUERING(
                Material.DIAMOND_SWORD,
                Component.text("Conquering"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.WEAPON(
                        Archetype.FIGHTER,
                        AttackType.MELEE,
                        DamageType.PHYSICAL,
                        75.0,
                        1.0,
                        5,
                        new String[][] {
                                { "QUESTING", "QUESTING" }
                        }
                )
        ),
        QUESTING(
                Material.GOLDEN_SWORD,
                Component.text("Questing"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.WEAPON(
                        Archetype.FIGHTER,
                        AttackType.MELEE,
                        DamageType.PHYSICAL,
                        50.0,
                        1.0,
                        4,
                        new String[][] {
                                { "TRAINING", "TRAINING" }
                        }
                )
        ),
        SHIELD_DOT_EXE(
                Material.SHIELD,
                Component.text("Shield.exe"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.WEAPON(
                        Archetype.FIGHTER,
                        AttackType.MELEE,
                        DamageType.PHYSICAL,
                        10.0,
                        1.0,
                        1,
                        new String[][] {

                        }
                )
        ),
        THE_END(
                Material.NETHERITE_SWORD,
                Component.text("The End."),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.WEAPON(
                        Archetype.FIGHTER,
                        AttackType.MELEE,
                        DamageType.PHYSICAL,
                        100.0,
                        1.0,
                        6,
                        new String[][] {
                                { "CONQUERING", "CONQUERING" }
                        }
                )
        ),
        THE_HOLY_BIBLE(
                Material.BOOK,
                Component.text("The Holy Bible"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.WEAPON(
                        Archetype.FIGHTER,
                        AttackType.MELEE,
                        DamageType.PHYSICAL,
                        10.0,
                        1.0,
                        1,
                        new String[][] {

                        }
                )
        ),
        TRAINING(
                Material.IRON_AXE,
                Component.text("Training"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.WEAPON(
                        Archetype.FIGHTER,
                        AttackType.MELEE,
                        DamageType.PHYSICAL,
                        25.0,
                        1.0,
                        3,
                        new String[][] {
                                { "ADVENTURING", "ADVENTURING" }
                        }
                )
        ),
        COAL(
                Material.COAL, // Material
                Component.text("Coal"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.COMPONENT(
                        ComponentType.ORE,
                        1,
                        new String[][] {
                                
                        }
                )
        ),
        COPPER(
                Material.NETHER_BRICK, // Material
                Component.text("Copper"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.COMPONENT(
                        ComponentType.ORE,
                        2,
                        new String[][] {
                                
                        }
                )
        ),
        COW_LEATHER(
                Material.LEATHER, // MaterialType
                Component.text("Cow Leather"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.COMPONENT(
                        ComponentType.LEATHER,
                        2,
                        new String[][] {
                                
                        }
                )
        ),
        GOLD(
                Material.GOLD_INGOT, // Material
                Component.text("Gold"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.COMPONENT(
                        ComponentType.ORE,
                        4,
                        new String[][] {
                                
                        }
                )
        ),
        GOLDEN_CLOTH(
                Material.RABBIT_HIDE, // Material
                Component.text("Golden Cloth"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.COMPONENT(
                        ComponentType.CLOTH,
                        6,
                        new String[][] {
                                
                        }
                )
        ),
        IRON(
                Material.IRON_INGOT, // Material
                Component.text("Iron"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.COMPONENT(
                        ComponentType.ORE,
                        3,
                        new String[][] {
                                
                        }
                )
        ),
        NETHERITE(
                Material.NETHERITE_INGOT, // Material
                Component.text("Netherite"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.COMPONENT(
                        ComponentType.ORE,
                        6,
                        new String[][] {
                                { "GOLD", "STEEL" }
                        }
                )
        ),
        STEEL(
                Material.IRON_BLOCK, // Material
                Component.text("Steel"),
                new Component[] {Component.text("TODO: Lore")},
                new ItemTypeData.COMPONENT(
                        ComponentType.ORE,
                        5,
                        new String[][] {
                                { "COAL", "IRON" }
                        }
                )
        );

        private final ItemData itemData;
        private final ItemTypeData itemTypeData;

        DevelopmentItems(
                @NotNull Material display,
                // @NotNull ItemRarity rarity,
                Component displayName,
                @NotNull Component[] lore,
                // int cmd,
                @NotNull ItemTypeData itemTypeData
        ) {
            final ItemType type = itemTypeData.getType();
            final List<Component> componentList = new ArrayList<>(Arrays.asList(lore));
            this.itemTypeData = itemTypeData;

            // Add type + rarity to lore
            componentList.add(type.displayName());

            this.itemData = new ItemData(
                    name(),
                    displayName,
                    display,
                    type,
                    new ItemRarity(Component.text(""), new NBTCompound()),
                    componentList.toArray(Component[]::new),
                    0
            );
        }

        @Override
        public @NotNull ItemData get() {
            return itemData;
        }

        @Override
        public ItemMetaBuilder apply(ItemMetaBuilder builder) {
            itemTypeData.apply(builder);
            return builder;
        }
    }
}
