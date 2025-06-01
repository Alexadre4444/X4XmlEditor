package tbb.x4.ui.panel;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tbb.x4.ui.panel.directory.XDirectoryPanel;
import tbb.x4.ui.panel.editor.XEditor;

import javax.swing.*;
import java.io.File;

@ApplicationScoped
public class XMainPanel {
    private final XDirectoryPanel xDirectoryPanel;
    private final XEditor xEditor;
    private JSplitPane mainPanel;

    @Inject
    public XMainPanel(XDirectoryPanel xDirectoryPanel, XEditor xEditor) {
        this.xDirectoryPanel = xDirectoryPanel;
        this.xEditor = xEditor;
    }

    public void openDirectory(File directory) {
        xDirectoryPanel.openDirectory(directory);
        mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, xDirectoryPanel.tree(), xEditor.tabbedPane());
    }

    public JComponent mainPanel() {
        return mainPanel;
    }
}
