package org.narses.narsion.dev.player;

import it.unimi.dsi.fastutil.bytes.Byte2LongMap;
import it.unimi.dsi.fastutil.bytes.Byte2LongOpenHashMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Task;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.classes.PlayerClass;
import org.narses.narsion.classes.abilities.Ability;
import org.narses.narsion.player.NarsionPlayer;
import org.narses.narsion.region.Region;
import org.narses.narsion.region.Regioned;
import org.narses.narsion.util.MathUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class DevPlayer extends NarsionPlayer implements Regioned {

    // Abilities
    private final Task abilityRenderTask;
    private byte abilitySlot = 0;
    private final Byte2LongMap lastAbilityActivation = new Byte2LongOpenHashMap();

    public DevPlayer(@NotNull NarsionServer server, @NotNull Player player) {
        super(server, player);

        this.abilityRenderTask = MinecraftServer.getSchedulerManager()
                .buildTask(this::renderAbilityActionBar)
                .repeat(Duration.of(250, ChronoUnit.MILLIS))
                .schedule();
    }

    // Region
    private final Set<Region> regionSet = new HashSet<>();

    @Override
    public @NotNull Set<Region> getRegions() {
        return regionSet;
    }

    @Override
    public @NotNull Point getPosition() {
        return player.getPosition();
    }

    /**
     * Attempts to activate the currently selected ability.
     * @return true if ability activated, false otherwise
     */
    public boolean activateAbility() {
        PlayerClass playerClass = getPlayerClass();
        Ability[] abilities = playerClass.abilities();
        Ability ability = abilities[abilitySlot];

        int cooldownTime = ability.getCooldown();

        if (getAbilityCooldown(abilitySlot) < cooldownTime) {
            return false;
        }

        ability.activate(server, player);
        lastAbilityActivation.put(abilitySlot, System.currentTimeMillis());
        return true;
    }

    /**
     * Gets the time elasped since the last ability slot usage
     * @return the cooldown in ms
     */
    public int getAbilityCooldown(byte slot) {
        if (!lastAbilityActivation.containsKey(slot)) {
            return Integer.MAX_VALUE;
        }

        long lastActivated = lastAbilityActivation.get(slot);
        long currentTime = System.currentTimeMillis();

        return (int) (currentTime - lastActivated);
    }

    public byte getAbilitySlot() {
        return abilitySlot;
    }

    public Ability getSelectedAbility() {
        PlayerClass playerClass = getPlayerClass();
        Ability[] abilities = playerClass.abilities();
        return abilities[abilitySlot];
    }

    public byte setAbilitySlot(byte newSlot) {
        // Exit if slot unchanged
        if (newSlot == abilitySlot) {
            return abilitySlot;
        }

        PlayerClass playerClass = getPlayerClass();
        Ability[] abilities = playerClass.abilities();

        abilitySlot = (byte) (newSlot % abilities.length);

        while (abilitySlot < 0) {
            abilitySlot += abilities.length;
        }

        renderAbilityActionBar();
        return abilitySlot;
    }

    private void renderAbilityActionBar() {
        if (!player.isOnline()) {
            abilityRenderTask.cancel();
            return;
        }

        // Send slot update to client
        PlayerClass playerClass = getPlayerClass();
        Ability[] abilities = playerClass.abilities();

        TextComponent.Builder builder = Component.text();

        for (int i = 0; i < abilities.length; i++) {
            Ability ability = abilities[i];

            // Find the cooldown
            double abilityCooldown = ability.getCooldown();
            double percentage = getAbilityCooldown((byte) i) / abilityCooldown;
            percentage = Math.min(percentage, 1.0);

            if (i == abilitySlot) {
                int red = MathUtils.lerp(0xFF, 0x00, percentage);
                int green = MathUtils.lerp(0x00, 0xFF, percentage);
                int blue = MathUtils.lerp(0x00, 0x55, percentage);

                builder.append(Component.text(ability.getName()).color(TextColor.color(red, green, blue)));
            } else {
                int red = MathUtils.lerp(0xFF, 0x55, percentage);
                int green = MathUtils.lerp(0x00, 0x55, percentage);
                int blue = MathUtils.lerp(0x00, 0x55, percentage);

                builder.append(Component.text(ability.getSymbol()).color(TextColor.color(red, green, blue)));
            }

            if (i != abilities.length - 1) {
                builder.append(Component.text(" | ").color(NamedTextColor.DARK_GRAY));
            }
        }

        player.sendActionBar(builder.build());
    }
}
