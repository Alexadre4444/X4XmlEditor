package tbb.x4.ui.panel.editor;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import tbb.x4.api.directory.XFile;

import javax.swing.*;

@ApplicationScoped
public class XEditor {
    private static final Logger LOGGER = Logger.getLogger(XEditor.class);

    private final JTabbedPane tabbedPane;

    public XEditor() {
        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.tabbedPane.setTabPlacement(JTabbedPane.TOP);
    }

    public JTabbedPane tabbedPane() {
        return tabbedPane;
    }

    public void openFile(XFile file) {
        LOGGER.infof("Opening file: %s", file.name());
    }
}
