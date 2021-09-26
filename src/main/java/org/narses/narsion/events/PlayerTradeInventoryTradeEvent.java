package org.narses.narsion.events;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.inventory.TradeInventory;

public record PlayerTradeInventoryTradeEvent(
        @NotNull Player player,
        @NotNull TradeInventory tradeInventory,
        @NotNull TradeInventory.Trade selectedTrade
) implements Event, PlayerEvent, InventoryEvent {
    @Override
    public @NotNull TradeInventory getInventory() {
        return tradeInventory;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }
}
