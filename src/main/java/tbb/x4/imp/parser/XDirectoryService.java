package tbb.x4.imp.parser;

import jakarta.enterprise.context.ApplicationScoped;
import tbb.x4.api.directory.IXDirectoryService;
import tbb.x4.api.directory.XDirectory;
import tbb.x4.api.directory.XFsElement;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class XDirectoryService implements IXDirectoryService {
    @Override
    public boolean isXBaseDirectory(File file) {
        return file.isDirectory() && file.listFiles() != null &&
                Arrays.stream(Objects.requireNonNull(file.listFiles()))
                        .anyMatch(child -> child.getName().equals("jobeditor.html"));
    }

    @Override
    public XDirectory parseXBaseDirectory(File file) {
        return new XDirectory(file.toPath(), parseChildren(file));
    }

    private List<XFsElement> parseChildren(File directory) {
        File[] files = directory.listFiles();
        return Arrays.stream(Objects.requireNonNull(files))
                .filter(File::isDirectory)
                .map(child -> new XDirectory(child.toPath(), parseChildren(child)))
                .map(XFsElement.class::cast)
                .toList();
    }
}
