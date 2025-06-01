package tbb.x4.ui.menu.file;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tbb.x4.ui.XFrame;
import tbb.x4.ui.menu.IMenuItem;

import javax.swing.*;

@ApplicationScoped
public class XItemExit implements IMenuItem {
    private final JMenuItem item;

    @Inject
    public XItemExit(XFrame xFrame) {
        item = new JMenuItem("Exit", 'E');
        item.addActionListener(e -> xFrame.exit());
    }

    public JMenuItem item() {
        return item;
    }

    @Override
    public int order() {
        return 10;
    }
}
