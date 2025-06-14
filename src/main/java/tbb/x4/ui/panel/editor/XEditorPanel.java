package tbb.x4.ui.panel.editor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;
import tbb.x4.api.model.ElementValue;
import tbb.x4.api.parser.Attribute;
import tbb.x4.api.parser.Tag;
import tbb.x4.imp.view.collection.OpenTagEvent;

import javax.swing.*;

@ApplicationScoped
public class XEditorPanel {
    private static final Logger LOGGER = Logger.getLogger(XEditorPanel.class);

    private final JTabbedPane tabbedPane;

    public XEditorPanel() {
        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.tabbedPane.setTabPlacement(JTabbedPane.TOP);
    }

    public JTabbedPane tabbedPane() {
        return tabbedPane;
    }

    public void onOpenTagEvent(@Observes OpenTagEvent event) {
        LOGGER.info("OpenTagEvent received: " + event.tag().descriptor().name());
        addTagEditorTab(event.tag());
    }

    public void addTagEditorTab(Tag tag) {
        String tabTitle = getTabTitle(tag);
        if (tabbedPane.indexOfTab(tabTitle) == -1) {
            TagEditorPanel tagEditorPanel = new TagEditorPanel(tag);
            tabbedPane.addTab(tabTitle, tagEditorPanel.scrollPane());
            tabbedPane.setSelectedComponent(tagEditorPanel.scrollPane());
            LOGGER.infof("Added new tag editor for: %s", tabTitle);
        } else {
            LOGGER.warnf("Tag editor for '%s' already exists, skipping addition.", tabTitle);
        }
    }

    private boolean isTabTitlePresent(String tabTitle) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(tabTitle)) {
                return true;
            }
        }
        return false;
    }

    private String getTabTitle(Tag tag) {
        String tabTitle = getBaseTabTitle(tag);
        if (isTabTitlePresent(tabTitle)) {
            int count = 1;
            String baseTitle = tabTitle;
            while (isTabTitlePresent(tabTitle)) {
                tabTitle = "%s (%d)".formatted(baseTitle, count);
                count++;
            }
        }
        return tabTitle;
    }

    private String getBaseTabTitle(Tag tag) {
        return tag.findIdAttribute()
                .map(Attribute::value)
                .map(ElementValue::value)
                .map(name -> "%s (%s)".formatted(name, tag.descriptor().name().name()))
                .orElseGet(() -> tag.descriptor().name().name());
    }
}
