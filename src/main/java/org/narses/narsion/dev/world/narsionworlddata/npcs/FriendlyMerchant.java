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

class FriendlyMerchant extends MerchantNpc {

    private final TradeInventory.Trade[] trades = {
            new TradeInventory.Trade(
                    new Object2IntOpenHashMap<>(Map.of(
                            NarsionItems.COW_LEATHER, 1,
                            NarsionItems.IRON, 2
                    )),
                    new Object2IntOpenHashMap<>(Map.of(
                            NarsionItems.BEGINNING, 1
                    ))
            ),
            new TradeInventory.Trade(
                    new Object2IntOpenHashMap<>(Map.of(
                            NarsionItems.IRON, 1,
                            NarsionItems.COAL, 1
                    )),
                    new Object2IntOpenHashMap<>(Map.of(
                            NarsionItems.COPPER, 1
                    ))
            )
    };

    private final Component displayName;

    public FriendlyMerchant(@NotNull UUID uuid, @NotNull String id, @NotNull Pos homePosition, @NotNull TextComponent displayName) {
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
