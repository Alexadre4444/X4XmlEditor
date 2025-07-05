package tbb.x4.imp.project;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import tbb.x4.api.background.IBackgroundService;
import tbb.x4.api.background.IBackgroundTask;
import tbb.x4.api.directory.XDirectory;
import tbb.x4.api.parser.Collection;
import tbb.x4.api.parser.IX4CollectionsServices;
import tbb.x4.api.project.CollectionsLoadEvent;
import tbb.x4.api.project.IProjectService;
import tbb.x4.api.project.ProjectLoadEvent;
import tbb.x4.imp.background.BackgroundTask;
import tbb.x4.imp.parser.XDirectoryService;
import tbb.x4.ui.util.SwingEvent;

import java.io.File;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ProjectService implements IProjectService {
    private static final Logger LOGGER = Logger.getLogger(ProjectService.class);

    private final XDirectoryService directoryService;
    private final SwingEvent<ProjectLoadEvent> projectLoadEvent;
    private final SwingEvent<CollectionsLoadEvent> collectionsLoadEventEvent;
    private final IX4CollectionsServices collectionsServices;
    private final IBackgroundService backgroundService;
    private List<Collection> collections = Collections.emptyList();
    private XDirectory projectDirectory;

    @Inject
    public ProjectService(XDirectoryService directoryService, Event<ProjectLoadEvent> projectLoadEvent,
                          Event<CollectionsLoadEvent> collectionsLoadEventEvent, IX4CollectionsServices collectionsServices, IBackgroundService backgroundService) {
        this.directoryService = directoryService;
        this.projectLoadEvent = new SwingEvent<>(projectLoadEvent);
        this.collectionsLoadEventEvent = new SwingEvent<>(collectionsLoadEventEvent);
        this.collectionsServices = collectionsServices;
        this.backgroundService = backgroundService;
    }

    @Override
    public void loadProject(File projectFile) {
        backgroundService.doTask(makeLoadProjectTask(projectFile));
    }

    private IBackgroundTask makeLoadProjectTask(File projectFile) {
        return new BackgroundTask("Load project", observer -> {
            LOGGER.infof("Loading project from file: %s", projectFile.getAbsolutePath());
            observer.publishProgress("Parsing project directory");
            projectDirectory = directoryService.parseXBaseDirectory(projectFile);
            projectLoadEvent.fire(new ProjectLoadEvent(projectDirectory));
            LOGGER.infof("Project loaded: %s", projectDirectory.path());
            observer.publishProgress("Loading collections");
            LOGGER.infof("Loading collections for project: %s", projectDirectory.path());
            collections = collectionsServices.getCollections(projectDirectory);
            collectionsLoadEventEvent.fire(new CollectionsLoadEvent(collections));
            LOGGER.infof("Collections loaded: %d for project: %s", collections.size(), projectDirectory.path());
        });
    }

    @Override
    public XDirectory projectDirectory() {
        return projectDirectory;
    }

    @Override
    public List<Collection> collections() {
        return collections;
    }
}
