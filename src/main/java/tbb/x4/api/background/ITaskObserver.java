package tbb.x4.api.background;

public interface ITaskObserver {
    /**
     * Publishes the progress of a task.
     * This method is called to update the observers about the current progress of a task.
     * Progress should be a value between 0.0 (exclusive) and 1.0 (exclusive),
     * where 0.0 means no progress and 1.0 means the task is completed.
     *
     * @param progress the current progress of the task
     */
    void publishProgress(double progress, String message);

    /**
     * Call this method to indicate that the task can be safely stop now if it has been requested to cancel.
     */
    void cancelIfRequested();
}
