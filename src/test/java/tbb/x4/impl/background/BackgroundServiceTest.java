package tbb.x4.impl.background;

import org.junit.jupiter.api.Test;
import tbb.x4.api.background.IBackgroundService;
import tbb.x4.api.background.TaskId;
import tbb.x4.api.background.TaskProgress;
import tbb.x4.api.background.TaskStatus;
import tbb.x4.imp.background.BackgroundService;
import tbb.x4.imp.background.BackgroundTask;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BackgroundServiceTest {
    IBackgroundService backgroundService = new BackgroundService();

    @Test
    void doTaskStartTask() {
        AtomicBoolean startTask = new AtomicBoolean(false);
        AtomicBoolean continueTask = new AtomicBoolean(false);
        AtomicBoolean onComplete = new AtomicBoolean(false);
        TaskId taskId = backgroundService.doTask(new BackgroundTask("pouet", observer -> {
            await().until(startTask::get);
            observer.publishProgress(0.5, "Task is running");
            await().until(continueTask::get);
        }, () -> {
            onComplete.set(true);
        }));
        Optional<TaskProgress> taskProgress = backgroundService.getTaskProgress(taskId);
        assertTrue(taskProgress.isPresent());
        assertEquals(TaskProgress.started(), taskProgress.get());

        startTask.set(true);
        await().until(() -> backgroundService.getTaskProgress(taskId).isPresent() &&
                backgroundService.getTaskProgress(taskId).get().progress() == 0.5);
        taskProgress = backgroundService.getTaskProgress(taskId);
        assertTrue(taskProgress.isPresent());
        assertEquals(TaskProgress.running(0.5, "Task is running"), taskProgress.get());

        continueTask.set(true);
        await().until(() -> backgroundService.getTaskProgress(taskId).isPresent() &&
                backgroundService.getTaskProgress(taskId).get().status() == TaskStatus.COMPLETED);
        taskProgress = backgroundService.getTaskProgress(taskId);
        assertTrue(taskProgress.isPresent());
        assertEquals(TaskProgress.completed(), taskProgress.get());
        assertTrue(onComplete.get(), "onComplete should have been called");
    }

}
