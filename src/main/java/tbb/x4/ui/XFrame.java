package tbb.x4.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import tbb.x4.ui.menu.XMenuBar;
import tbb.x4.ui.panel.XMainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;

@ApplicationScoped
public class XFrame {

    private static final Logger LOGGER = Logger.getLogger(XFrame.class);

    private final XMenuBar xMenuBar;
    private final XMainPanel xMainPanel;
    private JFrame frame;

    @Inject
    public XFrame(XMenuBar xMenuBar, XMainPanel xMainPanel) {
        this.xMenuBar = xMenuBar;
        this.xMainPanel = xMainPanel;
    }

    public void init() {
        LOGGER.info("Initializing X4 Xml Editor");
        FlatDarkLaf.setup();
        GraphicsEnvironment graphics =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        frame = new JFrame("X4 Xml Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(xMenuBar.menuBar());
        frame.setSize(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());
        frame.setVisible(true);
    }

    public void openDirectory(File directory) {
        LOGGER.infof("Opening directory: %s", directory.getAbsolutePath());
        xMainPanel.openDirectory(directory);
        frame.add(xMainPanel.mainPanel());
        frame.revalidate();
    }

    public void exit() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
