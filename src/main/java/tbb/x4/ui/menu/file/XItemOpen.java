package tbb.x4.ui.menu.file;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tbb.x4.api.background.IBackgroundService;
import tbb.x4.api.directory.IXDirectoryService;
import tbb.x4.api.project.IProjectService;
import tbb.x4.ui.menu.IMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.YES_NO_OPTION;

@ApplicationScoped
@FileMenu
public class XItemOpen implements IMenuItem {
    private final JMenuItem item;
    private final IXDirectoryService directoryService;
    private final IProjectService projectService;
    private final IBackgroundService backgroundService;

    @Inject
    public XItemOpen(IXDirectoryService directoryService, IProjectService projectService, IBackgroundService backgroundService) {
        this.directoryService = directoryService;
        this.projectService = projectService;
        this.backgroundService = backgroundService;
        item = new JMenuItem("Open", 'O');
        item.addActionListener(this::displayOpenFrame);
    }

    private void displayOpenFrame(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (directoryService.isXBaseDirectory(fileChooser.getSelectedFile())) {
                projectService.loadProject(fileChooser.getSelectedFile());
            } else {
                int response = JOptionPane.showConfirmDialog(null,
                        "Selected directory does not seem to be a valid X4 directory. Do you want to continue?",
                        "X4 directory", YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    projectService.loadProject(fileChooser.getSelectedFile());
                }
            }
        }
    }

    public JMenuItem item() {
        return item;
    }

    @Override
    public int order() {
        return 1;
    }
}
