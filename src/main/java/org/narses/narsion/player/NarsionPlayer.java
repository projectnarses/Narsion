package org.narses.narsion.player;

import net.minestom.server.entity.Player;
import net.minestom.server.tag.Tag;
import net.minestom.server.tag.TagReadable;
import net.minestom.server.tag.TagWritable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.classes.PlayerClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NarsionPlayer implements TagReadable, TagWritable {
    protected final @NotNull NarsionServer server;

    // Player object
    protected final @NotNull Player player;

    // Player data
    private final @NotNull NBTCompound nbtData;
    private @NotNull PlayerClass playerClass;

    protected NarsionPlayer(@NotNull NarsionServer server, @NotNull Player player) {
        this.player = player;
        this.server = server;
        this.playerClass = server.getPlayerClasses().WARRIOR;
        this.nbtData = new NBTCompound();

        // TODO: Read player data from database
        this.setTag(TAG_ABILITY_SELECTION_MODE, "SCROLL");
    }

    public void setPlayerClass(@NotNull PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    public @NotNull PlayerClass getPlayerClass() {
        return playerClass;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public <T> @Nullable T getTag(@NotNull Tag<T> tag) {
        return tag.read(nbtData);
    }

    @Override
    public <T> void setTag(@NotNull Tag<T> tag, @Nullable T value) {
        tag.write(nbtData, value);
    }

    // Data tags
    public static final Tag<String> TAG_ABILITY_SELECTION_MODE = Tag.String("narsion:ability_selection_mode");
}
