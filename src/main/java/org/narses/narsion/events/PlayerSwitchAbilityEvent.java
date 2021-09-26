package org.narses.narsion.events;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.classes.abilities.Ability;

public record PlayerSwitchAbilityEvent(
        @NotNull Player player,
        @NotNull int abilitySlot,
        @NotNull boolean scroll
) implements PlayerEvent {

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull int getAbilitySlot() { return abilitySlot; }

    public @NotNull boolean isScroll() { return scroll; }
}
