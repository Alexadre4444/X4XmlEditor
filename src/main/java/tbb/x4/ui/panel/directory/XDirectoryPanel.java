package tbb.x4.ui.panel.directory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tbb.x4.api.directory.IXDirectoryService;
import tbb.x4.api.directory.XDirectory;
import tbb.x4.api.directory.XFile;
import tbb.x4.api.directory.XFsElement;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

@ApplicationScoped
public class XDirectoryPanel {

    private final IXDirectoryService directoryService;
    private final XDirectoryMouseAdapter mouseAdapter;
    private JTree tree;

    @Inject
    public XDirectoryPanel(IXDirectoryService directoryService, XDirectoryMouseAdapter mouseAdapter) {
        this.directoryService = directoryService;
        this.mouseAdapter = mouseAdapter;
    }

    public void openDirectory(File directoryFile) {
        XDirectory rootDirectory = directoryService.parseXBaseDirectory(directoryFile);
        tree = new JTree(createTreeNode(rootDirectory));
        tree.addMouseListener(mouseAdapter);
        //TODO: Add scroll bar
    }

    public JTree tree() {
        return tree;
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
}
