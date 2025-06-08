package tbb.x4.api.project;

import tbb.x4.api.directory.XDirectory;

/**
 * Event triggered when a project is loaded.
 * This event carries the directory of the loaded project.
 */
public class ProjectLoadEvent {
    private final XDirectory projectDirectory;

    public ProjectLoadEvent(XDirectory projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    /**
     * Returns the directory of the loaded project.
     *
     * @return the project directory
     */
    public XDirectory directory() {
        return projectDirectory;
    }
}
