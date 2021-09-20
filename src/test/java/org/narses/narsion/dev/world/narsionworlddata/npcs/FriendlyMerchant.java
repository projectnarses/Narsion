package org.narses.narsion.dev.world.narsionworlddata.npcs;

import net.kyori.adventure.text.TextComponent;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.dev.inventory.MerchantInventory;
import org.narses.narsion.item.data.NarsionItems;

import java.util.Map;
import java.util.UUID;

class FriendlyMerchant extends MerchantNpc {

    private MerchantInventory.Trade[] trades;

    public FriendlyMerchant(@NotNull UUID uuid, @NotNull String id, @NotNull Pos homePosition, @NotNull TextComponent displayName) {
        super(uuid, id, homePosition, displayName);
    }

    @Override
    protected MerchantInventory.Trade[] getTrades(@NotNull NarsionServer server) {
        if (trades == null) {
            trades = new MerchantInventory.Trade[] {
                    new MerchantInventory.Trade(
                            server,
                            Map.of(
                                    NarsionItems.COW_LEATHER, 1,
                                    NarsionItems.IRON, 2
                            ),
                            Map.of(
                                    NarsionItems.BEGINNING, 1
                            )
                    ),
                    new MerchantInventory.Trade(
                            server,
                            Map.of(
                                    NarsionItems.IRON, 1,
                                    NarsionItems.COAL, 1
                            ),
                            Map.of(
                                    NarsionItems.COPPER, 1
                            )
                    )
            };
        }
        return trades;
    }
}
