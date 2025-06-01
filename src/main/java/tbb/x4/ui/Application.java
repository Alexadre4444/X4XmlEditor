package tbb.x4.ui;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.weld.environment.se.events.ContainerInitialized;


@ApplicationScoped
public class Application {

    private final XFrame xFrame;

    @Inject
    public Application(XFrame xFrame) {
        this.xFrame = xFrame;
    }

    public void start(@Observes ContainerInitialized startEvent) {
        xFrame.init();
    }
}
