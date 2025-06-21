package tbb.x4.imp.background;

import tbb.x4.api.background.ITaskObserver;
import tbb.x4.api.background.TaskId;
import tbb.x4.api.background.TaskProgress;
import tbb.x4.api.background.TaskStatus;

record TaskObserver(BackgroundService backgroundService, TaskId observedId) implements ITaskObserver {
    TaskObserver {
        if (backgroundService == null) {
            throw new IllegalArgumentException("BackgroundService cannot be null");
        }
        if (observedId == null) {
            throw new IllegalArgumentException("Observed TaskId cannot be null");
        }
    }

    @Override
    public void publishProgress(double progress, String message) {
        backgroundService.publishProgress(observedId, new TaskProgress(progress, TaskStatus.RUNNING, message));
    }

    @Override
    public void cancelIfRequested() {
        if (backgroundService.isTaskCancelled(observedId)) {
            throw new CancelledException();
        }
    }
}
