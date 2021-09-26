package org.narses.narsion.events;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.classes.abilities.Ability;

public record PlayerAbilityEvent(
        @NotNull Player player,
        @NotNull Ability ability
) implements PlayerEvent {

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull Ability getAbility() {
        return ability;
    }
}
