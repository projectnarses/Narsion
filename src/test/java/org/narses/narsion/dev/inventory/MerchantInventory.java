package org.narses.narsion.dev.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.TransactionOption;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.ItemStackBuilder;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.BundleMeta;
import net.minestom.server.network.packet.server.play.TradeListPacket;
import org.itemize.data.ItemData;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.item.ClickableInventory;
import org.narses.narsion.util.InventoryUtils;

import java.util.*;

public class MerchantInventory extends Inventory implements ClickableInventory {

    private static final @NotNull TextComponent INGREDIENTS_DISPLAY_NAME = Component.text("Ingredients");
    private static final @NotNull TextComponent RESULT_DISPLAY_NAME = Component.text("Result");
    private static final int SELECTED_INGREDIENT = 0;
    private static final int BLANK_SLOT = 1;
    private static final int SELECTED_RESULT = 2;

    private final @NotNull NarsionServer server;
    private final TradeListPacket TRADE_LIST_PACKET;
    private final Trade[] trades;
    private Trade selectedTrade;

    public MerchantInventory(@NotNull NarsionServer server, @NotNull Trade... trades) {
        super(InventoryType.MERCHANT, Component.text("test"));
        this.server = server;
        this.trades = trades;

        TradeListPacket packet = new TradeListPacket();

        packet.windowId = this.getWindowId();
        packet.canRestock = true;
        packet.experience = 0;
        packet.regularVillager = true;
        packet.villagerLevel = 1;

        List<TradeListPacket.Trade> convertedTrades = new ArrayList<>();

        for (Trade trade : trades) {
            TradeListPacket.Trade convertedTrade = new TradeListPacket.Trade();

            convertedTrade.inputItem1 = generateBundle(trade.ingredients.values()).withDisplayName(INGREDIENTS_DISPLAY_NAME);
            convertedTrade.result = generateBundle(trade.result.values()).withDisplayName(RESULT_DISPLAY_NAME);
            convertedTrade.tradeDisabled = false;
            convertedTrade.exp = 0;
            convertedTrade.maxTradeUsesNumber = Integer.MAX_VALUE;
            convertedTrade.priceMultiplier = 0.0F;
            convertedTrade.specialPrice = 0;
            convertedTrade.demand = 0;

            convertedTrades.add(convertedTrade);
        }

        packet.trades = convertedTrades.toArray(TradeListPacket.Trade[]::new);

        this.TRADE_LIST_PACKET = packet;
    }

    @Override
    public boolean leftClick(@NotNull Player player, int slot) {
        return super.leftClick(player, slot);
    }

    private @NotNull ItemStack generateBundle(@NotNull Collection<ItemStack> items) {
        ItemStackBuilder builder = ItemStack.builder(Material.BUNDLE);

        final List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Items: ").color(NamedTextColor.AQUA));

        for (ItemStack item : items) {
            builder.meta(BundleMeta.class, meta -> meta.addItem(item));

            lore.add(Component.text("    " + item.getAmount() + "x ").append(item.getDisplayName()));
        }

        builder.lore(lore.toArray(Component[]::new));

        return builder.build();
    }

    @Override
    public boolean addViewer(@NotNull Player player) {
        player.getPlayerConnection().sendPacket(TRADE_LIST_PACKET);
        return super.addViewer(player);
    }

    public void handleSlotSelect(@NotNull Player player, int selectedSlot) {
        if (selectedSlot >= trades.length) {
            return;
        }

        Trade trade = trades[selectedSlot];

        this.selectedTrade = trade;

        this.setItemStack(
                SELECTED_INGREDIENT,
                generateBundle(trade.ingredients.values())
                        .withDisplayName(INGREDIENTS_DISPLAY_NAME)
        );
        this.setItemStack(
                SELECTED_RESULT,
                generateBundle(trade.result.values())
                        .withDisplayName(RESULT_DISPLAY_NAME)
        );
        // this.setItemStack(CONFIRMATION_BUTTON, trade.recipeDisplay);
    }

    @Override
    public void preClickEvent(@NotNull InventoryPreClickEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        switch (event.getSlot()) {
            case SELECTED_INGREDIENT, BLANK_SLOT -> event.setCancelled(true);
            case SELECTED_RESULT -> {
                event.setCancelled(true);

                if (selectedTrade == null) {
                    return;
                }

                if (event.getClickType() != ClickType.LEFT_CLICK) {
                    return;
                }

                if (!InventoryUtils.containsItems(inventory, selectedTrade.ingredients.values())) {
                    player.sendMessage("You do not have the materials.");
                    return;
                }

                player.sendMessage("You have the materials!");

                // Remove items from inventory
                InventoryUtils.removeItems(inventory, selectedTrade.ingredients.values());

                // Give result
                // Check if result can be added to inventory
                for (ItemStack item : selectedTrade.result.values()) {
                    if (inventory.addItemStack(item, TransactionOption.DRY_RUN)) {
                        inventory.addItemStack(item);
                    } else {
                        // TODO: Figure out what to do when the result does not fit.
                        player.sendMessage(
                                Component.text("The item: ")
                                        .append(Objects.requireNonNullElse(item.getDisplayName(), Component.text(item.getMaterial().name())))
                                        .append(Component.text(" could not be added to your inventory."))
                        );
                    }
                }
            }
        }
    }

    @Override
    public void clickEvent(@NotNull InventoryClickEvent event) {
    }

    public static class Trade {

        private final @NotNull Map<String, ItemStack> ingredients;
        private final @NotNull Map<String, ItemStack> result;

        public Trade(
                @NotNull NarsionServer server,
                @NotNull Map<String, Integer> ingredients,
                @NotNull Map<String, Integer> result
        ) {
            this.ingredients = new HashMap<>();
            this.result = new HashMap<>();

            ingredients.forEach((id, amount) -> {
                ItemData itemData = server.getItemDataProvider().get(id);

                if (itemData == null) {
                    return;
                }

                this.ingredients.put(itemData.ID(), server.getItemStackProvider().create(
                        itemData,
                        new UUID(0, 0), // TODO: Proper item origin
                        null
                ).withAmount(amount));
            });

            result.forEach((id, amount) -> {
                ItemData itemData = server.getItemDataProvider().get(id);

                if (itemData == null) {
                    return;
                }

                this.result.put(itemData.ID(), server.getItemStackProvider().create(
                        itemData,
                        new UUID(0, 0), // TODO: Proper item origin
                        null
                ));
            });
        }
    }
}
