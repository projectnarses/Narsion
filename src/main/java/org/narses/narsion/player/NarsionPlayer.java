package org.narses.narsion.player;

import net.minestom.server.entity.Player;
import net.minestom.server.tag.Tag;
import net.minestom.server.tag.TagReadable;
import net.minestom.server.tag.TagWritable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.classes.PlayerClass;
import org.narses.narsion.quest.Quest;
import org.narses.narsion.social.Guild;
import org.narses.narsion.social.SocialMember;
import org.narses.narsion.social.SocialRank;
import org.narses.narsion.util.TagUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NarsionPlayer implements TagReadable, TagWritable, SocialMember {
    protected final @NotNull NarsionServer server;

    // Player object
    protected final @NotNull Player player;

    // Player data
    private final @NotNull MutableNBTCompound nbtData;
    private @NotNull PlayerClass playerClass;

    // Quests
    private final Set<Quest> completedQuests = new HashSet<>();
    private final Set<Quest> activeQuests = new HashSet<>();

    protected NarsionPlayer(@NotNull NarsionServer server, @NotNull Player player) {
        this.player = player;
        this.server = server;
        this.playerClass = server.getPlayerClasses().WARRIOR;
        this.nbtData = new MutableNBTCompound();

        // TODO: Read player data from database
//        this.setTag(TAG_ABILITY_SELECTION_MODE, "SCROLL");
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

    public @NotNull Quest.Status getQuestStatus(@NotNull Quest quest) {
        if (getCompletedQuests().contains(quest)) {
            return Quest.Status.COMPLETED;
        }

        if (getActiveQuests().contains(quest)) {
            return Quest.Status.ACTIVE;
        }

        return Quest.Status.NOT_STARTED;
    }

    public @NotNull Set<Quest> getCompletedQuests() {
        return completedQuests;
    };

    public @NotNull Set<Quest> getActiveQuests() {
        return activeQuests;
    };

    // Data tags
    public static final Tag<String> TAG_ABILITY_SELECTION_MODE = Tag.String("narsion:ability_selection_mode").defaultValue("SCROLL");
    public static final Tag<@NotNull Boolean> TAG_VISITED_ELSINORE = TagUtils.Boolean("narsion:visited_elsinore").defaultValue(false);

    @Override
    public @Nullable SocialRank getRank() {
        return server.getSocialsManager().getRank(this);
    }

    @Override
    public @NotNull UUID getUuid() {
        return this.player.getUuid();
    }

    public @Nullable Guild getGuild() {
        return server.getSocialsManager().getGroupFromMember(this);
    }
}
