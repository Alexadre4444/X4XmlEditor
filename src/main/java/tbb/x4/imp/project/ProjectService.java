package tbb.x4.imp.project;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import tbb.x4.api.directory.XDirectory;
import tbb.x4.api.parser.Collection;
import tbb.x4.api.parser.IX4CollectionsServices;
import tbb.x4.api.project.CollectionsLoadEvent;
import tbb.x4.api.project.IProjectService;
import tbb.x4.api.project.ProjectLoadEvent;
import tbb.x4.imp.parser.XDirectoryService;

import java.io.File;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ProjectService implements IProjectService {
    private static final Logger LOGGER = Logger.getLogger(ProjectService.class);

    private final XDirectoryService directoryService;
    private final Event<ProjectLoadEvent> projectLoadEvent;
    private final Event<CollectionsLoadEvent> collectionsLoadEventEvent;
    private final IX4CollectionsServices collectionsServices;
    private List<Collection> collections = Collections.emptyList();
    private XDirectory projectDirectory;

    @Inject
    public ProjectService(XDirectoryService directoryService, Event<ProjectLoadEvent> projectLoadEvent,
                          Event<CollectionsLoadEvent> collectionsLoadEventEvent, IX4CollectionsServices collectionsServices) {
        this.directoryService = directoryService;
        this.projectLoadEvent = projectLoadEvent;
        this.collectionsLoadEventEvent = collectionsLoadEventEvent;
        this.collectionsServices = collectionsServices;
    }

    @Override
    public void loadProject(File projectFile) {
        LOGGER.infof("Loading project from file: %s", projectFile.getAbsolutePath());
        projectDirectory = directoryService.parseXBaseDirectory(projectFile);
        projectLoadEvent.fire(new ProjectLoadEvent(projectDirectory));
        LOGGER.infof("Project loaded: %s", projectDirectory.path());
        LOGGER.infof("Loading collections for project: %s", projectDirectory.path());
        collections = collectionsServices.getCollections(projectDirectory);
        collectionsLoadEventEvent.fire(new CollectionsLoadEvent(collections));
        LOGGER.infof("Collections loaded: %d for project: %s", collections.size(), projectDirectory.path());
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
