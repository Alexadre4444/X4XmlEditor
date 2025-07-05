package tbb.x4.api.background;

public record TaskProgress(Progress progress, TaskStatus status, String message) {
    public TaskProgress {
        if (progress == null) {
            throw new IllegalArgumentException("Progress cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        if (progress.mode().equals(Progress.ProgressMode.DETERMINED) && progress.progress() == 1 && status != TaskStatus.COMPLETED) {
            throw new IllegalArgumentException("Only status COMPLETED can have progress 1");
        }
    }

    public static TaskProgress completed() {
        return new TaskProgress(Progress.determined(1), TaskStatus.COMPLETED, "Task completed successfully");
    }

    public static TaskProgress started() {
        return new TaskProgress(Progress.determined(0), TaskStatus.RUNNING, "Task started");
    }

    public static TaskProgress running(double progress, String message) {
        return new TaskProgress(Progress.determined(progress), TaskStatus.RUNNING, message);
    }

    public static TaskProgress running(String message) {
        return new TaskProgress(Progress.undetermined(), TaskStatus.RUNNING, message);
    }

    public static TaskProgress failed(String message) {
        return new TaskProgress(Progress.determined(0), TaskStatus.FAILED, message);
    }

    public static TaskProgress cancelled() {
        return new TaskProgress(Progress.determined(0), TaskStatus.CANCELLED, "Task was cancelled");
    }
}
