package tbb.x4.ui.menu;

import javax.swing.*;

public interface IMenu {
    /**
     * Returns the swing menu associated with this menu.
     *
     * @return the JMenu for this menu
     */
    JMenu menu();

    /**
     * Returns the order of this menu in the menu bar.
     * If two menus have the same order, the order will be undefined.
     *
     * @return the order of the menu
     */
    int order();
}
