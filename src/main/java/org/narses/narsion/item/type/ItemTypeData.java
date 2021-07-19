package org.narses.narsion.item.type;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemMetaBuilder;
import net.minestom.server.tag.Tag;
import org.itemize.data.ItemType;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.narses.narsion.classes.Archetype;
import org.narses.narsion.util.TagUtils;

/**
 * Defines all item types and their data
 */
public interface ItemTypeData {
    // Global Tags
    Tag<String> TAG_ITEM_TYPE = Tag.String("ITEM_TYPE");
    Tag<String[][]> TAG_RECIPES = TagUtils.Serializable("RECIPES");
    Tag<Integer> TAG_TIER = Tag.Integer("TIER");

    // TODO: ARMOR
    // TODO: FOOD
    // TODO: POTION

    ///////////////
    // Component //
    ///////////////
    Tag<ComponentType> TAG_COMPONENT_TYPE = TagUtils.Enum("COMPONENT_TYPE", ComponentType.class);

    record COMPONENT(
            ComponentType type,
            int tier,
            String[][] recipes
    ) implements ItemTypeData {
        private static final ItemType ITEM_TYPE = new ItemType(Component.text(COMPONENT.class.getName()), new NBTCompound());

        @Override
        public void apply(ItemMetaBuilder builder) {
            // Global
            builder.set(TAG_TIER, tier);
            builder.set(TAG_RECIPES, recipes);
            builder.set(TAG_ITEM_TYPE, COMPONENT.class.getName());

            // Component
            builder.set(TAG_COMPONENT_TYPE, type);
        }

        @Override
        public ItemType getType() {
            return ITEM_TYPE;
        }
    }

    ////////////
    // Weapon //
    ////////////
    Tag<Archetype> TAG_ARCHETYPE = TagUtils.Enum("ARCHETYPE", Archetype.class);
    Tag<AttackType> TAG_ATTACK_TYPE = TagUtils.Enum("ATTACK_TYPE", AttackType.class);
    Tag<DamageType> TAG_DAMAGE_TYPE = TagUtils.Enum("DAMAGE_TYPE", DamageType.class);
    Tag<Double> TAG_ATTACK_DAMAGE = Tag.Double("ATTACK_DAMAGE");
    Tag<Double> TAG_USE_SPEED = Tag.Double("USE_SPEED");

    record WEAPON(
            Archetype archetype,
            AttackType attackType,
            DamageType damageType,
            double attackDamage,
            double useSpeed,
            int tier,
            String[][] recipes
    ) implements ItemTypeData {
        private static final ItemType ITEM_TYPE = new ItemType(Component.text(WEAPON.class.getName()), new NBTCompound());

        @Override
        public void apply(ItemMetaBuilder builder) {
            // Global
            builder.set(TAG_TIER, tier);
            builder.set(TAG_RECIPES, recipes);
            builder.set(TAG_ITEM_TYPE, WEAPON.class.getName());

            // Weapon
            builder.set(TAG_ARCHETYPE, archetype);
            builder.set(TAG_ATTACK_TYPE, attackType);
            builder.set(TAG_DAMAGE_TYPE, damageType);
            builder.set(TAG_ATTACK_DAMAGE, attackDamage);
            builder.set(TAG_USE_SPEED, useSpeed);
        }

        @Override
        public ItemType getType() {
            return ITEM_TYPE;
        }
    }

    void apply(ItemMetaBuilder builder);

    ItemType getType();
}