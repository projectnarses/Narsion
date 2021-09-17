package org.narses.narsion.dev.world.npc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.npc.NonPlayableCharacter;
import org.narses.narsion.npc.NonPlayableCharacterSource;

import java.util.UUID;
import java.util.function.Supplier;

public enum NarsionNPCs implements NonPlayableCharacterSource {
    SHADY_MERCHANT(
            Component.text("Shady Merchant"),
            new Pos(2934.54, 73.00, -84.48, -770.89F, 12.15F),
            Component.text("Hey kid, wanna buy a watch?"),
            Component.text("You're new around here. Watch your back or get attack.")
    ),
    FRIENDLY_MERCHANT(Component.text("Friendly Merchant"), new Pos(2933.54, 71.00, -62.51, -461.63F, 8.13F)),
    VETERAN_MERCHANT(Component.text("Veteran Merchant"), new Pos(2930.56, 69.00, -46.53, -496.53F, 1.03F)),
    ;

    final @NotNull UUID uuid;
    final @NotNull TextComponent displayName;
    final @NotNull Pos homePosition;

    NarsionNPCs(
            @NotNull TextComponent displayName,
            @NotNull Pos homePosition,
            @NotNull TextComponent... dialogue
    ) {
        this.displayName = displayName;
        this.homePosition = homePosition;
        this.uuid = UUID.randomUUID();
    }

    public @NotNull NonPlayableCharacter spawn(@NotNull Instance instance) {
        NonPlayableCharacter npc = new NonPlayableCharacter(uuid) {
            @Override
            public @NotNull String id() {
                return name();
            }

            @Override
            public @NotNull Pos homePosition() {
                return homePosition;
            }
        };

        npc.setCustomName(displayName);

        npc.setInstance(instance, npc.homePosition());
        return npc;
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }

    @Override
    public @NotNull String getDisplayName() {
        final String content = displayName.content();
        return content.substring(0, Math.min(content.length(), 16));
    }
}
