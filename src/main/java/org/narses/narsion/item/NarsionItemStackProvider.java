package org.narses.narsion.item;

import net.minestom.server.item.ItemMeta;
import net.minestom.server.item.ItemStack;
import org.itemize.ItemStackProvider;
import org.itemize.data.ItemData;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class NarsionItemStackProvider extends ItemStackProvider {

    private final NarsionItemDataProvider narsionItemDataProvider;

    public NarsionItemStackProvider(@NotNull NarsionItemDataProvider narsionItemDataProvider) {
        super(narsionItemDataProvider);
        this.narsionItemDataProvider = narsionItemDataProvider;
    }

    @Override
    // TODO: Figure out if there is a way we can make origins work with clientside predictions
    protected void prepare(ItemMeta.Builder builder, ItemData itemData, UUID ignored) {
        super.prepare(builder, itemData, null);
        narsionItemDataProvider.prepare(itemData, builder);
    }
}
