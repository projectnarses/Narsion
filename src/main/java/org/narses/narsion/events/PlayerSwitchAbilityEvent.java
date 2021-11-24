package org.narses.narsion.events;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public record PlayerSwitchAbilityEvent(
        @NotNull Player player,
        int abilitySlot,
        boolean scroll
) implements PlayerEvent {

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    public int getAbilitySlot() { return abilitySlot; }

    public boolean isScroll() { return scroll; }
}
