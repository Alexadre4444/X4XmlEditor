package tbb.x4.ui;

import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import tbb.x4.ui.menu.XMenuBar;
import tbb.x4.ui.panel.XMainPanel;
import tbb.x4.ui.panel.background.BackgroundFooterPanel;
import tbb.x4.ui.util.CheckThreadViolationRepaintManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

@ApplicationScoped
public class XFrame {

    private static final Logger LOGGER = Logger.getLogger(XFrame.class);

    private final XMenuBar xMenuBar;
    private final XMainPanel xMainPanel;
    private final BackgroundFooterPanel backgroundFooterPanel;
    private JFrame frame;

    @Inject
    public XFrame(XMenuBar xMenuBar, XMainPanel xMainPanel, BackgroundFooterPanel backgroundFooterPanel) {
        this.xMenuBar = xMenuBar;
        this.xMainPanel = xMainPanel;
        this.backgroundFooterPanel = backgroundFooterPanel;
    }

    public void init() {
        SwingUtilities.invokeLater(() -> {
            LOGGER.info("Initializing X4 Xml Editor");
            RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
            setupTheme();
            GraphicsEnvironment graphics =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice device = graphics.getDefaultScreenDevice();
            frame = new JFrame("X4 Xml Editor");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setJMenuBar(xMenuBar.menuBar());
            frame.setSize(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());
            frame.add(xMainPanel.mainPanel(), BorderLayout.CENTER);
            frame.add(backgroundFooterPanel.panel(), BorderLayout.SOUTH);

            frame.setVisible(true);
        });
    }

    public void exit() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    private void setupTheme() {
        FlatDarkPurpleIJTheme.setup();
        UIManager.put("Tree.paintLines", true);
        UIManager.put("Tree.showDefaultIcons", true);
    }
}
