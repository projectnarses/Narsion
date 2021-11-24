package org.narses.narsion.dev.world.narsionworlddata.quests;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.quest.Quest;
import org.narses.narsion.util.Equivalent;

import java.util.function.Supplier;

public enum NarsionQuests implements Equivalent<Quest> {
    EXAMPLE(PvPTutorial::new),
    REGION(RegionTutorial::new),
    ;

    final @NotNull Quest quest;

    NarsionQuests(@NotNull Supplier<Quest> questSupplier) {
        this.quest = questSupplier.get();
    }

    @Override
    public @NotNull Quest getEquivalent() {
        return quest;
    }
}
