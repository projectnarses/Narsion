package org.narses.narsion.npc;

import net.minestom.server.entity.GameMode;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface NonPlayableCharacterSource {
    public default @NotNull PlayerInfoPacket.AddPlayer generatePlayerAddInfo() {
        return new PlayerInfoPacket.AddPlayer(getUUID(), getDisplayName(), GameMode.CREATIVE, 0);
    }

    public default @NotNull PlayerInfoPacket.RemovePlayer generatePlayerHideInfo() {
        return new PlayerInfoPacket.RemovePlayer(getUUID());
    }

    public @NotNull UUID getUUID();
    public @NotNull String getDisplayName();
    public @NotNull NonPlayableCharacter spawn(@NotNull Instance instance);
}
