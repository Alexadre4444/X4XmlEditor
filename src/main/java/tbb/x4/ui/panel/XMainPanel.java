package tbb.x4.ui.panel;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tbb.x4.ui.panel.editor.XEditor;
import tbb.x4.ui.panel.views.XViewsPanel;

import javax.swing.*;

@ApplicationScoped
public class XMainPanel {
    private final JSplitPane mainPanel;

    @Inject
    public XMainPanel(XViewsPanel xViewsPanel, XEditor xEditor) {
        mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, xViewsPanel.component(), xEditor.tabbedPane());
    }

    public JComponent mainPanel() {
        return mainPanel;
    }
}
