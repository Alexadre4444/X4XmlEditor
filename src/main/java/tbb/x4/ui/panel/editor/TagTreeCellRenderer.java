package tbb.x4.ui.panel.editor;

import tbb.x4.api.model.AttributeDescriptor;
import tbb.x4.api.model.ElementValue;
import tbb.x4.api.parser.Attribute;
import tbb.x4.api.parser.Tag;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class TagTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final Color PARENT_COLOR = new Color(0x000B58);
    private static final Color ATTRIBUTE_COLOR = new Color(0x1f9669);

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        TagTreeCellRenderer component = (TagTreeCellRenderer) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value instanceof Attribute(AttributeDescriptor descriptor, ElementValue value1)) {
            component.setIcon(new AttributeTreeLeafIcon(ATTRIBUTE_COLOR));
            component.setText("%s=%s".formatted(descriptor.name(), value1.stringValue()));
        } else if (value instanceof Tag) {
            component.setIcon(getIconForTag(expanded));
        }
        return component;
    }

    private Icon getIconForTag(boolean expanded) {
        return expanded ? new TagTreeOpenIcon(PARENT_COLOR) : new TagTreeClosedIcon(PARENT_COLOR);
    }
}
