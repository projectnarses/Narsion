package org.narses.narsion.npc;

import net.kyori.adventure.text.TextComponent;
import org.antlr.v4.runtime.misc.OrderedHashSet;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.util.AsyncUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class Dialogue {

    /**
     * Creates a new dialogue builder
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    private final @NotNull TextComponent[] dialogues;
    private final @NotNull Consumer<TextComponent>[] dialogueFunctions;
    private final @NotNull Duration[] dialogueTimings;

    @SuppressWarnings("unchecked")
    private Dialogue(
            @NotNull OrderedHashSet<TextComponent> dialogueSet,
            @NotNull Map<TextComponent, Consumer<TextComponent>> dialogueFunctions,
            @NotNull Map<TextComponent, Duration> dialogueTimings
    ) {
        this.dialogues = dialogueSet.toArray(TextComponent[]::new);
        this.dialogueFunctions = (Consumer<TextComponent>[]) Arrays.stream(dialogues)
                .map(dialogueFunctions::get)
                .toArray(Consumer[]::new);
        this.dialogueTimings = Arrays.stream(dialogues)
                .map(dialogueTimings::get)
                .toArray(Duration[]::new);
    }

    /**
     * Runs all dialogue once
     */
    public CompletableFuture<?> runAllOnce() {
        return AsyncUtils.scheduleTasks(
                dialogueTimings,
                dialogueFunctions,
                dialogues
        );
    }

    /**
     * Runs all dialogue forever
     */
    public void runAllForever() {
        runAllOnce().thenRun(this::runAllForever);
    }

    public static class Builder {

        private Builder() {
        }

        private @NotNull Consumer<TextComponent> defaultRunner = (ignored) -> {};
        private @NotNull Function<TextComponent, Duration> defaultDuration = (dialogue) ->
                Duration.of(dialogue.content().length() * 200L, ChronoUnit.MILLIS);
        private final @NotNull OrderedHashSet<TextComponent> dialogueSet = new OrderedHashSet<>();
        private final @NotNull Map<TextComponent, Consumer<TextComponent>> dialogueFunctions = new HashMap<>();
        private final @NotNull Map<TextComponent, Duration> dialogueTimings = new HashMap<>();

        public @NotNull Builder defaultRunner(@NotNull Consumer<TextComponent> runner) {
            this.defaultRunner = runner;
            return this;
        }

        public @NotNull Builder defaultDuration(@NotNull Function<TextComponent, Duration> durationFunction) {
            this.defaultDuration = durationFunction;
            return this;
        }

        public @NotNull Builder add(@NotNull TextComponent... dialogue) {
            for (TextComponent component : dialogue) {
                add(component, defaultRunner);
            }
            return this;
        }

        public @NotNull Builder add(@NotNull TextComponent dialogue) {
            return add(dialogue, defaultRunner);
        }

        public @NotNull Builder add(@NotNull TextComponent dialogue, @NotNull Duration duration) {
            return add(dialogue, duration, defaultRunner);
        }

        public @NotNull Builder add(@NotNull TextComponent dialogue, @NotNull Consumer<TextComponent> runner) {
            return add(dialogue, defaultDuration.apply(dialogue), runner);
        }

        public @NotNull Builder add(@NotNull TextComponent dialogue, @NotNull Duration duration, @NotNull Consumer<TextComponent> runner) {
            dialogueSet.add(dialogue);
            dialogueFunctions.put(dialogue, runner);
            dialogueTimings.put(dialogue, duration);
            return this;
        }

        public @NotNull Dialogue build() {
            return new Dialogue(
                    dialogueSet,
                    dialogueFunctions,
                    dialogueTimings
            );
        }
    }
}
