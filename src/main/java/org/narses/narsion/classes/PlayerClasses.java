package org.narses.narsion.classes;

import com.moandjiezana.toml.Toml;
import org.narses.narsion.classes.abilities.Ability;
import org.narses.narsion.classes.abilities.PlayerClass;

import static org.narses.narsion.util.TomlUtils.*;

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
    public final PlayerClass ARCHER;

    /*
     * Barbarian
     *
     * The most mobile Fighter Class, but also the least tanky. Primarily does
     * damage
     */
    public final PlayerClass BARBARIAN;

    /*
     * Cleric
     *
     * The Cleric is a dedicated support Class that can shield and mitigate damage,
     * as well as provide some healing. They're mostly intended as a deterrent
     * against archers taking over.
     */
    public final PlayerClass CLERIC;

    /*
     * Development
     *
     * The Development class is a class used for bug testing
     */
    public final PlayerClass DEVELOPMENT;

    /*
     * Juggernaut
     *
     * The tank. Does tank things. Has a shield, can cover teammates, etc.
     */
    public final PlayerClass JUGGERNAUT;

    /*
     * Mage
     *
     * Mages do wide, area of effect spells to punish players who group up densely
     */
    public final PlayerClass MAGE;

    /*
     * Outrider
     *
     * Light Cavalry. Can be on Horseback; high mobility, but not the best at 1v1
     * combat. Great at flanking.
     */
    public final PlayerClass OUTRIDER;

    /*
     * Paladin
     *
     * Buffing, support Class. Tanky-ish, doesn't do a ton of damage. Can mitigate
     * damage on teammates and provide light healing.
     */
    public final PlayerClass PALADIN;

    /*
     * Ranger
     *
     * Hybrid Class of melee/ranged with mobility. Likes to run around and poke
     * targets before going in for the kill, but will die if gap-closed on unless
     * target is damaged.
     */
    public final PlayerClass RANGER;

    /*
     * Warrior
     *
     * Generic, all around fighter. Balanced in giving damage and taking it.
     * Sword/board, very familiar to most Minecraft players.
     */
    public final PlayerClass WARRIOR;

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
}
