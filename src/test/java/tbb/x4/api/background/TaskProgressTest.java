package tbb.x4.api.background;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskProgressTest {


    @Test
    void completedStatusCanHaveProgressOne() {
        assertDoesNotThrow(() -> new TaskProgress(1, TaskStatus.COMPLETED, "Completed"));
    }

    @Test
    void nonCompletedStatusCannotHaveProgressOne() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new TaskProgress(1, TaskStatus.RUNNING, "Running")
        );
        assertEquals("Only status COMPLETED can have progress 1", exception.getMessage());
    }

    @Test
    void progressMustBeBetweenZeroAndOne() {
        assertThrows(IllegalArgumentException.class, () -> new TaskProgress(1.1, TaskStatus.COMPLETED, "Invalid"));
    }

    @Test
    void statusCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new TaskProgress(0, null, "Null status"));
    }

    @Test
    void messageCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new TaskProgress(0, TaskStatus.RUNNING, null));
    }

    @Test
    void completedTaskProgress() {
        TaskProgress progress = TaskProgress.completed();
        assertEquals(1.0, progress.progress());
        assertEquals(TaskStatus.COMPLETED, progress.status());
        assertEquals("Task completed successfully", progress.message());
    }

    @Test
    void startedTaskProgress() {
        TaskProgress progress = TaskProgress.started();
        assertEquals(0.0, progress.progress());
        assertEquals(TaskStatus.RUNNING, progress.status());
        assertEquals("Task started", progress.message());
    }

    @Test
    void runningTaskProgress() {
        TaskProgress progress = TaskProgress.running(0.5, "Task is running");
        assertEquals(0.5, progress.progress());
        assertEquals(TaskStatus.RUNNING, progress.status());
        assertEquals("Task is running", progress.message());
    }

    @Test
    void failedTaskProgress() {
        TaskProgress progress = TaskProgress.failed("Task failed");
        assertEquals(0.0, progress.progress());
        assertEquals(TaskStatus.FAILED, progress.status());
        assertEquals("Task failed", progress.message());
    }

    @Test
    void cancelledTaskProgress() {
        TaskProgress progress = TaskProgress.cancelled();
        assertEquals(0.0, progress.progress());
        assertEquals(TaskStatus.CANCELLED, progress.status());
        assertEquals("Task was cancelled", progress.message());
    }
}
