package tbb.x4.imp.background;

import tbb.x4.api.background.*;

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
        backgroundService.publishProgress(observedId, new TaskProgress(Progress.determined(progress), TaskStatus.RUNNING, message));
    }

    @Override
    public void publishProgress(String message) {
        backgroundService.publishProgress(observedId, new TaskProgress(Progress.undetermined(), TaskStatus.RUNNING, message));
    }

    @Override
    public void cancelIfRequested() {
        if (backgroundService.isTaskCancelled(observedId)) {
            throw new CancelledException();
        }
    }
}
