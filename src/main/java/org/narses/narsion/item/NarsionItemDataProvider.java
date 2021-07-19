package org.narses.narsion.item;

import net.minestom.server.item.ItemMetaBuilder;
import net.minestom.server.item.ItemStackBuilder;
import org.itemize.data.ItemData;
import org.itemize.data.ItemDataProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class NarsionItemDataProvider implements ItemDataProvider {
    protected final Map<String, ItemData> itemDataMap;

    protected NarsionItemDataProvider(Map<String, ItemData> itemDataMap) {
        this.itemDataMap = itemDataMap;

    }

    @Override
    public @NotNull Map<String, ItemData> getItemData() {
        return itemDataMap;
    }

    @Override
    public @Nullable ItemData get(@NotNull String ID) {
        return itemDataMap.get(ID);
    }

    public abstract void prepare(@NotNull String ID, @NotNull ItemStackBuilder builder);
}
