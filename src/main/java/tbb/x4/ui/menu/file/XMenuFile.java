package tbb.x4.ui.menu.file;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import tbb.x4.ui.menu.IMenu;
import tbb.x4.ui.menu.IMenuItem;

import javax.swing.*;
import java.util.Comparator;

@ApplicationScoped
public class XMenuFile implements IMenu {
    private final JMenu menu;

    @Inject
    public XMenuFile(@FileMenu Instance<IMenuItem> items) {
        menu = new JMenu("File");
        items.stream()
                .sorted(Comparator.comparing(IMenuItem::order))
                .map(IMenuItem::item).forEach(menu::add);
    }

    @Override
    public JMenu menu() {
        return menu;
    }

    @Override
    public int order() {
        return 0;
    }
}
