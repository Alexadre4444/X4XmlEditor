package tbb.x4.ui.panel.editor;

import tbb.x4.api.parser.Tag;

import javax.swing.*;

public class TagEditorPanel {
    private final JScrollPane scrollPane;

    public TagEditorPanel(Tag rootTag) {
        JTree tagTree = new JTree(new TagTreeModel(rootTag));
        tagTree.setCellRenderer(new TagTreeCellRenderer());
        this.scrollPane = new JScrollPane(tagTree);
    }

    public JScrollPane scrollPane() {
        return scrollPane;
    }
}
