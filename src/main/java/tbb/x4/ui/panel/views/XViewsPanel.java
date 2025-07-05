package tbb.x4.ui.panel.views;

import com.formdev.flatlaf.ui.FlatScrollPaneBorder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import tbb.x4.api.view.DataViewUpdatedEvent;
import tbb.x4.api.view.IDataView;

import javax.swing.*;
import java.awt.*;

@ApplicationScoped
public class XViewsPanel {
    private final Logger LOGGER = Logger.getLogger(XViewsPanel.class);

    private final JTabbedPane tabbedPane;

    @Inject
    public XViewsPanel(Instance<IDataView> dataViews) {
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setBorder(new FlatScrollPaneBorder());

    }

    public void onDataViewUpdate(@Observes DataViewUpdatedEvent event) {
        handleDataViewUpdate(event);
    }

    private void handleDataViewUpdate(DataViewUpdatedEvent event) {
        LOGGER.infof("Data view updated: %s", event.dataView().title());
        if (!isDataViewPresent(event.dataView())) {
            addDataView(event.dataView());
        } else {
            LOGGER.warnf("Data view '%s' is already present, skipping addition.", event.dataView().title());
        }
    }

    private boolean isDataViewPresent(IDataView dataView) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(dataView.title())) {
                return true;
            }
        }
        return false;
    }

    private void addDataView(IDataView dataView) {
        tabbedPane.insertTab(dataView.title(), dataView.icon(), new JScrollPane(dataView.tree()), null,
                computeTabIndex(dataView));
    }

    private int computeTabIndex(IDataView dataView) {
        int index = 0;
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).compareTo(dataView.title()) > 0) {
                index = i;
                break;
            }
        }
        return index;
    }

    public Component component() {
        return tabbedPane;
    }
}
