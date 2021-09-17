package org.narses.narsion.events;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.classes.abilities.Ability;

public class PlayerAbilityEvent implements PlayerEvent {

    private final @NotNull Player player;
    private final @NotNull Ability ability;

    public PlayerAbilityEvent(@NotNull Player player, @NotNull Ability ability) {
        this.player = player;
        this.ability = ability;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull Ability getAbility() {
        return ability;
    }
}
