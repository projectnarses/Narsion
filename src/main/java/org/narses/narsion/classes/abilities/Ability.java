package org.narses.narsion.classes.abilities;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.classes.abilities.archer.ChargedShotAbility;
import org.narses.narsion.classes.abilities.archer.DisengageAbility;
import org.narses.narsion.classes.abilities.archer.FocusedShotAbility;
import org.narses.narsion.classes.abilities.archer.VolleyAbility;
import org.narses.narsion.classes.abilities.barbarian.AxeThrowAbility;
import org.narses.narsion.classes.abilities.barbarian.FatalStrikeAbility;
import org.narses.narsion.classes.abilities.barbarian.HitAndRunAbility;
import org.narses.narsion.classes.abilities.barbarian.WarCryAbility;
import org.narses.narsion.classes.abilities.cleric.DeliveranceAbility;
import org.narses.narsion.classes.abilities.cleric.HelpingHandAbility;
import org.narses.narsion.classes.abilities.cleric.InvigorateAbility;
import org.narses.narsion.classes.abilities.cleric.SmokescreenAbility;
import org.narses.narsion.classes.abilities.development.DebugAbility;
import org.narses.narsion.classes.abilities.juggernaut.BarrierAbility;
import org.narses.narsion.classes.abilities.juggernaut.DecimationAbility;
import org.narses.narsion.classes.abilities.juggernaut.FortifyAbility;
import org.narses.narsion.classes.abilities.juggernaut.TauntAbility;
import org.narses.narsion.classes.abilities.mage.BlackHoleAbility;
import org.narses.narsion.classes.abilities.mage.FireblastAbility;
import org.narses.narsion.classes.abilities.mage.HeadwindAbility;
import org.narses.narsion.classes.abilities.mage.StarfallAbility;
import org.narses.narsion.classes.abilities.outrider.ChargeAbility;
import org.narses.narsion.classes.abilities.outrider.CometAbility;
import org.narses.narsion.classes.abilities.outrider.KnockoutBlowAbility;
import org.narses.narsion.classes.abilities.outrider.LastHurrahAbility;
import org.narses.narsion.classes.abilities.paladin.RelieveAbility;
import org.narses.narsion.classes.abilities.paladin.StalwartPlusAbility;
import org.narses.narsion.classes.abilities.ranger.BladeStormAbility;
import org.narses.narsion.classes.abilities.ranger.FeintAbility;
import org.narses.narsion.classes.abilities.ranger.HiddenShotAbility;
import org.narses.narsion.classes.abilities.ranger.LinearAbility;
import org.narses.narsion.classes.abilities.warrior.CleaveAbility;
import org.narses.narsion.classes.abilities.warrior.ExposeTheWeakAbility;
import org.narses.narsion.classes.abilities.warrior.LacerateAbility;
import org.narses.narsion.classes.abilities.warrior.WeaponTossAbility;

import java.util.Arrays;
import java.util.function.BiConsumer;

public enum Ability {
    // Abilities
    // To register a new ability, use this syntax:
    // AbilityName(AbilityPlayerClass, Cooldown (in ticks), Callback),

    // Archer
    // Ability 1: Volley - Sends a scatter of arrows in a cone in front of them.
    VOLLEY("ARCHER", 0, VolleyAbility::activate),
    // Ability 2: Charged Shot - Channels for three seconds, sending a single shot that does additional damage; increased if hits the head.
    CHARGED_SHOT("ARCHER", 0, ChargedShotAbility::activate),
    // Ability 3: Focused Shot - Shoots a bolt that causes the struck enemy to take 1.5x damage from allies for the next ten seconds.
    FOCUSED_SHOT("ARCHER", 0, FocusedShotAbility::activate),
    // Ability 4: Disengage - Leaps back a short distance, and fires a bolt at a target that slows them for 5 seconds.
    DISENGAGE("ARCHER", 0, DisengageAbility::activate),
    ARCHER5("ARCHER", 0, null),

    // Barbarian
    // Ability 1: Fatal Strike - Empower your next attack to do 5% bonus attack damage. However, if the enemy attacked is less than 15 HP, execute them instantly and gain a Fatal Strike stack. Everytime you gain a Fatal Strike stack, the cooldown on the ability is refreshed, and you gain 5% bonus attack speed, up to a maximum of 4 stacks or 20% bonus attack speed. Fatal Strike stacks are lost if the player goes 5 seconds without attacking an enemy.
    FATAL_STRIKE("BARBARIAN", 0, FatalStrikeAbility::activate),
    // Ability 2: Axe Throw - Throw your axe. If the ability hits, the target is rooted for 5 seconds
    AXE_THROW("BARBARIAN", 0, AxeThrowAbility::activate),
    // Ability 3: War Cry - Grant yourself a 30% attack speed increase for 5 seconds.
    WAR_CRY("BARBARIAN", 0, WarCryAbility::activate),
    // Ability 4: Hit and Run - Only available if you have 4 Fatality stacks. Gain 70% bonus movement speed for 5 seconds.
    HIT_AND_RUN("BARBARIAN", 0, HitAndRunAbility::activate),
    BARBARIAN5("BARBARIAN", 0, null),

