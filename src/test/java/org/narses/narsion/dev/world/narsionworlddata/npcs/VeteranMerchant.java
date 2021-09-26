package org.narses.narsion.dev.world.narsionworlddata.npcs;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.inventory.TradeInventory;
import org.narses.narsion.item.data.NarsionItems;

import java.util.Map;
import java.util.UUID;

class VeteranMerchant extends MerchantNpc {

    private final TradeInventory.Trade[] trades = {
            new TradeInventory.Trade(
                    new Object2IntOpenHashMap<>(Map.of(
                            NarsionItems.ADVENTURING, 3,
                            NarsionItems.GOLD, 3
                    )),
                    new Object2IntOpenHashMap<>(Map.of(
                            NarsionItems.TRAINING, 1
                    ))
            ),
            new TradeInventory.Trade(
                    new Object2IntOpenHashMap<>(Map.of(
                            NarsionItems.STEEL, 3,
                            NarsionItems.COPPER, 3
                    )),
                    new Object2IntOpenHashMap<>(Map.of(
                            NarsionItems.GOLD, 1
                    ))
            )
    };

    public VeteranMerchant(@NotNull UUID uuid, @NotNull String id, @NotNull Pos homePosition, @NotNull TextComponent displayName) {
        super(uuid, id, homePosition, displayName);
    }

    @Override
    protected TradeInventory.Trade[] getTrades(@NotNull NarsionServer server) {
        return trades;
    }
}
