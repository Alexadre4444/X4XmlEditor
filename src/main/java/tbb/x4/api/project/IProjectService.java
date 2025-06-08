package tbb.x4.api.project;

import tbb.x4.api.directory.XDirectory;
import tbb.x4.api.parser.Collection;

import java.io.File;
import java.util.List;

public interface IProjectService {
    /**
     * Opens a project from the specified file path.
     *
     * @param projectFile the file to load the project from
     */
    void loadProject(File projectFile);

    /**
     * Get actual project directory.
     *
     * @return the current project directory
     */
    XDirectory projectDirectory();

    /**
     * Retrieves a list of collections in the current project.
     *
     * @return a list of collection
     */
    List<Collection> collections();
}
