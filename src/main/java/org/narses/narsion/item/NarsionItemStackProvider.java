package org.narses.narsion.item;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.ItemStackBuilder;
import org.itemize.ItemStackProvider;
import org.itemize.data.ItemData;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class NarsionItemStackProvider extends ItemStackProvider {

    private final NarsionItemDataProvider narsionItemDataProvider;

    public NarsionItemStackProvider(@NotNull NarsionItemDataProvider narsionItemDataProvider) {
        super(narsionItemDataProvider);
        this.narsionItemDataProvider = narsionItemDataProvider;
    }

    @Override
    protected void prepare(ItemStackBuilder builder, ItemData itemData, UUID origin) {
        super.prepare(builder, itemData, origin);
        narsionItemDataProvider.prepare(itemData, builder);
    }
}
