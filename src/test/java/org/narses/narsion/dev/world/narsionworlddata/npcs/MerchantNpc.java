package org.narses.narsion.dev.world.narsionworlddata.npcs;

import net.kyori.adventure.text.TextComponent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.dev.inventory.MerchantInventory;
import org.narses.narsion.item.data.NarsionItems;
import org.narses.narsion.npc.NonPlayableCharacter;

import java.util.Map;
import java.util.UUID;

abstract class MerchantNpc extends NonPlayableCharacter {

    public MerchantNpc(@NotNull UUID uuid, @NotNull String id, @NotNull Pos homePosition, @NotNull TextComponent displayName) {
        super(uuid, id, homePosition, displayName);
    }

    @Override
    public void onInteract(@NotNull NarsionServer server, PlayerEntityInteractEvent event) {
        event.getPlayer().openInventory(new MerchantInventory(server, getTrades(server)));
    }

    protected abstract MerchantInventory.Trade[] getTrades(@NotNull NarsionServer server);
}
