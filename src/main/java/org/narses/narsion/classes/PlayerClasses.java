package org.narses.narsion.classes;

import com.moandjiezana.toml.Toml;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narses.narsion.classes.abilities.Ability;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.narses.narsion.util.TomlUtils.Keys;
import static org.narses.narsion.util.TomlUtils.TomlKey;

/**
 * This class represents all loaded player classes
 */
public class PlayerClasses {
    // The keys to use to fetch the toml data
    private static final TomlKey<Archetype> KEY_ARCHETYPE = Keys.ENUM("Archetype", Archetype.class);
    private static final TomlKey<Double> KEY_ARMOR = Keys.CAST("Armor");
    private static final TomlKey<Double> KEY_ATTACK_DAMAGE = Keys.CAST("AttackDamage");
    private static final TomlKey<Double> KEY_HEALTH = Keys.CAST("Health");
    private static final TomlKey<Double> KEY_MAGIC_DAMAGE = Keys.CAST("MagicDamage");
    private static final TomlKey<Double> KEY_MAGIC_RESISTANCE = Keys.CAST("MagicResistance");
    private static final TomlKey<Double> KEY_MOVEMENT_SPEED = Keys.CAST("MovementSpeed");
    private static final TomlKey<Double> KEY_TRUE_DAMAGE = Keys.CAST("TrueDamage");

    /*
     * Archer
     *
     * Has a bow/arrow, does damage at a range. The main "ranged" Class. Also has a
     * small dagger to pretend that they can fight in close combat.
     */
    public final @NotNull PlayerClass ARCHER;

    /*
     * Barbarian
     *
     * The most mobile Fighter Class, but also the least tanky. Primarily does
     * damage
     */
    public final @NotNull PlayerClass BARBARIAN;

    /*
     * Cleric
     *
     * The Cleric is a dedicated support Class that can shield and mitigate damage,
     * as well as provide some healing. They're mostly intended as a deterrent
     * against archers taking over.
     */
    public final @NotNull PlayerClass CLERIC;

    /*
     * Development
     *
     * The Development class is a class used for bug testing
     */
    public final @NotNull PlayerClass DEVELOPMENT;

    /*
     * Juggernaut
     *
     * The tank. Does tank things. Has a shield, can cover teammates, etc.
     */
    public final @NotNull PlayerClass JUGGERNAUT;

    /*
     * Mage
     *
     * Mages do wide, area of effect spells to punish players who group up densely
     */
    public final @NotNull PlayerClass MAGE;

    /*
     * Outrider
     *
     * Light Cavalry. Can be on Horseback; high mobility, but not the best at 1v1
     * combat. Great at flanking.
     */
    public final @NotNull PlayerClass OUTRIDER;

    /*
     * Paladin
     *
     * Buffing, support Class. Tanky-ish, doesn't do a ton of damage. Can mitigate
     * damage on teammates and provide light healing.
     */
    public final @NotNull PlayerClass PALADIN;

    /*
     * Ranger
     *
     * Hybrid Class of melee/ranged with mobility. Likes to run around and poke
     * targets before going in for the kill, but will die if gap-closed on unless
     * target is damaged.
     */
    public final @NotNull PlayerClass RANGER;

    /*
     * Warrior
     *
     * Generic, all around fighter. Balanced in giving damage and taking it.
     * Sword/board, very familiar to most Minecraft players.
     */
    public final @NotNull PlayerClass WARRIOR;

    // Cached collections for easy lookup
    private final @NotNull PlayerClass[] values;
    private final @NotNull Map<String, PlayerClass> classesByName = new ConcurrentHashMap<>();

    /**
     * Constructs all loaded player classes from the specified config
     */
    public PlayerClasses(Toml config) {
        this.ARCHER = generateClass("ARCHER", config.getTable("ARCHER"));
        this.BARBARIAN = generateClass("BARBARIAN", config.getTable("BARBARIAN"));
        this.CLERIC = generateClass("CLERIC", config.getTable("CLERIC"));
        this.DEVELOPMENT = generateClass("DEVELOPMENT", config.getTable("DEVELOPMENT"));
        this.JUGGERNAUT = generateClass("JUGGERNAUT", config.getTable("JUGGERNAUT"));
        this.MAGE = generateClass("MAGE", config.getTable("MAGE"));
        this.OUTRIDER = generateClass("OUTRIDER", config.getTable("OUTRIDER"));
        this.PALADIN = generateClass("PALADIN", config.getTable("PALADIN"));
        this.RANGER = generateClass("RANGER", config.getTable("RANGER"));
        this.WARRIOR = generateClass("WARRIOR", config.getTable("WARRIOR"));

        this.values = new PlayerClass[] {
                ARCHER,
                BARBARIAN,
                CLERIC,
                DEVELOPMENT,
                JUGGERNAUT,
                MAGE,
                OUTRIDER,
                PALADIN,
                RANGER,
                WARRIOR
        };

        // Place into map
        for (PlayerClass playerClass : values) {
            classesByName.put(playerClass.className(), playerClass);
        }
    }

    private PlayerClass generateClass(String className, Toml config) {
            return new PlayerClass(
                    className,
                    KEY_ARCHETYPE.get(config),
                    Ability.getAbilities(className),
                    KEY_ARMOR.get(config),
                    KEY_ATTACK_DAMAGE.get(config),
                    KEY_HEALTH.get(config),
                    KEY_MAGIC_DAMAGE.get(config),
                    KEY_MAGIC_RESISTANCE.get(config),
                    KEY_MOVEMENT_SPEED.get(config),
                    KEY_TRUE_DAMAGE.get(config)
            );
    }

    public @Nullable PlayerClass getPlayerClass(@NotNull String name) {
        return classesByName.get(name);
    }

    public @NotNull PlayerClass[] values() {
        return values;
    }
}
