package tbb.x4.ui.panel.editor;

import tbb.x4.api.parser.Attribute;
import tbb.x4.api.parser.Tag;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;

public class TagTree extends JTree {

    public TagTree(Tag rootTag) {
        super(new TagTreeModel(rootTag));
        setCellRenderer(new TagTreeCellRenderer());
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        String result = null;
        int selRow = getRowForLocation(event.getX(), event.getY());
        TreePath selPath = getPathForLocation(event.getX(), event.getY());
        if (selRow != -1 && selPath != null) {
            result = switch (selPath.getLastPathComponent()) {
                case Tag tag -> tag.descriptor().description();
                case Attribute attribute -> attribute.descriptor().description();
                default -> null;
            };
        }
        return result;
    }
}
