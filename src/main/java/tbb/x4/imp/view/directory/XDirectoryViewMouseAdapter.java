package tbb.x4.imp.view.directory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import tbb.x4.api.directory.XFile;
import tbb.x4.ui.panel.editor.XEditor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

@ApplicationScoped
public class XDirectoryViewMouseAdapter extends MouseAdapter {
    private static final Logger LOGGER = Logger.getLogger(XEditor.class);

    private final DirectoryView directoryView;
    private final XEditor xEditor;

    @Inject
    public XDirectoryViewMouseAdapter(DirectoryView directoryView, XEditor xEditor) {
        this.directoryView = directoryView;
        this.xEditor = xEditor;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (directoryView.tree() instanceof JTree tree) {
            handleMouseClick(e, tree);
        }
    }

    private void handleMouseClick(MouseEvent e, JTree tree) {
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        if (selRow != -1 && selPath != null) {
            if (e.getClickCount() == 2) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                Object userObject = node.getUserObject();
                if (Objects.requireNonNull(userObject) instanceof XFile file) {
                    xEditor.openFile(file);
                }
            }
        }
    }
}
