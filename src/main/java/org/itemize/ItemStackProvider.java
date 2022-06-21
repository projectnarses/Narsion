package org.itemize;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minestom.server.item.ItemMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.itemize.data.ItemData;
import org.itemize.data.ItemDataProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemStackProvider {
	protected static final @NotNull Tag<UUID> TAG_ORIGIN = UUIDTag.get("itemize:tag_origin");
	private final ItemDataProvider itemDataProvider;

	public ItemStackProvider(@NotNull ItemDataProvider dataProvider) {
		this.itemDataProvider = dataProvider;
	}

	public @NotNull ItemDataProvider getItemDataProvider() {
		return itemDataProvider;
	}

	public @NotNull ItemStack create(
			@NotNull Supplier<ItemData> itemDataSupplier,
			@NotNull UUID origin,
			@Nullable Consumer<ItemMeta.Builder> metaBuilderConsumer
	) {
		return create(itemDataSupplier.get(), origin, metaBuilderConsumer);
	}

	public @NotNull ItemStack create(
			@NotNull String ID,
			@NotNull UUID origin,
			@Nullable Consumer<ItemMeta.Builder> metaBuilderConsumer
	) {
		ItemData itemData = itemDataProvider.get(ID);

		if (itemData == null) {
			throw new IllegalArgumentException("No ItemData found for ID: " + ID + ". DataProvider: " + itemDataProvider);
		}

		return create(itemData, origin, metaBuilderConsumer);
	}

	public @NotNull ItemStack create(
			@NotNull ItemData itemData,
			@NotNull UUID origin,
			@Nullable Consumer<ItemMeta.Builder> metaBuilderConsumer
	) {
		// Setup item stack builder
		ItemStack.Builder builder = ItemStack.builder(itemData.display());
		builder.meta(metaBuilder -> {
			// Prepare item
			prepare(metaBuilder, itemData, origin);
			if (metaBuilderConsumer != null)
				metaBuilderConsumer.accept(metaBuilder);
		});

		return builder.build();
	}

	protected void prepare(ItemMeta.Builder builder, ItemData itemData, UUID origin) {
		// Display
		builder.displayName(itemData.displayName());
		builder.lore(itemData.lore());

		// ItemData
		itemData.apply(builder);

		// Origin
		builder.set(TAG_ORIGIN, origin);
	}
}
