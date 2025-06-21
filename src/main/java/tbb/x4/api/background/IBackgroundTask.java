package tbb.x4.api.background;

import java.util.function.Consumer;

public interface IBackgroundTask {
    /**
     * Returns the label of the task.
     *
     * @return the label of the task
     */
    String label();

    /**
     * Returns the runnable that will be executed in the background.
     * This runnable should not update the UI directly.
     *
     * @return the runnable to be executed
     */
    Consumer<ITaskObserver> consumer();

    /**
     * Returns the runnable that will be executed when the task is completed.
     * This runnable can be used to update the UI or perform any final actions.
     *
     * @return the runnable to be executed on completion
     */
    Runnable onComplete();
}
