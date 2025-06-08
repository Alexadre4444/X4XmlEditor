package tbb.x4.imp.view.directory;

import tbb.x4.api.directory.XFile;
import tbb.x4.api.directory.XFsElement;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class DirectoryTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DirectoryTreeCellRenderer component = (DirectoryTreeCellRenderer) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (leaf) {
            DefaultMutableTreeNode treeNode = ((DefaultMutableTreeNode) value);
            XFsElement element = (XFsElement) treeNode.getUserObject();
            if (element instanceof XFile xFile) {
                String[] parts = xFile.name().split("\\.");
                if (parts[parts.length - 1].equalsIgnoreCase("xml")) {
                    component.setIcon(new DirectoryTreeLeafIcon(Color.GREEN));
                }
            }
        }
        return component;
    }
}
