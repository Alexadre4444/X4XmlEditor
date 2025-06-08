package tbb.x4.api.view;

import jakarta.annotation.Nullable;

import javax.swing.*;
import java.awt.*;

public interface IDataView {

    /**
     * Returns the component representing the data view.
     * This is typically a JTree component that displays the directory structure.
     * If the view is not yet loaded, it may return null or an empty component.
     *
     * @return the component
     */
    Component tree();

    /**
     * Returns the view's title.
     *
     * @return the title of the view
     */
    String title();

    /**
     * Returns the icon representing the view.
     *
     * @return the icon of the view
     */
    @Nullable
    Icon icon();

    /**
     * Return the view's priority for sorting.
     */
    int priority(); // Default priority for sorting views, can be overridden by implementations
}
