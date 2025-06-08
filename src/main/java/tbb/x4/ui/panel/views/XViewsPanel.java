package tbb.x4.ui.panel.views;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import tbb.x4.api.view.DataViewUpdatedEvent;
import tbb.x4.api.view.IDataView;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

@ApplicationScoped
public class XViewsPanel {
    private final Logger LOGGER = Logger.getLogger(XViewsPanel.class);

    private final JTabbedPane tabbedPane;
    private final Instance<IDataView> dataViews;

    @Inject
    public XViewsPanel(Instance<IDataView> dataViews) {
        this.dataViews = dataViews;
        tabbedPane = new JTabbedPane();
    }

    public void onDataViewUpdate(@Observes DataViewUpdatedEvent event) {
        LOGGER.infof("Data view updated: %s", event.dataView().title());
        refreshComponent();
    }

    private void refreshComponent() {
        tabbedPane.removeAll();
        dataViews.stream().sorted(Comparator.comparing(IDataView::priority)).forEach(this::addDataView);
        tabbedPane.revalidate();
    }

    private void addDataView(IDataView dataView) {
        tabbedPane.addTab(dataView.title(), dataView.icon(), dataView.tree());
    }

    public Component component() {
        return tabbedPane;
    }
}
