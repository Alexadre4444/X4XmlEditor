package tbb.x4.imp.view.directory;

import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import tbb.x4.api.directory.XDirectory;
import tbb.x4.api.directory.XFile;
import tbb.x4.api.directory.XFsElement;
import tbb.x4.api.project.ProjectLoadEvent;
import tbb.x4.api.view.DataViewUpdatedEvent;
import tbb.x4.api.view.IDataView;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

@ApplicationScoped
public class DirectoryView implements IDataView {

    private final XDirectoryViewMouseAdapter mouseAdapter;
    private final Event<DataViewUpdatedEvent> dataViewUpdatedEvent;
    private final JTree tree;

    @Inject
    public DirectoryView(XDirectoryViewMouseAdapter mouseAdapter, Event<DataViewUpdatedEvent> dataViewUpdatedEvent) {
        this.mouseAdapter = mouseAdapter;
        this.dataViewUpdatedEvent = dataViewUpdatedEvent;
        tree = new JTree(new DefaultTreeModel(createDefaultRootNode()));
        tree.setCellRenderer(new DirectoryTreeCellRenderer());
        tree.addMouseListener(mouseAdapter);
    }

    private DefaultMutableTreeNode createDefaultRootNode() {
        return new DefaultMutableTreeNode("Root Directory");
    }

    private void loadCollections(XDirectory directory) {
        tree.setModel(new DefaultTreeModel(createTreeNode(directory)));
        dataViewUpdatedEvent.fire(new DataViewUpdatedEvent(this));
    }

    public void onProjectLoad(@Observes ProjectLoadEvent event) {
        loadCollections(event.directory());
    }

    private DefaultMutableTreeNode createTreeNode(XDirectory directory) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(directory);
        for (XFsElement subElement : directory.children()) {
            switch (subElement) {
                case XDirectory subDirectory -> node.add(createTreeNode(subDirectory));
                case XFile file -> node.add(createTreeNode(file));
            }
        }
        return node;
    }

    private DefaultMutableTreeNode createTreeNode(XFile file) {
        return new DefaultMutableTreeNode(file);
    }

    @Override
    public Component tree() {
        return tree != null ? tree : new JPanel();
    }

    @Override
    public String title() {
        return "Directory view";
    }

    @Nullable
    @Override
    public Icon icon() {
        return null;
    }
}
