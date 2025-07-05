package tbb.x4.imp.view.collection;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import tbb.x4.api.parser.Tag;
import tbb.x4.ui.panel.editor.XEditorPanel;
import tbb.x4.ui.util.SwingEvent;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

@ApplicationScoped
public class CollectionsViewMouseAdapter extends MouseAdapter {
    private static final Logger LOGGER = Logger.getLogger(XEditorPanel.class);

    private final CollectionsView collectionsView;
    private final SwingEvent<OpenTagEvent> openTagEvent;

    @Inject
    public CollectionsViewMouseAdapter(CollectionsView collectionsView, Event<OpenTagEvent> openTagEvent) {
        this.collectionsView = collectionsView;
        this.openTagEvent = new SwingEvent<>(openTagEvent);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (collectionsView.tree() instanceof JTree tree) {
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
                if (Objects.requireNonNull(userObject) instanceof Tag tag) {
                    openTagEvent.fire(new OpenTagEvent(tag));
                } else {
                    LOGGER.warnf("User object is not a Tag: %s", userObject);
                }
            }
        }
    }
}
