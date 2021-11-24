package org.narses.narsion.classes;

import org.narses.narsion.classes.abilities.Ability;

/**
 * This represents a single player class instance
 * <br><br>
 * All class abilities are in {@link Ability}
 */
public record PlayerClass(
        /*
         * Health (HP) Attack Damage (AD) Armor (AR) Magic Damage (MD) Magic Resistance
         * (MR) True Damage (TD) Cool Down Reduction (CDR) Movement Speed (MS)
         */
        String className,
        Archetype archetype,
        Ability[] abilities,
        double armor,
        double attackDamage,
        double health,
        double magicDamage,
        double magicResistance,
        double movementSpeed,
        double trueDamage
) {}