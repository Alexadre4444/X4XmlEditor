package tbb.x4.ui.panel.background;

import tbb.x4.api.background.IBackgroundTask;
import tbb.x4.api.background.Progress;
import tbb.x4.api.background.TaskProgress;

import javax.swing.*;
import java.awt.*;

public class TaskProgressPanel extends Panel {
    private final JProgressBar progressBar;
    private final JLabel taskLabel;
    private boolean isEmpty = true;

    public TaskProgressPanel(IBackgroundTask task) {
        setLayout(new FlowLayout());
        progressBar = new JProgressBar(0, 100);
        progressBar.setDoubleBuffered(true);
        taskLabel = new JLabel();
    }

    public void updateTaskProgress(IBackgroundTask backgroundTask, TaskProgress taskProgress) {
        if (isEmpty) {
            this.removeAll();
            add(taskLabel);
            add(progressBar);
            isEmpty = false;
        }
        taskLabel.setText(backgroundTask.label());
        if (taskProgress.progress().mode().equals(Progress.ProgressMode.DETERMINED)) {
            progressBar.setIndeterminate(false);
            progressBar.setValue((int) (taskProgress.progress().progress() * 100));
            progressBar.setStringPainted(true);
        } else {
            progressBar.setIndeterminate(true);
            progressBar.setStringPainted(false);
        }
        revalidate();
        repaint();
    }

    public void removeTaskProgress() {
        this.removeAll();
        isEmpty = true;
        revalidate();
        repaint();
    }
}
