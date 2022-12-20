package org.narses.narsion.dev.world.narsionworlddata.npcs;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.inventory.TradeInventory;
import org.narses.narsion.item.data.NarsionItems;

import java.util.Map;
import java.util.UUID;

class ShadyMerchant extends MerchantNpc {

    private final TradeInventory.Trade[] trades = {
            new TradeInventory.Trade(
                    Map.of(
                            NarsionItems.BEGINNING, 3,
                            NarsionItems.STEEL, 3
                    ),
                    Map.of(
                            NarsionItems.ADVENTURING, 1
                    )
            ),
            new TradeInventory.Trade(
                    Map.of(
                            NarsionItems.COAL, 3,
                            NarsionItems.IRON, 3
                    ),
                    Map.of(
                            NarsionItems.STEEL, 1
                    )
            )
    };

    private final Component displayName;

    public ShadyMerchant(@NotNull UUID uuid, @NotNull String id, @NotNull Pos homePosition, @NotNull TextComponent displayName) {
        super(uuid, id, homePosition, displayName);
        this.displayName = displayName;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return displayName;
    }

    @Override
    protected TradeInventory.Trade[] getTrades(@NotNull NarsionServer server) {
        return trades;
    }
}
