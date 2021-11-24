package org.narses.narsion.dev.world.narsionworlddata.quests;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.quest.ConstructedQuest;
import org.narses.narsion.quest.Quest;
import org.narses.narsion.util.AsyncUtils;

import java.time.Duration;

public class RegionTutorial extends ConstructedQuest {

    public RegionTutorial() {
        super();

        // Add the starting step
        addStep(this::startTutorial);
    }

    // Exit the spawn region step
    private void startTutorial(@NotNull NarsionServer server, @NotNull Player player, @NotNull Runnable nextStep) {
        AsyncUtils.scheduleTasks(
                // One second between each task
                Duration.ofSeconds(1),
                // Notify the player that they have started the region tutorial
                () -> player.sendMessage("You have started the region tutorial."),
                // Run the next step
                nextStep
        );
    }

    @Override
    public void complete(@NotNull NarsionServer server, @NotNull Player player) {
        // Notify the player that they have completed the region tutorial
        player.sendMessage("You have completed the region tutorial.");
    }
}
