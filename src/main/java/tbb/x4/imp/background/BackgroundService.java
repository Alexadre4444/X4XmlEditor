package tbb.x4.imp.background;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import tbb.x4.api.background.*;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class BackgroundService implements IBackgroundService {
    private static final Logger LOGGER = Logger.getLogger(BackgroundService.class);
    private final ConcurrentHashMap<TaskId, TaskInfos> taskInfosMap = new ConcurrentHashMap<>();
    private final Event<TaskProgressEvent> taskProgressEvent;

    @Inject
    public BackgroundService(Event<TaskProgressEvent> taskProgressEvent) {
        this.taskProgressEvent = taskProgressEvent;
    }

    @Override
    public TaskId doTask(IBackgroundTask task) {
        TaskId taskId = new TaskId();
        taskInfosMap.put(taskId, new TaskInfos(taskId, task, TaskProgress.started(), false));
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
            }
        };
    }

    @Override
    public void cancelTask(TaskId id) {
        taskInfosMap.computeIfPresent(id, (taskId, value) -> {
            LOGGER.infof("Cancelling task: %s", taskId);
            return new TaskInfos(
                    taskId,
                    value.task(),
                    TaskProgress.cancelled(),
                    true
            );
        });
    }

    void publishProgress(TaskId taskId, TaskProgress progress) {
        taskInfosMap.put(taskId, new TaskInfos(
                taskId,
                taskInfosMap.get(taskId).task(),
                progress,
                taskInfosMap.get(taskId).isCancelled()
        ));
        taskProgressEvent.fire(new TaskProgressEvent(taskInfosMap.get(taskId).task(), progress));
    }

    boolean isTaskCancelled(TaskId taskId) {
        return taskInfosMap.getOrDefault(taskId, new TaskInfos(taskId, null, TaskProgress.started(), false))
                .isCancelled;
    }

    @Override
    public Optional<TaskProgress> getTaskProgress(TaskId id) {
        return Optional.ofNullable(taskInfosMap.get(id)).map(TaskInfos::progress);
    }

    private record TaskInfos(
            TaskId taskId,
            IBackgroundTask task,
            TaskProgress progress,
            boolean isCancelled
    ) {
    }
}
