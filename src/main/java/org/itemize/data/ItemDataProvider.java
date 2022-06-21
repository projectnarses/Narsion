package org.itemize.data;

import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public interface ItemDataProvider {
	public @NotNull Map<String, ItemData> getItemData();

	public @Nullable ItemData get(@NotNull String ID);

	public default @NotNull ItemData from(@NotNull ItemStack item) {
		String ID = Objects.requireNonNull(
				item.getTag(ItemData.TAG_ID),
				"ItemStack passed to ItemDataProvider#from did not have an ID"
		);

		return Objects.requireNonNull(
				get(ID),
				"ID in ItemStack passed to ItemDataProvider#from does not have a valid itemData"
		);
	};
}
