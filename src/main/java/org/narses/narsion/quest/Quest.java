package org.narses.narsion.quest;

import net.minestom.server.entity.Player;
import net.minestom.server.network.PacketProcessor;
import net.minestom.server.network.packet.client.handshake.HandshakePacket;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.player.NarsionPlayer;

import java.util.concurrent.atomic.AtomicInteger;

public interface Quest {

    @NotNull QuestStep @NotNull [] steps();
    @NotNull String id();

    default @NotNull QuestEmbarkInfo embark(final @NotNull NarsionServer server, final @NotNull Player player) {
        final NarsionPlayer narsionPlayer = server.wrap(player);
        Quest.Status status = narsionPlayer.getQuestStatus(this);

        switch (status) {
            case COMPLETED -> {
                return QuestEmbarkInfo.PREVIOUSLY_COMPLETED;
            }
            case ACTIVE -> {
                return QuestEmbarkInfo.CURRENTLY_ACTIVE;
            }
        }

        final QuestStep[] steps = steps();
        // Use an atomic to handle the current step's index
        final AtomicInteger currentStep = new AtomicInteger(-1);
        final Quest quest = this;

        // Create a runnable that runs the next step, or completes the quest
        final Runnable handleStep = new Runnable() {
            @Override
            public void run() {
                // Get the step index
                int i = currentStep.incrementAndGet();

                // If quest is finished run Quest#complete and exit
                if (i == steps.length) {
                    quest.complete(server, player);
                    return;
                }

                // Get the current step
                QuestStep step = steps[i];

                // Setup current step
                step.setup(server, player, this);
            }
        };

        // Run the runnable once to start the quest
        handleStep.run();

        return QuestEmbarkInfo.EMBARKED;
    }

    void complete(final @NotNull NarsionServer server, final @NotNull Player player);

    @FunctionalInterface
    public interface QuestStep {
        void setup(final @NotNull NarsionServer server, final @NotNull Player player, final @NotNull Runnable nextStep);
    }

    public enum QuestEmbarkInfo {
        PREVIOUSLY_COMPLETED(true, "You have already completed this quest."),
        CURRENTLY_ACTIVE(true, "You have already embarked on this quest."),
        EMBARKED(false, "You have embarked on this quest.");

        final @NotNull String infoMessage;
        final boolean isError;

        QuestEmbarkInfo(boolean isError, @NotNull String infoMessage) {
            this.infoMessage = infoMessage;
            this.isError = isError;
        }

        public @NotNull String getInfoMessage() {
            return infoMessage;
        }

        public boolean isError() {
            return isError;
        }
    }

    public enum Status {
        COMPLETED,
        UNCOMPLETED,
        ACTIVE;
    }
}
