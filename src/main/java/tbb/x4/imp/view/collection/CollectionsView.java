package tbb.x4.imp.view.collection;

import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import tbb.x4.api.parser.Collection;
import tbb.x4.api.parser.Tag;
import tbb.x4.api.project.CollectionsLoadEvent;
import tbb.x4.api.view.DataViewUpdatedEvent;
import tbb.x4.api.view.IDataView;
import tbb.x4.ui.util.SwingEvent;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.List;

@ApplicationScoped
public class CollectionsView implements IDataView {

    private final SwingEvent<DataViewUpdatedEvent> dataViewUpdatedEvent;
    private final JTree tree;

    @Inject
    public CollectionsView(Event<DataViewUpdatedEvent> dataViewUpdatedEvent, CollectionsViewMouseAdapter mouseAdapter) {
        this.dataViewUpdatedEvent = new SwingEvent<>(dataViewUpdatedEvent);
        tree = new JTree(new DefaultTreeModel(createDefaultRootNode()));
        tree.addMouseListener(mouseAdapter);
    }

    public void onCollectionsLoad(@Observes CollectionsLoadEvent event) {
        loadCollections(event.collections());
    }

    public void loadCollections(List<Collection> collections) {
        tree.setModel(new DefaultTreeModel(createTreeNode(collections)));
        dataViewUpdatedEvent.fire(new DataViewUpdatedEvent(this));
    }

    private DefaultMutableTreeNode createDefaultRootNode() {
        return new DefaultMutableTreeNode("Collections");
    }

    private DefaultMutableTreeNode createTreeNode(List<Collection> collections) {
        DefaultMutableTreeNode root = createDefaultRootNode();
        for (Collection collection : collections) {
            DefaultMutableTreeNode collectionNode = new DefaultMutableTreeNode(collection);
            for (Tag tag : collection.elements()) {
                collectionNode.add(createTreeNode(tag));
            }
            root.add(collectionNode);
        }
        return root;
    }

    private DefaultMutableTreeNode createTreeNode(Tag tag) {
        return new DefaultMutableTreeNode(tag);
    }

    @Override
    public Component tree() {
        return tree != null ? tree : new JPanel();
    }

    @Override
    public String title() {
        return "Collections view";
    }

    @Nullable
    @Override
    public Icon icon() {
        return null;
    }
}