    // Warrior
    // Ability 1: Cleave - Attack hits in a 360 degree, 6 block radius
    CLEAVE("WARRIOR", 20, CleaveAbility::activate),
    // Ability 2: Expose The Weak - TODO: exposes weak, non-friendly players
    EXPOSE_THE_WEAK("WARRIOR", 60, ExposeTheWeakAbility::activate),
    // Ability 3: Stalwart - Stops the next attack from effecting the user
    STALWART("WARRIOR", 30, FortifyAbility::activate),
    // Ability 4: Weapon Toss - Weapon is thrown out to deal damage + slow down enemies
    WEAPON_TOSS("WARRIOR", 60, WeaponTossAbility::activate),
    // Ability 5:
    LACERATE("WARRIOR", 180, LacerateAbility::activate),

    // Cleric
    // Ability 1: Smokescreen - After gathering magic energy which is then dispersed into the atmosphere for 4 seconds. Gain 3 smoke orbs which can be fired off. At each block where a smoke orb lands, a 5x5 square is instantly filled with an 8 block high cloud of black smoke. These clouds last for 10 seconds after landing.
    SMOKESCREEN("CLERIC", 0, SmokescreenAbility::activate),
    // Ability 2: Invigorate - Grant all allies within 7 blocks 35 bonus armor and 15 bonus magic resist for 5 seconds.
    INVIGORATE("CLERIC", 0, InvigorateAbility::activate),
    // Ability 3: Helping Hand - Lose 25% of your current HP and stop any health regeneration for  yourself for 25 seconds. Grant all allies within 7 blocks of you 250% bonus health regeneration for 5 seconds.
    HELPING_HAND("CLERIC", 0, HelpingHandAbility::activate),
    // Ability 4: Deliverance - Instantly heal 35 HP for all allies within your party. Fairly long cooldown.
    DELIVERANCE("CLERIC", 0, DeliveranceAbility::activate),
    CLERIC5("CLERIC", 0, null),

    // Debug
    DEVELOPMENT1("DEVELOPMENT", 0, null),
    DEVELOPMENT2("DEVELOPMENT", 0, null),
    DEVELOPMENT3("DEVELOPMENT", 0, null),
    DEVELOPMENT4("DEVELOPMENT", 0, null),
    DEBUG("DEVELOPMENT", 0, DebugAbility::activate),


    // Juggernaut
    // Ability 1: Fortify - Reduces damage from all sources by half for 5 seconds.
    FORTIFY("JUGGERNAUT", 0, FortifyAbility::activate),
    // Ability 2: Barrier - Creates a field that destroys all incoming projectiles and abilities for 10 seconds.
    BARRIER("JUGGERNAUT", 0, BarrierAbility::activate),
    // Ability 3: Taunt - Redirects half the damage done to an ally back to you for the next thirty seconds.
    TAUNT("JUGGERNAUT", 0, TauntAbility::activate),
    // Ability 4: Decimation - Channel for 7 seconds, slowing all enemies in a 7x7 area around you. If any enemies are still caught in the area following the channel, do a burst of damage to them.
    DECIMATION("JUGGERNAUT", 0, DecimationAbility::activate),
    JUGGERNAUT5("JUGGERNAUT", 0, null),

    // Mage
    // Ability 1: Starfall - After gathering magic energy which is then dispersed into the atmosphere for 4 seconds, designate a block on which the ability will land. A square around the designated block with a size of 5x5 blocks will then be struck by magic energy, dealing 30 bonus magic damage to all players within the square. If Starfall hits at least 5 players, all players within the radius are stunned for 4 seconds.
    STARFALL("MAGE", 0, StarfallAbility::activate),
    // Ability 2: Fireblast - After gathering magic energy for 2 seconds, shoot a fireball which explodes on the block it lands, applying Burn to all players in a square with a size of 5x5 blocks. Players affected by Burn take 30 base magic damage applied over 5 seconds.
    FIREBLAST("MAGE", 0, FireblastAbility::activate),
    // Ability 3: Headwind -  After gathering magic energy which is then dispersed into the atmosphere for 2 seconds, designate a block on which the ability will land. Heavy winds will then appear in a square around the designated block with a size of 5x5 blocks for 5 seconds, slowing all players within the square by 50%.
    HEADWIND("MAGE", 0, HeadwindAbility::activate),
    // Ability 4: Black Hole - After gathering magic energy which is then dispersed into the atmosphere for 4 seconds, designate a block on which the ability will land. All players in a square with a size of 5x5 blocks will then be pulled towards the designated block for 4 seconds.
    BLACK_HOLE("MAGE", 0, BlackHoleAbility::activate),
    MAGE5("MAGE", 0, null),

