package org.narses.narsion.item.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.itemize.data.ItemData;
import org.itemize.data.ItemRarity;
import org.itemize.data.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.narses.narsion.classes.Archetype;
import org.narses.narsion.item.NarsionItemDataProvider;
import org.narses.narsion.item.data.type.AttackType;
import org.narses.narsion.item.data.type.ComponentType;
import org.narses.narsion.item.data.type.DamageType;
import org.narses.narsion.item.data.type.ItemTypeData;
import org.narses.narsion.util.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * How to add a new item:
 * Add an entry to {@link ItemsEnum}, and it's id to the ease of use item ids
 */
public class NarsionItems extends NarsionItemDataProvider {

    public NarsionItems() {
        super(
            Arrays.stream(ItemsEnum.values())
                .collect(
                    Collectors.toMap(
                        Enum::name,
                        ItemsEnum::get
                    )
                )
        );

    }

    @Override
    public void prepare(@NotNull ItemData data, @NotNull ItemStack.Builder builder) {
        ItemsEnum.valueOf(data.ID()).apply(builder);
    }

    // An item rarity only used during development
    // TODO: Remove this item rarity
    private static final ItemRarity developmentRarity = new ItemRarity(Component.text("Development"), new NBTCompound());

    // Item ids for ease of use.
    public static final String ADVENTURING = "ADVENTURING";
    public static final String BEGINNING = "BEGINNING";
    public static final String CONQUERING = "CONQUERING";
    public static final String QUESTING = "QUESTING";
    public static final String SHIELD_DOT_EXE = "SHIELD_DOT_EXE";
    public static final String THE_END = "THE_END";
    public static final String THE_HOLY_BIBLE = "THE_HOLY_BIBLE";
    public static final String TRAINING = "TRAINING";
    public static final String COAL = "COAL";
    public static final String COPPER = "COPPER";
    public static final String COW_LEATHER = "COW_LEATHER";
    public static final String GOLD = "GOLD";
    public static final String GOLDEN_CLOTH = "GOLDEN_CLOTH";
    public static final String IRON = "IRON";
    public static final String NETHERITE = "NETHERITE";
    public static final String STEEL = "STEEL";

    private enum ItemsEnum implements Supplier<ItemData>, UnaryOperator<ItemStack.Builder> {
        ADVENTURING(
                Material.STONE_SWORD,
                Component.text("Adventuring"),
                0,
                developmentRarity,
                new String[] {
                        "Adventures themselves can be the best of friends"
                },
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
                0,
                developmentRarity,
                new String[] {
                        "Every adventure has a beginning, will this be yours?"
                },
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
                0,
                developmentRarity,
                new String[] {
                        "Every great conqueror used a weapon such as this to assert themselves on the plane of Narsia"
                },
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
                0,
                developmentRarity,
                new String[] {
                        "Quests are a necessary step in the rise to greatness"
                },
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
                0,
                developmentRarity,
                new String[] {
                        "A shield. Conqueror of arrows, slayer of ... aggressive players?"
                },
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
                Component.text("The End.", NamedTextColor.BLACK),
                0,
                developmentRarity,
                new String[] {
                        "No lore is necessary for this blade."
                },
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
                0,
                developmentRarity,
                new String[] {
                        "It's just a book, nothing to see here officer."
                },
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
                0,
                developmentRarity,
                new String[] {
                        "Train until your hand fades red and your legs follow."
                },
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
                0,
                developmentRarity,
                new String[] {
                        "Coal is a resource that is commonly used for the production of steel"
                },
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
                0,
                developmentRarity,
                new String[] {
                        "Copper is a low strength material commonly used for magical and technological items"
                },
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
                0,
                developmentRarity,
                new String[] {
                        "Cow leather is a simple and classic material used for many types of applications"
                },
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
                0,
                developmentRarity,
                new String[] {
                        "Gold is a symbol of wealth and prosperity, only the greatest warriors will have access to this resource"
                },
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
                0,
                developmentRarity,
                new String[] {
                        "What is better then gold and leather? Both at once."
                },
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
                0,
                developmentRarity,
                new String[] {
                        "Iron. Solid. Dependable. It gets the job done."
                },
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
                0,
                developmentRarity,
                new String[] {
                        "Netherite is a rare material said to be infused with the strength of the gods"
                },
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
                0,
                developmentRarity,
                new String[] {
                        "Steel will get the job done and then some, yet it requires a high level blacksmith to produce."
                },
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

        ItemsEnum(
                @NotNull Material display,
                Component displayName,
                int cmd,
                @NotNull ItemRarity rarity,
                @NotNull String[] lore,
                @NotNull ItemTypeData itemTypeData
        ) {
            final ItemType type = itemTypeData.getType();
            this.itemTypeData = itemTypeData;

            // Create lore
            final Style loreStyle = Style.style()
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
                    .build();

            final List<Component> preparedLore = TextUtils.prepareLore(loreStyle, lore);
            final List<Component> componentList = new ArrayList<>(preparedLore);

            // Add type + rarity to lore
            componentList.add(
                    Component.text()
                            .append(Component.text("Type: "))
                            .append(type.displayName())
                            .build()
            );

            componentList.add(
                    Component.text()
                            .append(Component.text("Rarity: "))
                            .append(rarity.displayName())
                            .build()
            );

            this.itemData = new ItemData(
                    name(),
                    displayName,
                    display,
                    type,
                    rarity,
                    componentList.toArray(Component[]::new),
                    cmd
            );
        }

        @Override
        public @NotNull ItemData get() {
            return itemData;
        }

        @Override
        public ItemStack.Builder apply(ItemStack.Builder builder) {
            itemTypeData.apply(builder);
            return builder;
        }
    }
}
