package tbb.x4.ui.menu;

import javax.swing.*;

public interface IMenuItem {
    /**
     * Returns the swing menu item associated with this menu item.
     *
     * @return the JMenuItem for this menu item
     */
    JMenuItem item();

    /**
     * Returns the order of this item in the menu.
     * If two items have the same order, the order will be undefined.
     *
     * @return the order of the item
     */
    int order();
}
