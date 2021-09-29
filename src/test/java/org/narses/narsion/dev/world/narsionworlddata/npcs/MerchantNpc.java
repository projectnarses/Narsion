package org.narses.narsion.dev.world.narsionworlddata.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.inventory.TradeInventory;
import org.narses.narsion.npc.NonPlayableCharacter;

import java.util.UUID;

abstract class MerchantNpc extends NonPlayableCharacter {

    public MerchantNpc(@NotNull UUID uuid, @NotNull String id, @NotNull Pos homePosition, @NotNull TextComponent displayName) {
        super(uuid, id, homePosition, displayName);
    }

    public abstract @NotNull Component getDisplayName();

    @Override
    public void onInteract(@NotNull NarsionServer server, PlayerEntityInteractEvent event) {
        event.getPlayer().openInventory(new TradeInventory(server, this.getDisplayName(), getTrades(server)));
    }

    protected abstract TradeInventory.Trade[] getTrades(@NotNull NarsionServer server);
}
