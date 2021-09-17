package org.narses.narsion.dev.world.narsionworlddata.quests;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerStartSneakingEvent;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.events.PlayerAbilityEvent;
import org.narses.narsion.quest.Quest;
import org.narses.narsion.util.AsyncUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

class PvPTutorial implements Quest {

    private final QuestStep[] steps = {
            // Move around
            (server, player, nextStep) -> {
                // Register the event listener
                @NotNull EventListener<PlayerMoveEvent> eventListener = EventListener.builder(PlayerMoveEvent.class)
                        .filter((event) -> event.getPlayer().equals(player))
                        .filter((event) -> !event.getNewPosition().sameView(player.getPosition()))
                        .handler((event) -> nextStep.run())
                        .expireCount(1)
                        .build();

                MinecraftServer.getGlobalEventHandler().addListener(eventListener);

                player.sendMessage(
                        Component.text()
                                .append(
                                        Component.text("You started the basic tutorial. ")
                                                .color(NamedTextColor.AQUA),
                                        Component.text("Move around ")
                                                .color(NamedTextColor.YELLOW),
                                        Component.text("to continue.")
                                                .color(NamedTextColor.AQUA)
                                )
                );
            },

            // Shift
            (server, player, nextStep) -> AsyncUtils.scheduleTasks(
                    Duration.of(3, ChronoUnit.SECONDS),
                    player::sendMessage,
                    Component.text("Good Job.").color(NamedTextColor.AQUA),
                    Component.text("In the world of Narses, fighting is essential to survival.").color(NamedTextColor.AQUA),
                    Component.text("You will need to learn how to fight in order to progress through this world.").color(NamedTextColor.AQUA),
                    Component.text()
                            .append(
                                    Component.text("First and foremost, ")
                                            .color(NamedTextColor.AQUA),
                                    Component.text("hold the sneak key to sneak")
                                            .color(NamedTextColor.YELLOW),
                                    Component.text(".")
                                            .color(NamedTextColor.AQUA)
                            )
            ).thenRun(() -> {
                // Register the event listener
                EventListener<PlayerStartSneakingEvent> eventListener = EventListener.builder(PlayerStartSneakingEvent.class)
                        .filter((event) -> event.getPlayer().equals(player))
                        .handler((event) -> nextStep.run())
                        .expireCount(1)
                        .build();

                MinecraftServer.getGlobalEventHandler().addListener(eventListener);
            }),

            // Right click to activate ability
            (server, player, nextStep) -> AsyncUtils.scheduleTasks(
                    Duration.of(3, ChronoUnit.SECONDS),
                    player::sendMessage,
                    Component.text("Good Job.").color(NamedTextColor.AQUA),
                    Component.text()
                            .append(
                                    Component.text("Now, ")
                                            .color(NamedTextColor.AQUA),
                                    Component.text("right click while sneaking. ")
                                            .color(NamedTextColor.YELLOW),
                                    Component.text("This will activate your player class's ability.")
                                            .color(NamedTextColor.AQUA)
                            )
            ).thenRun(() -> {
                // Register the event listener
                EventListener<PlayerAbilityEvent> eventListener = EventListener.builder(PlayerAbilityEvent.class)
                        .filter((event) -> event.getPlayer().equals(player))
                        .handler((event) -> nextStep.run())
                        .expireCount(1)
                        .build();

                MinecraftServer.getGlobalEventHandler().addListener(eventListener);
            })
    };

    @Override
    public @NotNull QuestStep @NotNull [] steps() {
        return steps;
    }

    @Override
    public @NotNull String id() {
        new IllegalAccessException("Quest id accessed from package-private class instead of enum name.").printStackTrace();
        return "";
    }

    @Override
    public void complete(@NotNull NarsionServer server, @NotNull Player player) {
        AsyncUtils.scheduleTasks(
                Duration.of(3, ChronoUnit.SECONDS),
                player::sendMessage,
                Component.text("Congrats! You have completed the basics tutorial.").color(NamedTextColor.GREEN),
                Component.text()
                        .append(
                                Component.text("If you would like to do more tutorials, feel free to do the command '")
                                        .color(NamedTextColor.GREEN),
                                Component.text("/tutorials")
                                        .color(NamedTextColor.YELLOW),
                                Component.text("'.")
                                        .color(NamedTextColor.GREEN)
                        )
        );
    }
}
