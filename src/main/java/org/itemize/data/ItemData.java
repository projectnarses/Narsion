package org.itemize.data;

import net.minestom.server.item.ItemMeta;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import net.kyori.adventure.text.Component;

public record ItemData(
		String ID,
		Component displayName,
		Material display,
		ItemType type,
		ItemRarity rarity,
		Component[] lore,
		int cmd
) {
	public static final Tag<String> TAG_ID = Tag.String("ItemID");

	/**
	 * Applies this ItemData to the ItemMetaBuilder
	 * @param builder
	 */
	public void apply(ItemMeta.Builder builder) {
		builder.displayName(displayName);
		builder.customModelData(cmd);
		builder.lore(lore);

		// Tags
		builder.set(TAG_ID, ID);
	}
}
