package org.narses.narsion.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
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
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.events.PlayerTradeInventoryTradeEvent;
import org.narses.narsion.item.ClickableInventory;
import org.narses.narsion.util.InventoryUtils;

import java.util.*;
import java.util.function.Function;

public class TradeInventory extends Inventory implements ClickableInventory {

    private static final @NotNull TextComponent INGREDIENTS_DISPLAY_NAME = Component.text("Ingredients");
    private static final @NotNull TextComponent RESULT_DISPLAY_NAME = Component.text("Result");
    private static final int SELECTED_INGREDIENT = 0;
    private static final int BLANK_SLOT = 1;
    private static final int SELECTED_RESULT = 2;

    private final @NotNull NarsionServer server;
    private final TradeListPacket TRADE_LIST_PACKET;
    private final Trade[] trades;
    private Trade selectedTrade;

    public TradeInventory(@NotNull NarsionServer server, @NotNull Component inventoryTitle, @NotNull Trade... trades) {
        super(InventoryType.MERCHANT, inventoryTitle);
        this.server = server;
        this.trades = trades;

        TradeListPacket packet = new TradeListPacket();

        packet.windowId = this.getWindowId();
        packet.canRestock = true;
        packet.experience = 0;
        packet.regularVillager = true;
        packet.villagerLevel = 1;

        packet.trades = Arrays.stream(trades)
                .map((trade) -> trade.apply(server))
                .toArray(TradeListPacket.Trade[]::new);

        this.TRADE_LIST_PACKET = packet;
    }

    @Override
    public boolean leftClick(@NotNull Player player, int slot) {
        return super.leftClick(player, slot);
    }

    private static @NotNull ItemStack generateBundle(@NotNull NarsionServer server, @NotNull Object2IntMap<String> items) {

        // Exit early if only a single item
        if (items.size() == 1) {
            for (Object2IntMap.Entry<String> item : items.object2IntEntrySet()) {
                String itemID = item.getKey();
                int itemAmount = item.getIntValue();
                return server.getItemStackProvider()
                        .create(
                                itemID,
                                server.getOriginProvider().DISPLAY("inventory:merchant"),
                                null
                        )
                        .withAmount(itemAmount);
            }
        }

        // Else create bundle

        ItemStackBuilder builder = ItemStack.builder(Material.BUNDLE);

        final List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Items: ").color(NamedTextColor.AQUA));

        // Generate display items
        for (Object2IntMap.Entry<String> item : items.object2IntEntrySet()) {
            String itemID = item.getKey();
            int itemAmount = item.getIntValue();

            // Generate item
            ItemStack itemStack = server.getItemStackProvider()
                    .create(
                            itemID,
                            server.getOriginProvider().DISPLAY("inventory:merchant"),
                            null
                    )
                    .withAmount(itemAmount);

            builder.meta(BundleMeta.class, meta -> meta.addItem(itemStack));

            lore.add(Component.text("    " + itemAmount + "x ").append(itemStack.getDisplayName()));
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
                generateBundle(server, trade.ingredients)
                        .withDisplayName(INGREDIENTS_DISPLAY_NAME)
        );
        this.setItemStack(
                SELECTED_RESULT,
                generateBundle(server, trade.result)
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

                if (!InventoryUtils.containsItems(inventory, selectedTrade.ingredients)) {
                    player.sendMessage("You do not have the materials.");
                    return;
                }

                player.sendMessage("You have the materials!");

                // Remove items from inventory
                InventoryUtils.removeItems(inventory, selectedTrade.ingredients);

                // Call event
                EventDispatcher.call(new PlayerTradeInventoryTradeEvent(player, this, selectedTrade));

                // Give result
                for (Object2IntMap.Entry<String> item : selectedTrade.result.object2IntEntrySet()) {
                    String itemID = item.getKey();
                    int itemAmount = item.getIntValue();

                    // Generate item
                    ItemStack itemStack = server.getItemStackProvider()
                            .create(
                                    itemID,
                                    server.getOriginProvider().TRADE(
                                            "inventory:merchant",
                                            selectedTrade.ingredients.toString(),
                                            selectedTrade.result.toString()
                                    ),
                                    null
                            )
                            .withAmount(itemAmount);


                    // Check if result can be added to inventory
                    if (inventory.addItemStack(itemStack, TransactionOption.DRY_RUN)) {
                        inventory.addItemStack(itemStack);
                        continue;
                    }

                    // TODO: Figure out what to do when the result does not fit.
                    player.sendMessage(
                            Component.text("The item: ")
                                    .append(Objects.requireNonNullElse(itemStack.getDisplayName(), Component.text(itemStack.getMaterial().name())))
                                    .append(Component.text(" could not be added to your inventory."))
                    );
                }
            }
        }
    }

    @Override
    public void clickEvent(@NotNull InventoryClickEvent event) {
    }

    public record Trade(
            @NotNull Object2IntMap<String> ingredients,
            @NotNull Object2IntMap<String> result
    ) implements Function<NarsionServer, TradeListPacket.Trade> {
        @Override
        public TradeListPacket.Trade apply(@NotNull NarsionServer server) {
            TradeListPacket.Trade trade = new TradeListPacket.Trade();

            trade.inputItem1 = generateBundle(server, ingredients);
            trade.result = generateBundle(server, result);
            trade.tradeDisabled = false;
            trade.exp = 0;
            trade.maxTradeUsesNumber = Integer.MAX_VALUE;
            trade.priceMultiplier = 0.0F;
            trade.specialPrice = 0;
            trade.demand = 0;

            if (trade.inputItem1.getMaterial() == Material.BUNDLE) {
                trade.inputItem1 = trade.inputItem1.withDisplayName(INGREDIENTS_DISPLAY_NAME);
            }
            if (trade.result.getMaterial() == Material.BUNDLE) {
                trade.result = trade.result.withDisplayName(RESULT_DISPLAY_NAME);
            }

            return trade;
        }
    }
}
