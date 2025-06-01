package tbb.x4.ui.menu;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import javax.swing.*;
import java.util.Comparator;

@ApplicationScoped
public class XMenuBar {
    private final JMenuBar menuBar;

    @Inject
    public XMenuBar(Instance<IMenu> menus) {
        menuBar = new JMenuBar();
        menus.stream()
                .sorted(Comparator.comparing(IMenu::order))
                .map(IMenu::menu).forEach(menuBar::add);
    }

    public JMenuBar menuBar() {
        return menuBar;
    }
}
