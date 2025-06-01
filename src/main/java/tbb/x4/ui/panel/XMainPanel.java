package tbb.x4.ui.panel;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.swing.*;
import java.io.File;

@ApplicationScoped
public class XMainPanel {
    private final XDirectoryPanel xDirectoryPanel;
    private JSplitPane mainPanel;

    @Inject
    public XMainPanel(XDirectoryPanel xDirectoryPanel) {
        this.xDirectoryPanel = xDirectoryPanel;
    }

    public void openDirectory(File directory) {
        mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, xDirectoryPanel.mainPanel(), new JButton("Pouet"));
    }

    public JComponent mainPanel() {
        return mainPanel;
    }
}
