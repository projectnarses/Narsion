package org.narses.narsion.classes.abilities.warrior;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.narses.entities.thrownitementity.ThrownItemEntity;
import org.narses.narsion.NarsionServer;

import java.util.Objects;

public class WeaponTossAbility {
	
	private static final long slowdownTime = 5 * 20; // Slowdown time in ticks
	private static final int slowdownStrength = 3; // Strength of slow down
	
	public static void activate(NarsionServer server, @NotNull Player player) {
		Pos entityPosition = player.getPosition();
		ItemStack entityItemStack = player.getInventory().getItemInMainHand();


		// Create thrown entity
		final ThrownItemEntity entity = new ThrownItemEntity(entityPosition, entityItemStack, 15);
		entity.setInstance(Objects.requireNonNull(player.getInstance()));

		// Set hit callbacks
		entity.setHitBlock((instance, block) -> {
			entity.setMoving(false);

			ArmorStandMeta meta = (ArmorStandMeta) entity.getEntityMeta();

			meta.setHeadRotation(new Vec(((Math.random() * 45.0) - 22.5), 90, 180));

			entity.teleport(entity.getPosition().add(0f, 1.8f, 0f));

//			entity.setBoundingBox(new OffsetBoundingBox(entity, 0.1f, 0.1f, 0.1f, 0.0f, 0.0f, 0.0f));

			entity.scheduleRemove(15, TimeUnit.SECOND);
		});

		entity.setHitEntity(hitEntity -> {
			if (hitEntity instanceof Player somePlayer)
			if (player != somePlayer) {
				entity.remove();

				// TODO: Attack player
				somePlayer.sendMessage("You got hit");
			}
		});

		// Set entity instance
		entity.setInstance(Objects.requireNonNull(player.getInstance()));

		// Tell player of hereby thrown entity
		player.sendMessage("Tossed!");
	}
}
