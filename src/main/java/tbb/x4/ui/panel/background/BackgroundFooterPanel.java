package tbb.x4.ui.panel.background;

import com.formdev.flatlaf.ui.FlatScrollPaneBorder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import tbb.x4.api.background.*;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class BackgroundFooterPanel {
    private final JPanel footerPanel;
    private final Map<IBackgroundTask, TaskProgress> tasks;
    private final TaskProgressPanel taskProgressPanel;
    private final JLabel numberOfTasksLabel;

    public BackgroundFooterPanel() {
        this.footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        tasks = new HashMap<>();
        numberOfTasksLabel = new JLabel(makeNumberOfTasksLabel());
        footerPanel.add(numberOfTasksLabel);
        taskProgressPanel = new TaskProgressPanel(getMostCompletedTask());
        footerPanel.add(taskProgressPanel);
        footerPanel.setBorder(new FlatScrollPaneBorder());
    }

    private String makeNumberOfTasksLabel() {
        return String.format("<html>Number of tasks: <b>%d</b></html>", tasks.size());
    }

    public JPanel panel() {
        return footerPanel;
    }

    protected void onTaskProgressEvent(@Observes TaskProgressEvent taskProgressEvent) {
        handleTaskProgressEvent(taskProgressEvent);
    }

    private void handleTaskProgressEvent(TaskProgressEvent taskProgressEvent) {
        if (taskProgressEvent.progress().status().equals(TaskStatus.COMPLETED)) {
            tasks.remove(taskProgressEvent.task());
        } else {
            tasks.put(taskProgressEvent.task(), taskProgressEvent.progress());
        }
        updateFooterPanel();
    }

    private void updateFooterPanel() {
        numberOfTasksLabel.setText(makeNumberOfTasksLabel());
        if (tasks.isEmpty()) {
            taskProgressPanel.removeTaskProgress();
        } else {
            IBackgroundTask mostCompletedTask = getMostCompletedTask();
            taskProgressPanel.updateTaskProgress(mostCompletedTask, tasks.get(mostCompletedTask));
        }
    }

    private IBackgroundTask getMostCompletedTask() {
        IBackgroundTask result = tasks.entrySet().stream()
                .filter(entry -> entry.getValue().progress().mode().equals(Progress.ProgressMode.DETERMINED))
                .max(Map.Entry.comparingByValue(Comparator.comparingDouble(taskProgress -> taskProgress.progress().progress())))
                .map(Map.Entry::getKey)
                .orElse(null);
        if (result == null) {
            result = tasks.entrySet().stream()
                    .filter(entry -> entry.getValue().progress().mode().equals(Progress.ProgressMode.UNDETERMINED))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
        }
        return result;
    }
}
