package org.narses.narsion.dev.world.narsionworlddata.quests;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.quest.Quest;

import java.util.function.Supplier;

public enum NarsionQuests implements Quest {
    EXAMPLE(PvPTutorial::new);

    final @NotNull Quest quest;

    NarsionQuests(@NotNull Supplier<Quest> questSupplier) {
        this.quest = questSupplier.get();
    }

    @Override
    public @NotNull QuestStep @NotNull [] steps() {
        return quest.steps();
    }

    @Override
    public @NotNull String id() {
        return name();
    }

    @Override
    public void complete(@NotNull NarsionServer server, @NotNull Player player) {
        quest.complete(server, player);
    }
}
