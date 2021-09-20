package org.narses.narsion.dev.world.narsionworlddata.npcs;

import net.kyori.adventure.text.TextComponent;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.dev.inventory.MerchantInventory;
import org.narses.narsion.item.data.NarsionItems;

import java.util.Map;
import java.util.UUID;

class VeteranMerchant extends MerchantNpc {

    private MerchantInventory.Trade[] trades;

    public VeteranMerchant(@NotNull UUID uuid, @NotNull String id, @NotNull Pos homePosition, @NotNull TextComponent displayName) {
        super(uuid, id, homePosition, displayName);
    }

    @Override
    protected MerchantInventory.Trade[] getTrades(@NotNull NarsionServer server) {
        if (trades == null) {
            trades = new MerchantInventory.Trade[] {
                    new MerchantInventory.Trade(
                            server,
                            Map.of(
                                    NarsionItems.ADVENTURING, 3,
                                    NarsionItems.GOLD, 3
                            ),
                            Map.of(
                                    NarsionItems.TRAINING, 1
                            )
                    ),
                    new MerchantInventory.Trade(
                            server,
                            Map.of(
                                    NarsionItems.STEEL, 3,
                                    NarsionItems.COPPER, 3
                            ),
                            Map.of(
                                    NarsionItems.GOLD, 1
                            )
                    )
            };
        }
        return trades;
    }
}
