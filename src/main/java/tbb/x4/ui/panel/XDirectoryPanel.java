package tbb.x4.ui.panel;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.swing.*;

@ApplicationScoped
public class XDirectoryPanel {

    private final JTree tree;

    @Inject
    public XDirectoryPanel() {
        this.tree = new JTree();
    }

    public JComponent mainPanel() {
        return tree;
    }
}
