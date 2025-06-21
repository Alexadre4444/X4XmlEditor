package tbb.x4.imp.background;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import tbb.x4.api.background.IBackgroundService;
import tbb.x4.api.background.IBackgroundTask;
import tbb.x4.api.background.TaskId;
import tbb.x4.api.background.TaskProgress;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class BackgroundService implements IBackgroundService {
    private static final Logger LOGGER = Logger.getLogger(BackgroundService.class);
    private final ConcurrentHashMap<TaskId, Boolean> taskCancellationStatus = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<TaskId, TaskProgress> taskProgressMap = new ConcurrentHashMap<>();

    @Override
    public TaskId doTask(IBackgroundTask task) {
        TaskId taskId = new TaskId();
        taskCancellationStatus.put(taskId, false);
        LOGGER.infof("Starting task: %s", task.label());
        publishProgress(taskId, TaskProgress.started());
        new Thread(makeTaskRunnable(taskId, task)).start();
        return taskId;
    }

    private Runnable makeTaskRunnable(TaskId taskId, IBackgroundTask task) {
        return () -> {
            try {
                task.consumer().accept(new TaskObserver(this, taskId));
                publishProgress(taskId, TaskProgress.completed());
                task.onComplete().run();
            } catch (CancelledException e) {
                LOGGER.infof("Task %s was cancelled", taskId);
                publishProgress(taskId, TaskProgress.cancelled());
            } catch (Exception e) {
                LOGGER.errorf(e, "Error executing task %s", taskId);
                publishProgress(taskId, TaskProgress.failed(e.getMessage()));
            } finally {
                taskCancellationStatus.remove(taskId);
            }
        };
    }

    @Override
    public void cancelTask(TaskId id) {
        taskCancellationStatus.computeIfPresent(id, (taskId, value) -> {
            LOGGER.infof("Cancelling task: %s", taskId);
            return true; // Mark the task as cancelled
        });
    }

    void publishProgress(TaskId taskId, TaskProgress progress) {
        taskProgressMap.put(taskId, progress);
    }

    boolean isTaskCancelled(TaskId taskId) {
        return taskCancellationStatus.getOrDefault(taskId, false);
    }

    @Override
    public Optional<TaskProgress> getTaskProgress(TaskId id) {
        return Optional.ofNullable(taskProgressMap.get(id));
    }
}