    // Outrider
    // Ability 1: Charge - Gather strength for 5 seconds, if any damage is taken during this duration, the ability is canceled. Once 5 seconds have passed, Charge can be released, manifesting as an attack covering a short arc which deals 50 bonus attack damage and 15 True Damage.
    CHARGE("OUTRIDER", 0, ChargeAbility::activate),
    // Ability 2: Comet - The player's mount gains 60% bonus movement speed for 5 seconds, if the mount takes any damage during this duration, the ability is canceled.
    COMET("OUTRIDER", 0, CometAbility::activate),
    // Ability 3: Knockout Blow - Gather strength for 7 seconds. Once 7 seconds have passed, Knockout Blow can be released, manifesting as an attack covering a wider, shallow arc which stuns any target for 3 seconds and applies massive knockback to them.
    KNOCKOUT_BLOW("OUTRIDER", 0, KnockoutBlowAbility::activate),
    // Ability 4: Last Hurrah - Only activatable if the player's mount is below 30% HP. The player's mount gains 20% HP instantly, and regenerates a further 25% HP over 5 seconds.
    LAST_HURRAH("OUTRIDER", 0, LastHurrahAbility::activate),
    OUTRIDER5("OUTRIDER", 0, null),

    // Ranger
    // Ability 1: Hidden Shot - Instantly draw and fire an arrow from a crossbow. Dealing 30 bonus attack damage.
    HIDDEN_SHOT("RANGER", 0, HiddenShotAbility::activate),
    // Ability 2: Blade Storm - Instantly draw and throw 5 daggers in consecutive order. Each dagger deals 5 bonus attack damage.
    BLADE_STORM("RANGER", 0, BladeStormAbility::activate),
    // Ability 3: Feint - The player dashes 3 blocks in the direction they're facing and repels all incoming attacks and abilities for 1 second. During this period, the player is unable to attack, but anyone who attacks the player will be stunned for 3 seconds.
    FEINT("RANGER", 0, FeintAbility::activate),
    // Ability 4: Linear - The player dashes 4 blocks in the direction they're facing and performs an empowered attack, hitting all targets in a long, thin arc in front of them, dealing 40 bonus attack damage. If a target is killed using this ability, the player is able to dash 4 blocks in the direction they're facing again.
    LINEAR("RANGER", 0, LinearAbility::activate),
    RANGER5("RANGER", 0, null),

    // Paladin
    // RELIEVE: Players in a 10 block radius get increased health regeneration for
    // 15 seconds
    RELIEVE("PALADIN", 0, RelieveAbility::activate),
    // MAGIC_GUARD: Protect yourself, fully absorbing the damage of the next attack.
    MAGIC_GUARD("PALADIN", 0, FortifyAbility::activate),
    // STALWART+: Fortify others in a 10 block radius, absorbing all damage they
    // typically would take. Lasts 30 seconds.
    STALWART_PLUS("PALADIN", 0, StalwartPlusAbility::activate),
    PALADIN4("PALADIN", 0, null),
    PALADIN5("PALADIN", 0, null);


    public static Ability[] getAbilities(String className) {
        return Arrays.stream(values())
                .filter((ability) -> className.equals(ability.playerClassName))
                .toArray(Ability[]::new);
    }

    private final BiConsumer<NarsionServer, Player> abilityCallback;
    private final int cooldown;
    private final String playerClassName;

    Ability(String playerClassName, int cooldown, BiConsumer<NarsionServer, Player> abilityCallback) {
        this.playerClassName = playerClassName;
        this.cooldown = cooldown;
        this.abilityCallback = abilityCallback;
    }

    /**
     * Activates the specified ability on the player
     *
     * @param server the server to activate in
     * @param player the player to activate on
     */
    public void activate(@NotNull NarsionServer server, @NotNull Player player) {
        if (this.abilityCallback != null) {
            this.abilityCallback.accept(server, player);
        }
    }

    /**
     * @return the cooldown
     */
    public int getCooldown() {
        return this.cooldown;
    }

    /**
     * @return the playerClass
     */
    public String getPlayerClassName() {
        return this.playerClassName;
    }
}
