package org.narses.narsion.item;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import org.jetbrains.annotations.NotNull;

public interface ClickableInventory {
    public void preClickEvent(@NotNull InventoryPreClickEvent event);

    public void clickEvent(@NotNull InventoryClickEvent event);
}
