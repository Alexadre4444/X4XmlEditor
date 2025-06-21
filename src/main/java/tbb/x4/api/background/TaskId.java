package tbb.x4.api.background;

import java.util.UUID;

public record TaskId(UUID id) {
    public TaskId {
        if (id == null) {
            throw new IllegalArgumentException("Task ID cannot be null");
        }
    }

    public TaskId() {
        this(UUID.randomUUID());
    }
}
