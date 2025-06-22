package tbb.x4.api.background;

public record TaskProgressEvent(IBackgroundTask task, TaskProgress progress) {

    public TaskProgressEvent {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (progress == null) {
            throw new IllegalArgumentException("Progress cannot be null");
        }
    }
}
