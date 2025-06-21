package tbb.x4.imp.background;

import tbb.x4.api.background.IBackgroundTask;
import tbb.x4.api.background.ITaskObserver;

import java.util.function.Consumer;

public record BackgroundTask(
        String label,
        Consumer<ITaskObserver> consumer,
        Runnable onComplete
) implements IBackgroundTask {
    public BackgroundTask {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Task label cannot be null or empty");
        }
        if (consumer == null) {
            throw new IllegalArgumentException("Task consumer cannot be null");
        }
        if (onComplete == null) {
            throw new IllegalArgumentException("Task onComplete cannot be null");
        }
    }

    public BackgroundTask(String label, Consumer<ITaskObserver> consumer) {
        this(label, consumer, () -> {
        });
    }
}
