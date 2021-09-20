package org.narses.narsion.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import org.itemize.data.ItemData;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.item.NarsionItemStackProvider;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class InventoryUtils {

    /**
     * Returns true if all the specified items are found in the inventory. This takes item amounts into account.
     * @return true if all the item amounts are in the inventory, false otherwise.
     */
    public static boolean containsItems(@NotNull AbstractInventory inventory, @NotNull Collection<ItemStack> items) {
        Object2IntMap<String> inventoryItemAmounts = countItems(inventory.getItemStacks());
        Object2IntMap<String> itemAmounts = countItems(items);


        for (ItemStack item : items) {
            final String itemID = item.getTag(ItemData.TAG_ID);

            if (itemID == null) {
                continue;
            }

            inventoryItemAmounts.putIfAbsent(itemID, 0);
            itemAmounts.putIfAbsent(itemID, 0);
            if (inventoryItemAmounts.getInt(itemID) < itemAmounts.getInt(itemID)) {
                return false;
            }
        }

        return true;
    }

    public static boolean removeItems(@NotNull AbstractInventory inventory, @NotNull Collection<ItemStack> itemsToRemove) {
        boolean complete = true;

        for (ItemStack item : itemsToRemove) {
            int amountLeft = item.getAmount();
            ItemStack[] items = inventory.getItemStacks();

            // For each item in inventory
            for (int i = 0; i < items.length; i++) {
                if (amountLeft == 0) {
                    continue;
                }

                ItemStack itemStack = items[i];

                // If item has same id
                if (!InventoryUtils.isSimilar(item, itemStack)) {
                    continue;
                }

                // If item has enough
                int amount = itemStack.getAmount();

                // Remove all
                if (amount > amountLeft) {
                    inventory.setItemStack(i, itemStack.withAmount(amount - amountLeft));
                    amountLeft = 0;
                    continue;
                }

                // Else if item has exact amount, remove item.
                if (amount == amountLeft) {
                    inventory.setItemStack(i, ItemStack.AIR);
                    amountLeft = 0;
                    continue;
                }

                // Item has lower amount, remove as much as possible.
                amountLeft -= amount;
                inventory.setItemStack(i, ItemStack.AIR);
            }

            if (amountLeft != 0) {
                complete = false;
            }
        }

        return complete;
    }

    public static @NotNull Object2IntMap<String> countItems(@NotNull Collection<ItemStack> items) {
        Object2IntMap<String> itemAmounts = new Object2IntOpenHashMap<>();
        for (ItemStack item : items) {
            final String itemID = item.getTag(ItemData.TAG_ID);

            if (itemID == null) {
                continue;
            }

            itemAmounts.putIfAbsent(itemID, 0);
            itemAmounts.computeInt(itemID, (id, value) -> value += item.getAmount());
        }
        return itemAmounts;
    }

    public static @NotNull Object2IntMap<String> countItems(@NotNull ItemStack... items) {
        Object2IntMap<String> itemAmounts = new Object2IntOpenHashMap<>();
        for (ItemStack item : items) {
            final String itemID = item.getTag(ItemData.TAG_ID);

            if (itemID == null) {
                continue;
            }

            itemAmounts.putIfAbsent(itemID, 0);
            itemAmounts.computeInt(itemID, (id, value) -> value += item.getAmount());
        }
        return itemAmounts;
    }

    /**
     * Returns true if both items have the same id
     */
    public static boolean isSimilar(@NotNull ItemStack itemA, @NotNull ItemStack itemB) {
        return Objects.equals(itemA.getTag(ItemData.TAG_ID), itemB.getTag(ItemData.TAG_ID));
    }
}
