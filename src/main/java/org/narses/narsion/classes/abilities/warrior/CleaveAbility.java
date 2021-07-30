package org.narses.narsion.classes.abilities.warrior;

import net.minestom.server.entity.Player;
import org.narses.narsion.NarsionServer;

public class CleaveAbility {
	public static final Integer range = 5;

	public static void activate(NarsionServer server, Player player) {

		/*
		// Get position
		final Position position = player.getPosition().clone();

		// Send particle effects
		ParticleEffect.CLEAVE.send(player.getInstance(), position);

		for (final Entity entity : player.getInstance().getEntities()) {
			
			final double distance = position.getDistance(entity.getPosition());
			
			if (entity.getEntityId() != player.getEntityId()) {
				
				if (IsWithinDamageArea(entity, player) && (distance <= range) && (entity instanceof LivingEntity)) {
					
					final LivingEntity lEntity = (LivingEntity) entity;
					
					final float damageAmount = 3f + (3f * (player.getHealth() / player.getPlayerData().getPlayerClass().getHealth()));
					
					lEntity.damage(DamageType.fromPlayer(player), damageAmount);
					
					final Vector damageVector = player.getPosition().getDirection().multiply(6).add(0, 5, 0);
					
					entity.setVelocity(damageVector);
				}
			}
		}

		 */
	}

	/*
	private static boolean IsWithinDamageArea(Entity ent, Entity player) {
		final Vector heading = player.getPosition().getDirection().normalize()
				.subtract(ent.getPosition().getDirection().normalize());
		final double dot = heading.dot(player.getPosition().clone().getDirection());
		if (dot < 1)
			return true;
		return false;
	}

	 */
}
