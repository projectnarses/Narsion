package org.narses.narsion.util;

import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class AsyncUtils {
    /**
     * Schedules the specified tasks to run with the specified duration between them
     * @param duration the duration between the tasks
     * @param runnables the tasks
     * @return a completable future
     */
    public static @NotNull CompletableFuture<?> scheduleTasks(
            @NotNull final Duration duration,
            @NotNull final Runnable... runnables
    ) {
        if (runnables.length == 0) {
            return CompletableFuture.completedFuture(null);
        }

        final CompletableFuture<?> completableFuture = new CompletableFuture<>();

        AtomicInteger currentIndex = new AtomicInteger(-1);

        // Create the runnable that runs the next runnable
        Runnable nextConsumer = new Runnable() {
            @Override
            public void run() {
                // Get next index
                final int i = currentIndex.incrementAndGet();
                if (i == runnables.length) {
                    completableFuture.complete(null);
                    return;
                }

                // Run consumer
                runnables[i].run();

                // Schedule next runnable
                MinecraftServer.getSchedulerManager()
                        .buildTask(this)
                        .delay(duration)
                        .schedule();
            }
        };

        // Run the first runnable
        nextConsumer.run();

        return completableFuture;
    }

    /**
     * Schedules the consumer to accept the specified elements in order, with the specified duration of time between them
     * @param duration the duration between consumers
     * @param consumer the consumer to run
     * @param elements the elements
     * @param <T> the type of the element
     * @return a completable future
     */
    public static <T> CompletableFuture<?> scheduleTasks(
            @NotNull final Duration duration,
            @NotNull final Consumer<T> consumer,
            @NotNull final T... elements
    ) {
        if (elements.length == 0) {
            return CompletableFuture.completedFuture(null);
        }

        final CompletableFuture<?> completableFuture = new CompletableFuture<>();

        AtomicInteger currentIndex = new AtomicInteger(-1);

        // Create the runnable that runs the next consumer
        Runnable nextConsumer = new Runnable() {
            @Override
            public void run() {
                final int i = currentIndex.incrementAndGet();

                // Run consumer
                consumer.accept(elements[i]);

                if (i + 1 == elements.length) {
                    completableFuture.complete(null);
                    return;
                }

                // Schedule next element
                MinecraftServer.getSchedulerManager()
                        .buildTask(this)
                        .delay(duration)
                        .schedule();
            }
        };

        // Run the first iteration
        nextConsumer.run();

        return completableFuture;
    }

    /**
     * Schedules the consumer to accept the specified elements in order, with the specified duration of time between them
     * @param durations the durations between consumers
     * @param consumers the consumers to run
     * @param elements the elements
     * @param <T> the type of the element
     */
    public static <T> CompletableFuture<?> scheduleTasks(
            @NotNull final Duration[] durations,
            @NotNull final Consumer<T>[] consumers,
            @NotNull final T[] elements
    ) {
        final CompletableFuture<?> completableFuture = new CompletableFuture<>();

        AtomicInteger currentIndex = new AtomicInteger(-1);

        // Create the runnable that runs the next consumer
        Runnable nextConsumer = new Runnable() {
            @Override
            public void run() {
                final int i = currentIndex.incrementAndGet();

                // Run consumer
                consumers[i].accept(elements[i]);

                if (i + 1 == elements.length) {
                    // Schedule completion
                    MinecraftServer.getSchedulerManager()
                            .buildTask(() -> completableFuture.complete(null))
                            .delay(durations[i])
                            .schedule();
                    return;
                }

                // Schedule next element
                MinecraftServer.getSchedulerManager()
                        .buildTask(this)
                        .delay(durations[i])
                        .schedule();
            }
        };

        if (elements.length == 0) {
            completableFuture.complete(null);
            return completableFuture;
        }

        // Run the first iteration
        nextConsumer.run();

        return completableFuture;
    }
}
