package org.narses.narsion.classes.abilities.warrior;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.Position;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.player.NarsionPlayer;

public class LacerateAbility {
	public static final Integer range = 5;

	public static void activate(NarsionServer server, Player player) {
		/*

		player.sendMessage(Component.text("Lacerate!"));

		final Instance instance = player.getInstance();

		final Position startPosition = player.getPosition().clone().add(0, player.getEyeHeight() / 2.0, 0);

		final Result result = RayCast.rayCastBlocks(instance, startPosition, player.getPosition().getDirection(), 15, 1,
				block -> {
					return instance.getBlock(block.toBlockPosition()).equals(Block.AIR);
				}, null);

		final Position pos = result.getFinalPosition();

		while (instance.getBlock(pos.toBlockPosition()).equals(Block.AIR) && (pos.getY() != 0)) {
			pos.add(0, -1, 0);
		}

		pos.add(0, 1, 0);

		AdvancedParticleEffect.LACERATE.activate(pos, player.getChunk().getViewers());
		*/
	}
}