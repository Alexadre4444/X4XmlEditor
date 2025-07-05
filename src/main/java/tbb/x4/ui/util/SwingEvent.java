package tbb.x4.ui.util;

import jakarta.enterprise.event.Event;

import javax.swing.*;

public class SwingEvent<T> {
    private final Event<T> delegate;

    public SwingEvent(Event<T> delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        this.delegate = delegate;
    }

    /**
     * Fires the event with the provided data on the Swing Event Dispatch Thread (EDT).
     *
     * @param eventData the data to be passed with the event, must not be null
     * @throws IllegalArgumentException if eventData is null
     */
    public void fire(T eventData) {
        if (eventData == null) {
            throw new IllegalArgumentException("Event data cannot be null");
        }
        SwingUtilities.invokeLater(() -> delegate.fire(eventData));
    }
}
