package org.narses.narsion.dev.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.TradeListPacket;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class MerchantInventory extends Inventory {

    private final TradeListPacket TRADE_LIST_PACKET;

    public MerchantInventory() {
        super(InventoryType.MERCHANT, Component.text("test"));
        int windowID = this.getWindowId();

        TradeListPacket packet = new TradeListPacket();

        packet.windowId = this.getWindowId();
        packet.canRestock = true;
        packet.experience = 0;
        packet.regularVillager = true;
        packet.villagerLevel = 1;

        List<TradeListPacket.Trade> trades = new ArrayList<>();

        List<Material> materials = new ArrayList<>(Material.values());

        Random random = new Random();

        Supplier<Material> randomMat = () -> materials.get(random.nextInt(materials.size()));

        for (int i = 0; i < 10; i++) {

            TradeListPacket.Trade trade = new TradeListPacket.Trade();

            trade.inputItem1 = ItemStack.of(randomMat.get());
            trade.result = ItemStack.of(randomMat.get());
            trade.inputItem2 = ItemStack.of(randomMat.get());
            trade.demand = 0;
            trade.exp = 23;
            trade.maxTradeUsesNumber = Integer.MAX_VALUE;
            trade.tradeDisabled = false;

            trades.add(trade);
        }

        packet.trades = trades.toArray(TradeListPacket.Trade[]::new);

        this.TRADE_LIST_PACKET = packet;
    }

    @Override
    public boolean addViewer(@NotNull Player player) {
        player.getPlayerConnection().sendPacket(TRADE_LIST_PACKET);
        return super.addViewer(player);
    }
}
