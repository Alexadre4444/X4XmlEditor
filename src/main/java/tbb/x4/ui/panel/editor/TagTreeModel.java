package tbb.x4.ui.panel.editor;

import tbb.x4.api.parser.Tag;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.List;
import java.util.stream.Stream;

public class TagTreeModel implements TreeModel {

    private final Tag rootTag;

    public TagTreeModel(Tag rootTag) {
        this.rootTag = rootTag;
    }

    @Override
    public Object getRoot() {
        return rootTag;
    }

    private Tag castToTag(Object node) {
        if (!(node instanceof Tag tag)) {
            throw new IllegalArgumentException("Node must be an instance of Tag");
        }
        return tag;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return getTagChildren(castToTag(parent)).get(index);
    }

    private List<Object> getTagChildren(Tag tag) {
        List<Object> children = Stream.concat(
                        tag.subTags().stream().map(Object.class::cast),
                        tag.attributes().stream().map(Object.class::cast))
                .toList();
        if (tag.value() != null) {
            children = Stream.concat(Stream.of(tag.value()).map(Object.class::cast),
                    children.stream()).toList();
        }
        return children;
    }

    @Override
    public int getChildCount(Object parent) {
        int result = 0;
        if (parent instanceof Tag tag) {
            result = getTagChildren(tag).size();
        }
        return result;
    }

    @Override
    public boolean isLeaf(Object node) {
        boolean result = true;
        if (node instanceof Tag tag) {
            result = tag.subTags().isEmpty() && tag.attributes().isEmpty() && tag.value() == null;
        }
        return result;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        Tag parentTag = castToTag(parent);
        Tag childTag = castToTag(child);
        return parentTag.subTags().indexOf(childTag);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
    }

}
