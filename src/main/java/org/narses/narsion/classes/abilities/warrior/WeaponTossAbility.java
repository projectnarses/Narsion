package org.narses.narsion.classes.abilities.warrior;

import net.minestom.server.entity.Player;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.player.NarsionPlayer;

public class WeaponTossAbility {
	
	private static final long slowdownTime = 5 * 20; // Slowdown time in ticks
	private static final int slowdownStrength = 3; // Strength of slow down
	
	public static void activate(NarsionServer server, Player player) {
		/*

		Position entityPosition = player.getPosition().clone().add(0f, -0.2f, 0f);
		ItemStack entityItemStack = player.getInventory().getItemStack(0);
		// Create thrown entity
		final ThrownItemEntity entity = new ThrownItemEntity(entityPosition, player.getInstance(), entityItemStack);

		// Set hit callbacks
		entity.setHitBlock((instance, block) -> {

			entity.setMoving(false);


			ArmorStandMeta meta = (ArmorStandMeta) entity.getEntityMeta();

			meta.setHeadRotation(new Vector((float) ((Math.random() * 45.0) - 22.5), 90, 180));

			entity.getPosition().add(0f, 1.8f, 0f);

			entity.setBoundingBox(new OffsetBoundingBox(entity, 0.1f, 0.1f, 0.1f, 0.0f, 0.7f, 0.0f));

			entity.scheduleRemove(15, TimeUnit.SECOND);
		});

		entity.setHitEntity(hitEntity -> {
			final ItemStack item = entity.getHelmet();

			if (NarsesItemMeta.isValid(item.getMeta()) && (hitEntity instanceof NarsesPlayer)) {
				final String itemID = NarsesItemMeta.from(item.getMeta()).getNarsesID();
				final NarsesItem narsesItem = NarsesServer.getItemDatabase().getItem(itemID);
				final NarsesPlayer somePlayer = ((NarsesPlayer) hitEntity);

				// TODO: Update to new effect/attack system

				// Attack player
				// somePlayer.damage(narsesItem.getAttackDamage(), DamageType.PHYSICAL, player, AttackType.THROWN);

				// Slow player down
				// somePlayer.addEffect(NarsesEffects.SLOWED, slowdownStrength, slowdownTime);
			}

			entity.remove();
		});

		// Set entity instance
		entity.setInstance(player.getInstance());

		// Tell player of hereby thrown entity
		player.sendMessage(Component.text("Tossed!"));
		*/
	}
}
