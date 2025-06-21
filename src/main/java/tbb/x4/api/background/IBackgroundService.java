package tbb.x4.api.background;

import java.util.Optional;

public interface IBackgroundService {
    /**
     * Executes a background task and returns a registered task instance.
     *
     * @param task the background task to be executed
     * @return a registered task instance that can be used to track the status or cancel the task
     */
    TaskId doTask(IBackgroundTask task);

    /**
     * Cancels a registered background task.
     *
     * @param id the registered task id to be cancelled
     */
    void cancelTask(TaskId id);

    /**
     * Retrieves the progress of a registered background task.
     *
     * @param id the registered task id whose progress is to be retrieved
     * @return an Optional containing the TaskProgress if the task is found, or empty if not found
     */
    Optional<TaskProgress> getTaskProgress(TaskId id);
}
