package tbb.x4.api.background;

public record TaskProgress(double progress, TaskStatus status, String message) {
    public TaskProgress {
        if (progress < 0 || progress > 1) {
            throw new IllegalArgumentException("Progress must be between 0 and 1");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        if (progress == 1 && status != TaskStatus.COMPLETED) {
            throw new IllegalArgumentException("Only status COMPLETED can have progress 1");
        }
    }

    public static TaskProgress completed() {
        return new TaskProgress(1.0, TaskStatus.COMPLETED, "Task completed successfully");
    }

    public static TaskProgress started() {
        return new TaskProgress(0, TaskStatus.RUNNING, "Task started");
    }

    public static TaskProgress running(double progress, String message) {
        return new TaskProgress(progress, TaskStatus.RUNNING, message);
    }

    public static TaskProgress failed(String message) {
        return new TaskProgress(0, TaskStatus.FAILED, message);
    }

    public static TaskProgress cancelled() {
        return new TaskProgress(0, TaskStatus.CANCELLED, "Task was cancelled");
    }
}
